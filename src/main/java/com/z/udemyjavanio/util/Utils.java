package com.z.udemyjavanio.util;

public class Utils {
    public static int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ' : data;
    }
}
