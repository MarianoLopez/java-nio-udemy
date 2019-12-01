package com.z.udemyjavanio.handlers;

import java.io.IOException;

public class PrintingHandler<S> extends DecoratedHandler<S> {

    public PrintingHandler(Handler<S> other) {
        super(other);
    }

    public void handle(S s) throws IOException {
        System.out.println("Connected to "+s);
        try {
            super.handle(s);
        } finally {
            System.out.println("Disconnected from "+s);
        }
    }
}
