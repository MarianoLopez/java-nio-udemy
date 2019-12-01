package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.AcceptHandler;
import com.z.udemyjavanio.handlers.ReadHandler;
import com.z.udemyjavanio.handlers.TransmogrifyChannelHandler;
import com.z.udemyjavanio.handlers.WriteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class SingleThreadedSelectorNonBlockingServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        var serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>();
        var acceptHandler = new AcceptHandler(pendingData);
        var readHandler = new ReadHandler(pendingData);
        var writeHandler = new WriteHandler(pendingData);

        System.out.println("Server started: "+serverSocketChannel);
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for(Iterator<SelectionKey> it = keys.iterator(); it.hasNext();) {
                SelectionKey key = it.next();
                it.remove();;
                if(key.isValid()) {
                    if(key.isAcceptable()) {
                        acceptHandler.handle(key);
                    }else if(key.isReadable()) {
                        readHandler.handle(key);
                    }else if(key.isWritable()) {
                        writeHandler.handle(key);
                    }
                }
            }
        }
    }
}
