package com.yo1000.postcode.application;

import com.yo1000.postcode.config.TimeProperties;
import com.yo1000.postcode.domain.Node;
import com.yo1000.postcode.domain.NodeRepository;
import com.yo1000.postcode.domain.vo.NodeIdHolder;
import com.yo1000.postcode.domain.vo.WaitTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NodeApplicationServiceTests {
    @Test
    void test_exists() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();

        UUID uuid = UUID.fromString("f0000001-0000-7000-8000-0123456789ab");
        NodeIdHolder idHolder = new NodeIdHolder(uuid);

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Mockito.doReturn(Optional.of(new Node(uuid, 0, 1000L)))
                .when(repos)
                .findById(Mockito.eq(uuid));

        boolean result = service.exists();

        Assertions.assertThat(result).isTrue();

        Mockito.verify(repos, Mockito.times(1)).findById(Mockito.eq(uuid));
    }

    @Test
    void test_exists_false() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();

        UUID uuid = UUID.fromString("f0000001-0000-7000-8000-0123456789ab");
        NodeIdHolder idHolder = new NodeIdHolder(uuid);

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Mockito.doReturn(Optional.empty())
                .when(repos)
                .findById(Mockito.eq(uuid));

        boolean result = service.exists();

        Assertions.assertThat(result).isFalse();

        Mockito.verify(repos, Mockito.times(1)).findById(Mockito.eq(uuid));
    }

    @Test
    void test_init() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();
        timeProps.setWaitUnitTimeMillis(300000L);

        NodeIdHolder idHolder = new NodeIdHolder(UUID.randomUUID());

        Mockito.doReturn(Optional.of(Node.INIT_CHECK))
                .when(repos)
                .findById(Mockito.eq(Node.INIT_CHECK.id()));

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Optional<WaitTime> result = service.init();

        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().millis()).isEqualTo(timeProps.getWaitUnitTimeMillis());

        Mockito.verify(repos, Mockito.times(1))
                .findById(Mockito.eq(Node.INIT_CHECK.id()));
    }

    @Test
    void test_init_first() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();
        timeProps.setWaitUnitTimeMillis(300000L);

        NodeIdHolder idHolder = new NodeIdHolder(UUID.randomUUID());

        Mockito.doReturn(Optional.empty())
                .when(repos)
                .findById(Mockito.eq(Node.INIT_CHECK.id()));

        Mockito.doReturn(Node.INIT_CHECK)
                .when(repos)
                .save(Mockito.eq(Node.INIT_CHECK));

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Optional<WaitTime> result = service.init();

        Assertions.assertThat(result.isEmpty()).isTrue();

        Mockito.verify(repos, Mockito.times(1))
                .findById(Mockito.eq(Node.INIT_CHECK.id()));
        Mockito.verify(repos, Mockito.times(1))
                .save(Mockito.eq(Node.INIT_CHECK));
    }

    @Test
    void test_init_duplicated() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();
        timeProps.setWaitUnitTimeMillis(300000L);

        NodeIdHolder idHolder = new NodeIdHolder(UUID.randomUUID());

        Mockito.doReturn(Optional.empty())
                .when(repos)
                .findById(Mockito.eq(Node.INIT_CHECK.id()));

        Mockito.doThrow(new DuplicateKeyException("test"))
                .when(repos)
                .save(Mockito.eq(Node.INIT_CHECK));

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Optional<WaitTime> result = service.init();

        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().millis()).isEqualTo(timeProps.getWaitUnitTimeMillis());

        Mockito.verify(repos, Mockito.times(1))
                .findById(Mockito.eq(Node.INIT_CHECK.id()));
        Mockito.verify(repos, Mockito.times(1))
                .save(Mockito.eq(Node.INIT_CHECK));
    }

    @Test
    void test_register() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();

        NodeIdHolder idHolder = new NodeIdHolder(UUID.randomUUID());

        Mockito.doReturn(new Node(idHolder.value(), -1, 9000L))
                .when(repos)
                .save(Mockito.any(Node.class));

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        service.register(9000L);

        Mockito.verify(repos, Mockito.times(1))
                .save(Mockito.eq(new Node(idHolder.value(), -1, 9000L)));
    }

    @Test
    void test_rank() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();
        timeProps.setWaitUnitTimeMillis(300000L);

        UUID uuid1 = UUID.fromString("f0000001-0000-7000-8000-0123456789ab");
        UUID uuid2 = UUID.fromString("f0000002-0000-7000-8000-0123456789ab");
        NodeIdHolder idHolder = new NodeIdHolder(uuid1);

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Mockito.doReturn(List.of(
                        new Node(uuid1, 1, 1000L),
                        Node.INIT_CHECK,
                        new Node(uuid2, 0, 2000L)))
                .when(repos)
                .findAll();

        Mockito.doReturn(Mockito.mock(Node.class))
                .when(repos)
                .save(Mockito.any(Node.class));

        Mockito.doReturn(Optional.of(new Node(uuid1, 0, 1000L)))
                .when(repos)
                .findById(Mockito.eq(uuid1));

        Optional<WaitTime> result = service.rank(9000L);

        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().millis()).isEqualTo(0L);

        ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);

        Mockito.verify(repos, Mockito.times(2))
                .save(nodeCaptor.capture());

        List<Node> args = nodeCaptor.getAllValues();
        Assertions.assertThat(args.get(0).id()).isEqualTo(uuid1);
        Assertions.assertThat(args.get(0).rank()).isEqualTo(0L);
        Assertions.assertThat(args.get(0).updateEpochMillis()).isEqualTo(9000L);
        Assertions.assertThat(args.get(1).id()).isEqualTo(uuid2);
        Assertions.assertThat(args.get(1).rank()).isEqualTo(1L);
        Assertions.assertThat(args.get(1).updateEpochMillis()).isEqualTo(2000L);
    }

    @Test
    void test_cleanup() {
        NodeRepository repos = Mockito.mock(NodeRepository.class);

        TimeProperties timeProps = new TimeProperties();
        timeProps.setNodeKeepAliveMillis(300_000L);

        NodeIdHolder idHolder = new NodeIdHolder(UUID.randomUUID());

        NodeApplicationService service = new NodeApplicationService(repos, timeProps, idHolder);

        Mockito.doAnswer(_ -> null)
                .when(repos)
                .deleteByUpdateEpochMillisBefore(Mockito.anyLong());

        service.cleanup(500_000L);

        ArgumentCaptor<Long> nodeCaptor = ArgumentCaptor.forClass(Long.class);

        Mockito.verify(repos, Mockito.times(1))
                .deleteByUpdateEpochMillisBefore(nodeCaptor.capture());

        Long args = nodeCaptor.getValue();
        Assertions.assertThat(args).isEqualTo(200_000L);
    }
}
