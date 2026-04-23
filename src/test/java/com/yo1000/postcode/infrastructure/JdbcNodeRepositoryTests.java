package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.domain.Node;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.*;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Testcontainers
public class JdbcNodeRepositoryTests {
    @Container
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres");

    @BeforeAll
    static void startContainers() {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Required to apply `schema.sql`.
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    JdbcClient jdbcClient;

    @Test
    @Sql(statements = {
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000001-0000-7000-8000-0123456789ab', 0, 1776928458540);
            """
    })
    void test_findById() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Optional<Node> r = repo.findById(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"));

        r.ifPresentOrElse(entity -> {
            Assertions.assertThat(entity.id()).isEqualTo(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"));
            Assertions.assertThat(entity.rank()).isEqualTo(0);
            Assertions.assertThat(entity.updateEpochMillis()).isEqualTo(1776928458540L);
        }, () -> {
            Assertions.fail("Entity is empty");
        });
    }

    @Test
    void test_findById_empty() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Optional<Node> r = repo.findById(UUID.fromString("f0000000-0000-7000-8000-0123456789ab"));

        Assertions.assertThat(r.isEmpty()).isTrue();
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000001-0000-7000-8000-0123456789ab', 0, 1776928458540);
            """,
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000002-0000-7000-8000-0123456789ab', 1, 1776928458541);
            """,
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000003-0000-7000-8000-0123456789ab', 2, 1776928458542);
            """
    })
    void test_findAll() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Iterable<Node> r = repo.findAll();

        List<Node> entities = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                r.iterator(), Spliterator.ORDERED), false).toList();

        Assertions.assertThat(entities.size()).isEqualTo(3);
        Assertions.assertThat(entities.get(0)).isEqualTo(new Node(
                UUID.fromString("f0000001-0000-7000-8000-0123456789ab"), 0, 1776928458540L));
        Assertions.assertThat(entities.get(1)).isEqualTo(new Node(
                UUID.fromString("f0000002-0000-7000-8000-0123456789ab"), 1, 1776928458541L));
        Assertions.assertThat(entities.get(2)).isEqualTo(new Node(
                UUID.fromString("f0000003-0000-7000-8000-0123456789ab"), 2, 1776928458542L));
    }

    @Test
    void test_findAll_empty() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Iterable<Node> r = repo.findAll();

        List<Node> entities = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                r.iterator(), Spliterator.ORDERED), false).toList();

        Assertions.assertThat(entities.size()).isEqualTo(0);
    }

    @Test
    void test_save() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Node saved = repo.save(new Node(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"), 0, 1776928458540L));

        Assertions.assertThat(saved.id()).isEqualTo(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"));
        Assertions.assertThat(saved.rank()).isEqualTo(0);
        Assertions.assertThat(saved.updateEpochMillis()).isEqualTo(1776928458540L);

        List<Node> res = jdbcClient
                .sql(
                        """
                        SELECT
                            id, rank, update_epoch_millis
                        FROM
                            task_nodes
                        WHERE
                            id = :id
                        """)
                .param("id", UUID.fromString("f0000001-0000-7000-8000-0123456789ab"))
                .query(Node.class)
                .list();

        Assertions.assertThat(res.size()).isEqualTo(1);
        Assertions.assertThat(res.getFirst()).isEqualTo(saved);
    }


    @Test
    @Sql(statements = {
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000001-0000-7000-8000-0123456789ab', 0, 1776928458540);
            """
    })
    void test_save_update() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        Node saved = repo.save(new Node(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"), 1, 1776928458541L));

        Assertions.assertThat(saved.id()).isEqualTo(UUID.fromString("f0000001-0000-7000-8000-0123456789ab"));
        Assertions.assertThat(saved.rank()).isEqualTo(1);
        Assertions.assertThat(saved.updateEpochMillis()).isEqualTo(1776928458541L);

        List<Node> res = jdbcClient
                .sql(
                        """
                        SELECT
                            id, rank, update_epoch_millis
                        FROM
                            task_nodes
                        WHERE
                            id = :id
                        """)
                .param("id", UUID.fromString("f0000001-0000-7000-8000-0123456789ab"))
                .query(Node.class)
                .list();

        Assertions.assertThat(res.size()).isEqualTo(1);
        Assertions.assertThat(res.getFirst()).isEqualTo(saved);
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000001-0000-7000-8000-0123456789ab', 0, 1776928458540);
            """,
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000002-0000-7000-8000-0123456789ab', 1, 1776928458541);
            """,
            """
            INSERT INTO task_nodes (id, rank, update_epoch_millis)
            VALUES ('f0000003-0000-7000-8000-0123456789ab', 2, 1776928458542);
            """
    })
    void test_deleteByUpdateEpochMillisBefore() {
        JdbcNodeRepository repo = new JdbcNodeRepository(jdbcClient);
        repo.deleteByUpdateEpochMillisBefore(1776928458542L);

        List<Node> res = jdbcClient
                .sql(
                        """
                        SELECT
                            id, rank, update_epoch_millis
                        FROM
                            task_nodes
                        """)
                .query(Node.class)
                .list();

        Assertions.assertThat(res.size()).isEqualTo(1);
        Assertions.assertThat(res.getFirst().id()).isEqualTo(UUID.fromString("f0000003-0000-7000-8000-0123456789ab"));
        Assertions.assertThat(res.getFirst().rank()).isEqualTo(2);
        Assertions.assertThat(res.getFirst().updateEpochMillis()).isEqualTo(1776928458542L);
    }
}
