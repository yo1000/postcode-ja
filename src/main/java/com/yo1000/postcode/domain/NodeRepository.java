package com.yo1000.postcode.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NodeRepository {
    Optional<Node> findById(UUID id);
    List<Node> findAll();
    Node save(Node node);
    void deleteByUpdateEpochMillisBefore(long updateEpochMillis);
}
