package com.z.udemyjavanio.handlers;

import com.z.udemyjavanio.util.Utils;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.z.udemyjavanio.util.Utils.transmogrify;

public class TransmogrifyChannelHandler implements Handler<SocketChannel> {

    @Override
    public void handle(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(80);
        int read = socketChannel.read(buffer);
        if (read == -1) {
            socketChannel.close();
            return;
        }
        if (read > 0) {
            Utils.transmogrify(buffer);
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        }
    }
}
