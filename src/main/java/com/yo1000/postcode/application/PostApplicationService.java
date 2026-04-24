package com.yo1000.postcode.application;

import com.yo1000.postcode.application.port.PostCsv;
import com.yo1000.postcode.application.port.ZippedCsvFileLoader;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class PostApplicationService {
    private final PostRepository postRepos;
    private final ZippedCsvFileLoader<PostCsv, Post> loader;

    public PostApplicationService(PostRepository postRepos, ZippedCsvFileLoader<PostCsv, Post> loader) {
        this.postRepos = postRepos;
        this.loader = loader;
    }

    public Page<Post> pageByPostcode7(String postcode7, Pageable pageable) {
        return postRepos.findByPostcode7(postcode7, pageable);
    }

    public Page<Post> pageByPostcode5(String postcode5, Pageable pageable) {
        return postRepos.findByPostcode5(postcode5, pageable);
    }

    public Page<Post> pageByCriteria(Post criteria, Pageable pageable) {
        long creationEpochMillis = postRepos.findAllCreationEpochMillis().stream()
                .mapToLong(v -> v)
                .max()
                .orElseThrow();

        return postRepos.findByCriteria(
                criteria.updateCreationEpochMillis(creationEpochMillis),
                pageable);
    }

    public void update(long execTimeInMillis) {
        try (CloseableIterator<Post> iter = loader.load(row -> row.toPost(execTimeInMillis))) {
            postRepos.saveAll(() -> iter);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void delete(int keepGenerations) {
        Iterable<Long> creationEpochMillis = postRepos.findAllCreationEpochMillis();
        if (StreamSupport.stream(creationEpochMillis.spliterator(), false).count() > keepGenerations) {
            List<Long> keepGenerationEpochMillis = StreamSupport.stream(creationEpochMillis.spliterator(), false)
                    .sorted((o1, o2) -> Long.compare(o2, o1))
                    .limit(keepGenerations)
                    .toList();

            postRepos.deleteAllByCreationEpochMillis(
                    StreamSupport.stream(creationEpochMillis.spliterator(), false)
                            .filter(millis -> !keepGenerationEpochMillis.contains(millis))
                            .toList());
        }
    }
}
