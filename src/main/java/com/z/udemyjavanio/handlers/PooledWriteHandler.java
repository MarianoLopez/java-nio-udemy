package com.z.udemyjavanio.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class PooledWriteHandler implements Handler<SelectionKey> {
    private final ExecutorService pool;
    private final Queue<Runnable> selectorActions;
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public PooledWriteHandler(ExecutorService pool, Queue<Runnable> selectorActions, Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pool = pool;
        this.selectorActions = selectorActions;
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        Queue<ByteBuffer> queue = pendingData.get(socketChannel);

        while (!queue.isEmpty()) {
            ByteBuffer buffer = queue.peek();
            int written = socketChannel.write(buffer);
            if(written == -1) {
                socketChannel.close();;
                System.out.println("Disconnected from "+socketChannel+" (in write)");
                pendingData.remove(socketChannel);
                return;
            }
            if(buffer.hasRemaining()) return;
            queue.remove();
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}
