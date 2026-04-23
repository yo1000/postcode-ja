package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.domain.Node;
import com.yo1000.postcode.domain.NodeRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcNodeRepository implements NodeRepository {
    private final JdbcClient jdbcClient;

    public JdbcNodeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Node> findById(UUID id) {
        return jdbcClient
                .sql("""
                    SELECT
                        id,
                        rank,
                        update_epoch_millis
                    FROM
                        task_nodes
                    WHERE
                        id = :id
                    """)
                .param("id", id)
                .query(Node.class)
                .optional();
    }

    @Override
    public List<Node> findAll() {
        return jdbcClient
                .sql("""
                    SELECT
                        id,
                        rank,
                        update_epoch_millis
                    FROM
                        task_nodes
                    """)
                .query(Node.class)
                .list();
    }

    @Override
    public Node save(Node node) {
        findById(node.id()).ifPresentOrElse(
                n -> jdbcClient
                        .sql("""
                            UPDATE task_nodes
                            SET
                                rank = :rank,
                                update_epoch_millis = :updateEpochMillis
                            WHERE
                                id = :id
                            """)
                        .param("id", n.id())
                        .param("rank", node.rank())
                        .param("updateEpochMillis", node.updateEpochMillis())
                        .update(),
                () -> jdbcClient
                        .sql("""
                            INSERT INTO task_nodes(
                                id,
                                rank,
                                update_epoch_millis
                            ) VALUES(
                                :id,
                                :rank,
                                :updateEpochMillis
                            )
                            """)
                        .param("id", node.id())
                        .param("rank", node.rank())
                        .param("updateEpochMillis", node.updateEpochMillis())
                        .update()
        );

        return node;
    }

    @Override
    public void deleteByUpdateEpochMillisBefore(long updateEpochMillis) {
        jdbcClient
                .sql("""
                    DELETE FROM
                        task_nodes
                    WHERE
                        update_epoch_millis < :updateEpochMillis
                    """)
                .param("updateEpochMillis", updateEpochMillis)
                .update();
    }
}
