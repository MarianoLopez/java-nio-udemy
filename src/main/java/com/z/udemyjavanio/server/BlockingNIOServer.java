package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.BlockingChannelHandler;
import com.z.udemyjavanio.handlers.ExecutorServiceHandler;
import com.z.udemyjavanio.handlers.PrintingHandler;
import com.z.udemyjavanio.handlers.TransmogrifyChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executors;

public class BlockingNIOServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        var serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        var handler = new ExecutorServiceHandler<>(
            new PrintingHandler<>(
                new BlockingChannelHandler(
                    new TransmogrifyChannelHandler()
                )
            ),
            Executors.newCachedThreadPool()
        );

        System.out.println("Server started: "+serverSocket);
        while (true) {
            handler.handle(serverSocket.accept());
        }
    }
}
