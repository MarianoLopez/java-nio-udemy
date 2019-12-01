package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.TransmogrifyChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class SingleThreadedPollingNonBlockingServer {

    public static void main(String[] args) throws IOException {
        int threadSize = 10;
        int port = 8080;
        var serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);

        var handler = new TransmogrifyChannelHandler();
        List<SocketChannel> socketChannels = new ArrayList<>();

        System.out.println("Server started: "+serverSocket);
        while (true) {
            var socket = serverSocket.accept();
            if(socket !=null) {
                socketChannels.add(socket);
                socket.configureBlocking(false);
                System.out.println("Connected: "+socket);
            }

            for (SocketChannel socketChannel : socketChannels) {
                if (socketChannel.isConnected()) {
                    handler.handle(socketChannel);
                }
            }
            socketChannels.removeIf(s->!s.isConnected());
        }
    }
}
