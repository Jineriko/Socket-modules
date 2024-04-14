package com.company.project;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClassName implements AutoCloseable {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;

    public ClassName(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream()); // сначала его создаем, чтобы сообщение уходило
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    // метод отправки Message по сокет соединению
    public void send(Message message) throws IOException {
        message.setSentAt(LocalDateTime.now());
        outputStream.writeObject(message);
        outputStream.flush();
    }
    // метод получения Message по сокет соединению
    public Message read() throws IOException {
        try {
            return (Message) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
