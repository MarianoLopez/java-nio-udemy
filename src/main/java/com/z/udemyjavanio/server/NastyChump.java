package com.z.udemyjavanio.server;

import java.io.IOException;
import java.net.Socket;

public class NastyChump {
    public static void main(String[] args) throws InterruptedException {
        int socketsSize = 3000;
        String host = "localhost";
        int port = 8080;
        Socket[] sockets = new Socket[socketsSize];

        for (int i = 0; i < sockets.length; i++) {
            try {
                sockets[i] = new Socket(host, port);
                System.out.println("new socket: "+sockets[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Thread.sleep(1_000);
        }
    }
}
