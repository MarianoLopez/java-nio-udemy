package com.z.udemyjavanio.handlers;

import com.z.udemyjavanio.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

public class ReadHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int read = socketChannel.read(buffer);
        if (read == -1) {
            pendingData.remove(socketChannel);
            socketChannel.close();
            System.out.println("Disconnected from "+socketChannel+" (in read)");
            return;
        }
        if(read > 0) {
            Utils.transmogrify(buffer);
            pendingData.get(socketChannel).add(buffer);
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
