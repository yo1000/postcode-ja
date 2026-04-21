package com.yo1000.postcode.domain;

import java.util.List;

public interface PostRepository {
    List<Post> findByPostcode5(String postcode5);
    List<Post> findByPostcode5(String postcode5, long creationEpochMillis);
    List<Post> findByPostcode7(String postcode7);
    List<Post> findByPostcode7(String postcode7, long creationEpochMillis);
    List<Post> findByCriteria(Post criteria);
    List<Long> findAllCreationEpochMillis();
    List<Post> saveAll(Iterable<Post> entities);
}
