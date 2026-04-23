package com.yo1000.postcode.domain;

import java.util.UUID;

public record Node(
        UUID id,
        int rank,
        long updateEpochMillis
) {
    public static final Node INIT_CHECK = new Node(UUID.nameUUIDFromBytes(new byte[] {0}), Integer.MAX_VALUE, Long.MAX_VALUE);

    public Node update(int newOrder) {
        return update(newOrder, updateEpochMillis());
    }

    public Node update(int newOrder, long newUpdateEpochMillis) {
        return new Node(id(), newOrder, newUpdateEpochMillis);
    }
}
