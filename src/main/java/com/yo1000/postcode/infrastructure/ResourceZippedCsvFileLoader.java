package com.yo1000.postcode.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.config.AppProperties;
import com.yo1000.postcode.domain.Post;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.zip.ZipInputStream;

@Repository
public class ResourceZippedCsvFileLoader implements ZippedCsvFileLoader<PostCsv, Post> {
    private final AppProperties appProps;

    public ResourceZippedCsvFileLoader(AppProperties appProps) {
        this.appProps = appProps;
    }

    @Override
    public CloseableIterator<Post> load(RowHandler<PostCsv, Post> handler) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(appProps.getResource().getInputStream());
        zipIn.getNextEntry();

        InputStreamReader inReader = new InputStreamReader(zipIn, StandardCharsets.UTF_8);
        BufferedReader bufReader = new BufferedReader(inReader);

        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<PostCsv> iter = csvMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readerFor(PostCsv.class)
                .with(PostCsv.SCHEMA)
                .readValues(bufReader);

        return new RowHandlingIterator<>(
                handler, iter, zipIn, inReader, bufReader);
    }

    static class RowHandlingIterator<T, R> implements CloseableIterator<R> {
        private final RowHandler<T, R> rowHandler;
        private final MappingIterator<T> sourceIterator;
        private final Stack<Closeable> closeableStack;

        public RowHandlingIterator(
                RowHandler<T, R> rowHandler,
                MappingIterator<T> sourceIterator,
                Closeable... closeables) {
            this.rowHandler = rowHandler;
            this.sourceIterator = sourceIterator;
            this.closeableStack = new Stack<>();

            for (Closeable closeable : closeables) {
                closeableStack.push(closeable);
            }
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
                sourceIterator.close();
                while (!closeableStack.isEmpty()) {
                    closeableStack.pop().close();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
