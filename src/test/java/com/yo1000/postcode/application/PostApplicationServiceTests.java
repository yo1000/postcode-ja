package com.yo1000.postcode.application;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.PostRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.util.CloseableIterator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class PostApplicationServiceTests {
    @Test
    @SuppressWarnings({"unchecked", "resource"})
    void test_update() throws IOException {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doAnswer(_ -> null)
                .when(repos)
                .saveAll(Mockito.anyIterable());

        ZippedCsvFileLoader<PostCsv, Post> loader = (ZippedCsvFileLoader<PostCsv, Post>) Mockito
                .mock(ZippedCsvFileLoader.class);
        Mockito.doReturn((CloseableIterator<Post>) Mockito.mock(CloseableIterator.class))
                .when(loader)
                .load(Mockito.any());

        PostApplicationService service = new PostApplicationService(repos, loader);

        service.update(1000L);

        Mockito.verify(loader, Mockito.times(1)).load(Mockito.any());
        Mockito.verify(repos, Mockito.times(1)).saveAll(Mockito.anyIterable());
    }

    @Test
    @SuppressWarnings({"unchecked", "resource"})
    void test_update_onException() throws IOException {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doAnswer(_ -> null)
                .when(repos)
                .saveAll(Mockito.anyIterable());

        ZippedCsvFileLoader<PostCsv, Post> loader = (ZippedCsvFileLoader<PostCsv, Post>) Mockito
                .mock(ZippedCsvFileLoader.class);
        Mockito.doThrow(new IOException())
                .when(loader)
                .load(Mockito.any());

        PostApplicationService service = new PostApplicationService(repos, loader);

        ThrowableAssert.ThrowingCallable callable = () -> service.update(2);

        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(UncheckedIOException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    @SuppressWarnings({"unchecked"})
    void test_delete() {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doReturn(List.of(1000L, 3000L, 2000L))
                .when(repos)
                .findAllCreationEpochMillis();
        Mockito.doAnswer(_ -> null)
                .when(repos)
                .deleteAllByCreationEpochMillis(Mockito.anyIterable());

        PostApplicationService service = new PostApplicationService(
                repos, (ZippedCsvFileLoader<PostCsv, Post>) Mockito.mock(ZippedCsvFileLoader.class));

        service.delete(2);

        Mockito.verify(repos, Mockito.times(1)).findAllCreationEpochMillis();

        ArgumentCaptor<Iterable<Long>> captor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(repos, Mockito.times(1)).deleteAllByCreationEpochMillis(captor.capture());
        Assertions.assertThat(captor.getValue()).containsExactly(1000L);
    }

    @Test
    @SuppressWarnings({"unchecked"})
    void test_delete_none() {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doReturn(List.of(1000L, 3000L, 2000L))
                .when(repos)
                .findAllCreationEpochMillis();
        Mockito.doAnswer(_ -> null)
                .when(repos)
                .deleteAllByCreationEpochMillis(Mockito.anyIterable());

        PostApplicationService service = new PostApplicationService(
                repos, (ZippedCsvFileLoader<PostCsv, Post>) Mockito.mock(ZippedCsvFileLoader.class));

        service.delete(3);

        Mockito.verify(repos, Mockito.times(1)).findAllCreationEpochMillis();

        Mockito.verify(repos, Mockito.times(0)).deleteAllByCreationEpochMillis(Mockito.any());
    }
}
