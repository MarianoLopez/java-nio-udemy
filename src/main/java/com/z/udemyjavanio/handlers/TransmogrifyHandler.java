package com.z.udemyjavanio.handlers;

import java.io.IOException;
import java.net.Socket;

import static com.z.udemyjavanio.util.Utils.transmogrify;

public class TransmogrifyHandler implements Handler<Socket> {
    @Override
    public void handle(Socket socket) throws IOException {
        try(socket;
            var inputStream = socket.getInputStream();
            var outputStream = socket.getOutputStream()) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(transmogrify(data));
            }
        }
    }
}
