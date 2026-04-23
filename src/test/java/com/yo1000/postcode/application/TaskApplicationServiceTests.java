package com.yo1000.postcode.application;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.config.NodeProperties;
import com.yo1000.postcode.config.TimeProperties;
import com.yo1000.postcode.domain.NodeRepository;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.PostRepository;
import com.yo1000.postcode.domain.vo.NodeIdHolder;
import com.yo1000.postcode.domain.vo.WaitTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class TaskApplicationServiceTests {
    @Test
    @SuppressWarnings("unchecked")
    void test_updatePosts() {
        NodeIdHolder nodeIdHolder = new NodeIdHolder(UUID.randomUUID());

        PostApplicationService postApp = Mockito.spy(new PostApplicationService(
                Mockito.mock(PostRepository.class),
                (ZippedCsvFileLoader<PostCsv, Post>) Mockito.mock(ZippedCsvFileLoader.class)));
        Mockito.doAnswer(_ -> null)
                .when(postApp)
                .update(Mockito.anyLong());
        Mockito.doAnswer(_ -> null)
                .when(postApp)
                .delete(Mockito.anyInt());

        NodeApplicationService nodeApp = Mockito.spy(new NodeApplicationService(
                Mockito.mock(NodeRepository.class),
                Mockito.mock(TimeProperties.class),
                nodeIdHolder));
        Mockito.doReturn(true)
                .when(nodeApp)
                .exists();
        Mockito.doReturn(Optional.of(new WaitTime(0L)))
                .when(nodeApp)
                .init();
        Mockito.doAnswer(_ -> null)
                .when(nodeApp)
                .register(Mockito.anyLong());
        Mockito.doReturn(Optional.of(new WaitTime(0L)))
                .when(nodeApp)
                .rank(Mockito.anyLong());
        Mockito.doAnswer(_ -> null)
                .when(nodeApp)
                .cleanup(Mockito.anyLong());

        NodeProperties nodeProps = new NodeProperties();
        nodeProps.setGenerations(2);

        Clock clock = Clock.fixed(ZonedDateTime.of(
                        2026, 1, 1, 0, 0, 0, 0,
                        ZoneId.systemDefault())
                .toInstant(), ZoneId.systemDefault());

        TaskApplicationService service = new TaskApplicationService(
                postApp, nodeApp, nodeProps, nodeIdHolder, clock);

        service.updatePosts();

        Mockito.verify(nodeApp, Mockito.times(1)).exists();
        Mockito.verify(nodeApp, Mockito.times(0)).init();
        Mockito.verify(nodeApp, Mockito.times(0)).register(Mockito.anyLong());

        ArgumentCaptor<Long> rankCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(nodeApp, Mockito.times(1)).rank(rankCaptor.capture());
        Assertions.assertThat(rankCaptor.getValue()).isEqualTo(clock.millis());

        ArgumentCaptor<Long> updateCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(postApp, Mockito.times(1)).update(updateCaptor.capture());
        Assertions.assertThat(updateCaptor.getValue()).isEqualTo(clock.millis());

        ArgumentCaptor<Integer> deleteCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(postApp, Mockito.times(1)).delete(deleteCaptor.capture());
        Assertions.assertThat(deleteCaptor.getValue()).isEqualTo(nodeProps.getGenerations());

        ArgumentCaptor<Long> cleanupCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(nodeApp, Mockito.times(1)).cleanup(cleanupCaptor.capture());
        Assertions.assertThat(cleanupCaptor.getValue()).isEqualTo(clock.millis());
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_updatePosts_init() {
        NodeIdHolder nodeIdHolder = new NodeIdHolder(UUID.randomUUID());

        PostApplicationService postApp = Mockito.spy(new PostApplicationService(
                Mockito.mock(PostRepository.class),
                (ZippedCsvFileLoader<PostCsv, Post>) Mockito.mock(ZippedCsvFileLoader.class)));
        Mockito.doAnswer(_ -> null)
                .when(postApp)
                .update(Mockito.anyLong());
        Mockito.doAnswer(_ -> null)
                .when(postApp)
                .delete(Mockito.anyInt());

        NodeApplicationService nodeApp = Mockito.spy(new NodeApplicationService(
                Mockito.mock(NodeRepository.class),
                Mockito.mock(TimeProperties.class),
                nodeIdHolder));
        Mockito.doReturn(false)
                .when(nodeApp)
                .exists();
        Mockito.doReturn(Optional.of(new WaitTime(0L)))
                .when(nodeApp)
                .init();
        Mockito.doAnswer(_ -> null)
                .when(nodeApp)
                .register(Mockito.anyLong());
        Mockito.doReturn(Optional.of(new WaitTime(0L)))
                .when(nodeApp)
                .rank(Mockito.anyLong());
        Mockito.doAnswer(_ -> null)
                .when(nodeApp)
                .cleanup(Mockito.anyLong());

        NodeProperties nodeProps = new NodeProperties();
        nodeProps.setGenerations(2);

        Clock clock = Clock.fixed(ZonedDateTime.of(
                        2026, 1, 1, 0, 0, 0, 0,
                        ZoneId.systemDefault())
                .toInstant(), ZoneId.systemDefault());

        TaskApplicationService service = new TaskApplicationService(
                postApp, nodeApp, nodeProps, nodeIdHolder, clock);

        service.updatePosts();

        Mockito.verify(nodeApp, Mockito.times(1)).exists();
        Mockito.verify(nodeApp, Mockito.times(1)).init();

        ArgumentCaptor<Long> registerCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(nodeApp, Mockito.times(1)).register(registerCaptor.capture());
        Assertions.assertThat(registerCaptor.getValue()).isEqualTo(clock.millis());

        ArgumentCaptor<Long> rankCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(nodeApp, Mockito.times(1)).rank(rankCaptor.capture());
        Assertions.assertThat(rankCaptor.getValue()).isEqualTo(clock.millis());

        ArgumentCaptor<Long> updateCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(postApp, Mockito.times(1)).update(updateCaptor.capture());
        Assertions.assertThat(updateCaptor.getValue()).isEqualTo(clock.millis());

        ArgumentCaptor<Integer> deleteCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(postApp, Mockito.times(1)).delete(deleteCaptor.capture());
        Assertions.assertThat(deleteCaptor.getValue()).isEqualTo(nodeProps.getGenerations());

        ArgumentCaptor<Long> cleanupCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(nodeApp, Mockito.times(1)).cleanup(cleanupCaptor.capture());
        Assertions.assertThat(cleanupCaptor.getValue()).isEqualTo(clock.millis());
    }
}
