package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.ExecutorServiceHandler;
import com.z.udemyjavanio.handlers.PrintingHandler;
import com.z.udemyjavanio.handlers.ThreadedHandler;
import com.z.udemyjavanio.handlers.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ExecutorServiceBlockingServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        var serverSocket = new ServerSocket(port);
        var handler = new ExecutorServiceHandler<>(
                new PrintingHandler<>(new TransmogrifyHandler()),
                Executors.newCachedThreadPool(),
                (t, e) -> {
                    System.out.println(String.format("uncaught %s error %s", t, e));
                }
        );

        System.out.println("Server started: "+serverSocket);
        while (true) {
            handler.handle(serverSocket.accept());
        }
    }
}
