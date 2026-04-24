package com.yo1000.postcode.application;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.PostRepository;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CloseableIterator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class PostApplicationServiceTests {
    @Test
    @SuppressWarnings("unchecked")
    void test_listByPostcode7() {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doReturn(List.of(new Post(
                        "01101",
                        "060  ",
                        "0600000",
                        "北海道",
                        "ホッカイドウ",
                        "札幌市中央区",
                        "サッポロシチュウオウク",
                        "以下に掲載がない場合",
                        "イカニケイサイガナイバアイ",
                        false,
                        false,
                        false,
                        false,
                        false,
                        ChangeReasons.NO_CHANGE,
                        1000L)))
                .when(repos)
                .findByPostcode7(Mockito.anyString());

        ZippedCsvFileLoader<PostCsv, Post> loader = (ZippedCsvFileLoader<PostCsv, Post>) Mockito
                .mock(ZippedCsvFileLoader.class);

        PostApplicationService service = new PostApplicationService(repos, loader);

        List<Post> results = service.listByPostcode7("0600000");

        Assertions.assertThat(results).containsExactly(new Post(
                        "01101",
                        "060  ",
                        "0600000",
                        "北海道",
                        "ホッカイドウ",
                        "札幌市中央区",
                        "サッポロシチュウオウク",
                        "以下に掲載がない場合",
                        "イカニケイサイガナイバアイ",
                        false,
                        false,
                        false,
                        false,
                        false,
                        ChangeReasons.NO_CHANGE,
                        1000L));

        Mockito.verify(repos, Mockito.times(1))
                .findByPostcode7(Mockito.eq("0600000"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_listByPostcode5() {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doReturn(List.of(new Post(
                        "01101",
                        "060  ",
                        "0600000",
                        "北海道",
                        "ホッカイドウ",
                        "札幌市中央区",
                        "サッポロシチュウオウク",
                        "以下に掲載がない場合",
                        "イカニケイサイガナイバアイ",
                        false,
                        false,
                        false,
                        false,
                        false,
                        ChangeReasons.NO_CHANGE,
                        1000L)))
                .when(repos)
                .findByPostcode5(Mockito.anyString());

        ZippedCsvFileLoader<PostCsv, Post> loader = (ZippedCsvFileLoader<PostCsv, Post>) Mockito
                .mock(ZippedCsvFileLoader.class);

        PostApplicationService service = new PostApplicationService(repos, loader);

        List<Post> results = service.listByPostcode5("060  ");

        Assertions.assertThat(results).containsExactly(new Post(
                "01101",
                "060  ",
                "0600000",
                "北海道",
                "ホッカイドウ",
                "札幌市中央区",
                "サッポロシチュウオウク",
                "以下に掲載がない場合",
                "イカニケイサイガナイバアイ",
                false,
                false,
                false,
                false,
                false,
                ChangeReasons.NO_CHANGE,
                1000L));

        Mockito.verify(repos, Mockito.times(1))
                .findByPostcode5(Mockito.eq("060  "));
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_pageByCriteria() {
        PostRepository repos = Mockito.mock(PostRepository.class);
        Mockito.doReturn(List.of(1000L, 3000L, 2000L))
                .when(repos)
                .findAllCreationEpochMillis();
        Mockito.doReturn(new PageImpl<>(
                List.of(new Post(
                        "01101",
                        "060  ",
                        "0600000",
                        "北海道",
                        "ホッカイドウ",
                        "札幌市中央区",
                        "サッポロシチュウオウク",
                        "以下に掲載がない場合",
                        "イカニケイサイガナイバアイ",
                        false,
                        false,
                        false,
                        false,
                        false,
                        ChangeReasons.NO_CHANGE,
                        3000L)),
                        PageRequest.of(0, 10),
                        1L))
                .when(repos)
                .findByCriteria(Mockito.any(Post.class), Mockito.any(Pageable.class));

        ZippedCsvFileLoader<PostCsv, Post> loader = (ZippedCsvFileLoader<PostCsv, Post>) Mockito
                .mock(ZippedCsvFileLoader.class);

        PostApplicationService service = new PostApplicationService(repos, loader);

        Page<Post> results = service.pageByCriteria(new Post(
                "01101",
                "060  ",
                "0600000",
                "北海道",
                "ホッカイドウ",
                "札幌市中央区",
                "サッポロシチュウオウク",
                "以下に掲載がない場合",
                "イカニケイサイガナイバアイ",
                false,
                false,
                false,
                false,
                false,
                ChangeReasons.NO_CHANGE,
                0L),
                PageRequest.of(0, 10));

        Assertions.assertThat(results.getContent()).containsExactly(new Post(
                "01101",
                "060  ",
                "0600000",
                "北海道",
                "ホッカイドウ",
                "札幌市中央区",
                "サッポロシチュウオウク",
                "以下に掲載がない場合",
                "イカニケイサイガナイバアイ",
                false,
                false,
                false,
                false,
                false,
                ChangeReasons.NO_CHANGE,
                3000L));
        Assertions.assertThat(results.getTotalElements()).isEqualTo(1L);
        Assertions.assertThat(results.getSize()).isEqualTo(10);

        Mockito.verify(repos, Mockito.times(1))
                .findAllCreationEpochMillis();
        Mockito.verify(repos, Mockito.times(1))
                .findByCriteria(
                        Mockito.eq(new Post(
                                "01101",
                                "060  ",
                                "0600000",
                                "北海道",
                                "ホッカイドウ",
                                "札幌市中央区",
                                "サッポロシチュウオウク",
                                "以下に掲載がない場合",
                                "イカニケイサイガナイバアイ",
                                false,
                                false,
                                false,
                                false,
                                false,
                                ChangeReasons.NO_CHANGE,
                                3000L)),
                        Mockito.any(Pageable.class));
    }

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
