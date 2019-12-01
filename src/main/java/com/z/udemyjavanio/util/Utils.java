package com.z.udemyjavanio.util;

import java.nio.ByteBuffer;

public class Utils {
    public static int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ' : data;
    }

    public static void transmogrify(ByteBuffer buffer) {
        System.out.println("Transmogrification done by "+Thread.currentThread().getName());
        //pos= 0, limit =80, capacity = 80
        //"hello!\n" pos=6, limit =80, capacity = 80
        buffer.flip();
        //"hello!\n" pos=0, limit = 6, capacity =80
        for (int i = 0; i < buffer.limit(); i++) {
            buffer.put(i, (byte) transmogrify(buffer.get(i)));
        }
    }
}
