package com.yo1000.postcode.infrastructure;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.config.CsvProperties;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.zip.ZipInputStream;

@Repository
public class ResourceZippedCsvFileLoader<T, R> implements ZippedCsvFileLoader<T, R> {
    private final CsvProperties csvProps;
    private final ObjectMapper csvMapper;

    public ResourceZippedCsvFileLoader(CsvProperties csvProps, ObjectMapper csvMapper) {
        this.csvProps = csvProps;
        this.csvMapper = csvMapper;
    }

    @Override
    public CloseableIterator<R> load(RowHandler<T, R> handler) throws IOException {
        CloseableStack closeableStack = new CloseableStack();

        InputStream resourceIn = closeableStack.pushCloseable(csvProps.getResource().getInputStream());
        ZipInputStream zipIn = closeableStack.pushCloseable(new ZipInputStream(resourceIn));
        zipIn.getNextEntry();

        InputStreamReader inReader = closeableStack.pushCloseable(new InputStreamReader(zipIn, StandardCharsets.UTF_8));
        BufferedReader bufReader = closeableStack.pushCloseable(new BufferedReader(inReader));

        MappingIterator<T> iter = closeableStack.pushCloseable(csvMapper
                .readerFor(PostCsv.class)
                .with(PostCsv.SCHEMA)
                .readValues(bufReader));

        return new RowHandlingIterator<>(
                handler, iter, closeableStack);
    }

    static class CloseableStack extends Stack<Closeable> {
        public <T extends Closeable> T pushCloseable(T closeable) {
            push(closeable);
            return closeable;
        }
    }

    static class RowHandlingIterator<T, R> implements CloseableIterator<R> {
        private final RowHandler<T, R> rowHandler;
        private final MappingIterator<T> sourceIterator;
        private final CloseableStack closeableStack;

        public RowHandlingIterator(
                RowHandler<T, R> rowHandler,
                MappingIterator<T> sourceIterator,
                CloseableStack closeableStack) {
            this.rowHandler = rowHandler;
            this.sourceIterator = sourceIterator;
            this.closeableStack = closeableStack;
        }

        @Override
        public boolean hasNext() {
            try {
                return sourceIterator.hasNextValue();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public R next() {
            try {
                return rowHandler.handle(sourceIterator.nextValue());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void close() {
            try {
                while (!closeableStack.isEmpty()) {
                    closeableStack.pop().close();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
