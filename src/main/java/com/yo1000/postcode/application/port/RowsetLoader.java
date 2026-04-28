package com.yo1000.postcode.application.port;

import org.springframework.data.util.CloseableIterator;

import java.io.IOException;

public interface RowsetLoader<T, R> {
    interface RowHandler<T, R> {
        R handle(T row);
    }

    CloseableIterator<R> load(RowHandler<T, R> handler) throws IOException;
}
