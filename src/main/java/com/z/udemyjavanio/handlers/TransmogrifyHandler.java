package com.z.udemyjavanio.handlers;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import static com.z.udemyjavanio.util.Utils.transmogrify;

public class TransmogrifyHandler implements Handler<Socket> {
    @Override
    public void handle(Socket socket) throws IOException {
        try(socket;
            var inputStream = socket.getInputStream();
            var outputStream = socket.getOutputStream()) {
            int data;
            while ((data = inputStream.read()) != -1) {
                if(data == '%') throw new IOException("Oopsie");
                outputStream.write(transmogrify(data));
            }
        }
    }
}
