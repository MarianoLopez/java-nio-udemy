package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.PrintingHandler;
import com.z.udemyjavanio.handlers.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class SingleThreadBlockingServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        var serverSocket = new ServerSocket(port);
        var handler = new PrintingHandler<>(new TransmogrifyHandler());

        while (true) {
            handler.handle(serverSocket.accept());
        }
    }
}
