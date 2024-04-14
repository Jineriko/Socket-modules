package server;

import com.company.project.ClassName;
import com.company.project.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Server {
    private int port;
    private int successfullyProcessedRequests;
    private int helpCounter;
    private int pingCounter;
    private int requestsCounter;
    private int popularCounter;
    private Map<String, Integer> popularCommands = new HashMap<>();

    public Server(int port) {
        this.port = port;
        popularCommands.put("/help", helpCounter);
        popularCommands.put("/ping", pingCounter);
        popularCommands.put("/requests", requestsCounter);
        popularCommands.put("/popular", popularCounter);
    }

    // устанавливаем соединение
    // получает от клиента сообщение
    // формирует ответное сообщение
    // отправляем сообщение клиенту
    public void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            successfullyProcessedRequests = 0;
            helpCounter = 0;
            pingCounter = 0;
            requestsCounter = 0;
            popularCounter = 0;
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClassName connectionHandler = new ClassName(socket);
                    Message fromClient = connectionHandler.read();
                    if (fromClient.getText().startsWith("/")) {
                        switch (fromClient.getText()) {
                            case "/help" -> connectionHandler.send(this.help());
                            case "/requests" -> connectionHandler.send(this.requests());
                            case "/ping" ->  connectionHandler.send(this.ping(socket.getLocalAddress().getHostName()));
                            case "/popular" -> connectionHandler.send(this.popular());
                            default -> {
                                Message message = new Message("server");
                                message.setText("Неизвестная команда");
                                connectionHandler.send(message);
                            }
                        }
                    } else {
                        System.out.println(fromClient.getText());
                        Message message = new Message("server");
                        message.setText("text");
                        connectionHandler.send(message);
                        successfullyProcessedRequests++;
                    }
                } catch (Exception e) {
                    System.out.println("Проблема с соединением");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
            throw new RuntimeException(e);
        }
    }
    // Расширение функционала сервера.

    // Сервер может обрабатывать следующие запросы:
    // /help - список доступных запросов и их описание
    // /ping - время ответа сервера
    // /requests - количество успешно обработанных запросов
    // /popular - название самого популярного запроса

    //Если сервер не может обработать запрос (пришла команда, которую не может обработать сервер),
    //он должен отправить клиенту сообщение с соответствующей информацией.
    public Message help() {
        Message message = new Message("server");
        message.setText("Список доступных запросов:\n" +
                "/ping - время ответа сервера\n" +
                "/requests - количество успешно обработанных запросов\n" +
                "/popular - название самого популярного запроса\n");
        helpCounter++;
        successfullyProcessedRequests++;
        popularCommands.put("/help", helpCounter);
        return message;
    }

    public Message requests() {
        Message message = new Message("server");
        message.setText("Количество успешно обработанных запросов = " + successfullyProcessedRequests + "\n");
        requestsCounter++;
        successfullyProcessedRequests++;
        popularCommands.put("/help", requestsCounter);
        return message;
    }

    public Message popular() {
        int max = 0;
        String stringCommand = null;
        for (Map.Entry<String, Integer> entry: popularCommands.entrySet()){
            if (max < entry.getValue()){
                max = entry.getValue();
                stringCommand = entry.getKey();
            }
        }
        Message message = new Message("server");
        message.setText("Самый популярный запрос = " + stringCommand + "\n");
        popularCounter++;
        successfullyProcessedRequests++;
        popularCommands.put("/popular", popularCounter);
        return message;
    }

    public Message ping(String hostName) {
        Message message = new Message("server");
        try {
            InetAddress host = InetAddress.getByName(hostName);
            double afterSend;
            double beforeSend = System.currentTimeMillis();
            try {
                if (host.isReachable(10000)) {
                    afterSend = System.currentTimeMillis();
                    message.setText("Пинг = " + (afterSend - beforeSend) + "\n");
                    pingCounter++;
                    successfullyProcessedRequests++;
                    popularCommands.put("/ping", pingCounter);
                }
            } catch (IOException e) {
                System.out.println("Не удалось подключиться к серверу");
            }
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост");
        }
        return message;
    }
}

