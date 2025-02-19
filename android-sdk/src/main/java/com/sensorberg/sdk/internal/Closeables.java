package com.sensorberg.sdk.internal;

import java.io.Closeable;
import java.io.IOException;

class Closeables {
    @SuppressWarnings("EmptyCatchBlock")
    public static void close(Closeable closeable) {
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {

            }
        }
    }
}
