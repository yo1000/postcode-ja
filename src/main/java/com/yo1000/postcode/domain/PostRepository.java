package com.yo1000.postcode.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository {
    Page<Post> findByPostcode5(String postcode5, Pageable pageable);
    Page<Post> findByPostcode5(String postcode5, long creationEpochMillis, Pageable pageable);
    Page<Post> findByPostcode7(String postcode7, Pageable pageable);
    Page<Post> findByPostcode7(String postcode7, long creationEpochMillis, Pageable pageable);
    Page<Post> findByCriteria(Post criteria, Pageable pageable);
    List<Long> findAllCreationEpochMillis();
    List<Post> saveAll(Iterable<Post> entities);
    void deleteAllByCreationEpochMillis(Iterable<Long> creationEpochMillis);
}
