package com;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

        menu();
    }

    private void menu() throws IOException{
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite Seu nome:");

        String nome = scanner.nextLine();

        this.outputStream.writeObject(new FileMessage(nome));

        int option = 0;

        while(option != -1){
            System.out.println("1 - Sair | 2 - Enviar");
            option = scanner.nextInt();

            if (option == 2) {
                send(nome);
            }else if (option == 1){
                System.exit(0);
            }
        }
    }

    private void send(String nome) throws IOException {
        JFileChooser fileChooser = new JFileChooser();

        int opt = fileChooser.showOpenDialog(null);

        if(opt == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();

            this.outputStream.writeObject(new FileMessage(nome, file));
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
