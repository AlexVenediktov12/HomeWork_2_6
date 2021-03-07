package ru.geekbrains.HomeWork_2_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String ADDRESS = "localhost";
    private static final int PORT = 11111;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(ADDRESS, PORT);
            System.out.println("Подключен к серверу");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Поток на чтение
            Thread threadReader = new Thread(() -> {
                try {
                    while (true) {
                        out.writeUTF(scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadReader.setDaemon(true);
            threadReader.start();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/close")) {
                    System.out.println("Потеряно соединение с сервером");
                    out.writeUTF("/close");
                    break;
                } else {
                    System.out.println("Сервер " + str);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}


