package com;

import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Cliente {
    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws IOException {
        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new ListenerSocket(socket)).start();

        menu();
    }

    private void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite Seu nome:");

        String nome = scanner.nextLine();

        this.outputStream.writeObject(new FileMessage(nome));

        int option = 0;

        while (option != -1) {
            System.out.println("1 - Sair | 2 - Enviar");
            option = scanner.nextInt();

            if (option == 2) {
                send(nome);
            } else if (option == 1) {
                System.exit(0);
            }
        }
    }

    private void send(String nome) throws IOException {
        JFileChooser fileChooser = new JFileChooser();

        int opt = fileChooser.showOpenDialog(null);

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            this.outputStream.writeObject(new FileMessage(nome, file));
        }
    }

    public class ListenerSocket implements Runnable {

        private ObjectInputStream inputStream;

        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            FileMessage menssage = null;

            try {
                while ((menssage = (FileMessage) inputStream.readObject()) != null) {
                    System.out.println("\nVocê Recebeu um arquivo de " + menssage.getCliente());
                    System.out.println("O arquivo é " + menssage.getFile().getName());

                    salvar(menssage);

                    System.out.println("1 - Sair | 2 - Enviar");
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        private void salvar(FileMessage message) {
            try {
                FileInputStream fileInputStream = new FileInputStream(message.getFile());
                FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\lucas\\OneDrive\\Documentos\\NetBeansProjects\\TrabalhoProgParalela\\src"
                        + message.getFile().getName());

                FileChannel fin = fileInputStream.getChannel();
                FileChannel fout = fileOutputStream.getChannel();

                long size = fin.size();

                fin.transferTo(0, size, fout);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void main(String[] args) {
        try {
            new Cliente();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
