package com.itmo.client;

import com.company.project.ClassName;
import com.company.project.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {
    private InetSocketAddress address;
    private String username;
    private Scanner scanner;
    private double ping;

    public Client(InetSocketAddress address) {
        this.address = address;
        scanner = new Scanner(System.in);
    }

    public void startClient() {
        System.out.println("Введите имя");
        username = scanner.nextLine();
        while (true) {
            System.out.println("Введите текст");
            String text = scanner.nextLine();
            try (ClassName connectionHandler =
                         new ClassName(new Socket(
                                 address.getHostName(),
                                 address.getPort()
                         ))) {
                Message message = new Message(username);
                message.setText(text);
                try {
                    connectionHandler.send(message);
                    Message fromServer = connectionHandler.read();
                    System.out.println(fromServer.getText());
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
        }
    }
    public void ping(){
        try {
            InetAddress host = InetAddress.getByName(address.getHostName());
            double afterSend;
            double beforeSend = System.currentTimeMillis();
            try {
                if (host.isReachable(10000)){
                    afterSend = System.currentTimeMillis();
                    ping = afterSend - beforeSend;
                    System.out.println("Пинг = " + ping);
                }
            } catch (IOException e) {
                System.out.println("Не удалось подключиться к серверу");
            }
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост");
        }
    }
}
