package com.z.udemyjavanio.server;

import com.z.udemyjavanio.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

public class SelectorWithWorkPoolNonBlockingServer {

    public static void main(String[] args) throws IOException {
        int threadSize = 10;
        int port = 8080;
        var serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ExecutorService pool = Executors.newFixedThreadPool(threadSize);
        Map<SocketChannel, Queue<ByteBuffer>> pendingData = new ConcurrentHashMap<>();
        Queue<Runnable> selectorActions = new ConcurrentLinkedQueue<>();

        var acceptHandler = new PooledAcceptHandler(pool, selectorActions, pendingData);
        var readHandler = new PooledReadHandler(pool, selectorActions, pendingData);
        var writeHandler = new PooledWriteHandler(pool, selectorActions, pendingData);

        System.out.println("Server started: "+serverSocketChannel);
        while (true) {
            selector.select();
            processSelectorActions(selectorActions);
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

    private static void processSelectorActions(Queue<Runnable> selectorActions) {
        Runnable action;
        while ((action = selectorActions.poll()) != null) {
            action.run();
        }
    }
}
