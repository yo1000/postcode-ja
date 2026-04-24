package com.yo1000.postcode.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository {
    List<Post> findByPostcode5(String postcode5);
    List<Post> findByPostcode5(String postcode5, long creationEpochMillis);
    List<Post> findByPostcode7(String postcode7);
    List<Post> findByPostcode7(String postcode7, long creationEpochMillis);
    Page<Post> findByCriteria(Post criteria, Pageable pageable);
    List<Long> findAllCreationEpochMillis();
    List<Post> saveAll(Iterable<Post> entities);
    void deleteAllByCreationEpochMillis(Iterable<Long> creationEpochMillis);
}
