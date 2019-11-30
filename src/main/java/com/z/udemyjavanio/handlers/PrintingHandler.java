package com.z.udemyjavanio.handlers;

import java.io.IOException;

public class PrintingHandler<S> implements Handler<S> {
    private final Handler<S> other;

    public PrintingHandler(Handler<S> other) {
        this.other = other;
    }

    public void handle(S s) throws IOException {
        System.out.println("Connected to "+s);
        try {
            other.handle(s);
        } finally {
            System.out.println("Disconnected from "+s);
        }
    }
}
