package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.PrintingHandler;
import com.z.udemyjavanio.handlers.ThreadedHandler;
import com.z.udemyjavanio.handlers.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiThreadBlockingServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        var serverSocket = new ServerSocket(port);
        var handler = new ThreadedHandler<>(new PrintingHandler<>(new TransmogrifyHandler()));

        while (true) {
            handler.handle(serverSocket.accept());
        }
    }
}
