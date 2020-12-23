package com;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// Este servidor faz a comunicação entre clientes
public class Servidor {
    private ServerSocket serverSocket;
    private Socket socket;


    public Servidor() {
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Servidor On!");
            
            while(true){
                socket = serverSocket.accept(); // inicializa quanto alguém conecta a porta 5555

                // thread ouvinte do servidor
                new Thread(new ListenerSocket(socket)).start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // implementação da thread ouvinte
    private class ListenerSocket implements Runnable{
        private ObjectOutputStream outputStream; // objeto que será responsável pelo envio de mensagens
        private ObjectInputStream inputStream; // quem recebe as mensagens

        private FileMessage message = new FileMessage();

        // criando instancias
        public ListenerSocket(Socket socket) throws IOException{
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }



        private BufferedImage converter() throws IOException {

            BufferedImage image = ImageIO.read(message.getFile());

            int height = image.getHeight();
            int width = image.getWidth();


            Converter quadranteC = new Converter(image, 0, height/2, 0, width/2);
            Converter quadranteD = new Converter(image,0, height/2, width/2, width );
            Converter quadranteA = new Converter(image, height/2, height, 0, width/2 );
            Converter quadranteB = new Converter(image, height/2, height, width/2, width );

            quadranteA.run();
            quadranteB.run();
            quadranteC.run();
            quadranteD.run();

            return image;
        }

        @Override
        public void run() {
            // variavel que representa o arquivo
            this.message = null;
            
            try {
                while((message = (FileMessage) inputStream.readObject()) != null ){
                    if (message.getFile() != null) { // se for diferente de null possui uma mensagem

                        BufferedImage convertedImage = converter();

                        // salva em um arquivo temporário para poder enviar e depois exclui
                        File temporario = new File("src/temporario.png");
                        ImageIO.write(convertedImage, "PNG", temporario);


                        this.outputStream.writeObject(new FileMessage(temporario));
                        temporario.delete();
                    }
                }
            } catch (IOException ex) {
                System.out.println(message.getCliente() + " Desconectou");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }
}
