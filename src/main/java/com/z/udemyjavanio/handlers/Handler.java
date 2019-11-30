package com.z.udemyjavanio.handlers;

import java.io.IOException;
//Decorator component
public interface Handler<S> {
    void handle(S s) throws IOException;
}
