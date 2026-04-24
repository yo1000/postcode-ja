package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.PostRepository;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.StreamSupport;

@Repository
public class JdbcPostRepository implements PostRepository {
    private final NamedParameterJdbcOperations namedJdbcOps;

    public JdbcPostRepository(NamedParameterJdbcOperations namedJdbcOps) {
        this.namedJdbcOps = namedJdbcOps;
    }

    @Override
    public Page<Post> findByPostcode5(String postcode5, Pageable pageable) {
        long creationEpochMillis = findAllCreationEpochMillis().stream()
                .mapToLong(v -> v)
                .max()
                .orElseThrow();

        return findByPostcode5(postcode5, creationEpochMillis, pageable);
    }

    @Override
    public Page<Post> findByPostcode5(String postcode5, long creationEpochMillis, Pageable pageable) {
        String criteriaQuery = "postcode5 = :postcode5 AND creation_epoch_millis = :creationEpochMillis";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("postcode5", postcode5)
                .addValue("creationEpochMillis", creationEpochMillis);

        return new PageImpl<>(
                query(criteriaQuery, params, pageable),
                pageable,
                count(criteriaQuery, params));

    }

    @Override
    public Page<Post> findByPostcode7(String postcode7, Pageable pageable) {
        long creationEpochMillis = findAllCreationEpochMillis().stream()
                .mapToLong(v -> v)
                .max()
                .orElseThrow();

        return findByPostcode7(postcode7, creationEpochMillis, pageable);
    }

    @Override
    public Page<Post> findByPostcode7(String postcode7, long creationEpochMillis, Pageable pageable) {
        String criteriaQuery = "postcode7 = :postcode7 AND creation_epoch_millis = :creationEpochMillis";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("postcode7", postcode7)
                .addValue("creationEpochMillis", creationEpochMillis);

        return new PageImpl<>(
                query(criteriaQuery, params, pageable),
                pageable,
                count(criteriaQuery, params));
    }

    @Override
    public Page<Post> findByCriteria(Post criteria, Pageable pageable) {
        StringJoiner joiner = new StringJoiner(" AND ");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (StringUtils.hasText(criteria.localGovCode())) {
            joiner.add("local_gov_code = :localGovCode");
            params.addValue("localGovCode", criteria.localGovCode());
        }

        if (StringUtils.hasText(criteria.postcode5())) {
            joiner.add("postcode5 = :postcode5");
            params.addValue("postcode5", criteria.postcode5());
        }

        if (StringUtils.hasText(criteria.postcode7())) {
            joiner.add("postcode7 = :postcode7");
            params.addValue("postcode7", criteria.postcode7());
        }

        if (StringUtils.hasText(criteria.prefectureName())) {
            joiner.add("prefecture_name = :prefectureName");
            params.addValue("prefectureName", criteria.prefectureName());
        }

        if (StringUtils.hasText(criteria.prefectureNameKatakana())) {
            joiner.add("prefecture_name_katakana = :prefectureNameKatakana");
            params.addValue("prefectureNameKatakana", criteria.prefectureNameKatakana());
        }

        if (StringUtils.hasText(criteria.municipalityName())) {
            joiner.add("municipality_name = :municipalityName");
            params.addValue("municipalityName", criteria.municipalityName());
        }

        if (StringUtils.hasText(criteria.municipalityNameKatakana())) {
            joiner.add("municipality_name_katakana = :municipalityNameKatakana");
            params.addValue("municipalityNameKatakana", criteria.municipalityNameKatakana());
        }

        if (StringUtils.hasText(criteria.townAreaName())) {
            joiner.add("town_area_name = :townAreaName");
            params.addValue("townAreaName", criteria.townAreaName());
        }

        if (StringUtils.hasText(criteria.townAreaNameKatakana())) {
            joiner.add("town_area_name_katakana = :townAreaNameKatakana");
            params.addValue("townAreaNameKatakana", criteria.townAreaNameKatakana());
        }

        if (Objects.nonNull(criteria.isTownAreaWithMultiplePostcodes())) {
            joiner.add("is_town_area_with_multiple_postcodes = :isTownAreaWithMultiplePostcodes");
            params.addValue("isTownAreaWithMultiplePostcodes", criteria.isTownAreaWithMultiplePostcodes() ? "1" : "0");
        }

        if (Objects.nonNull(criteria.isTownAreaWithAddressNumbersPerKoaza())) {
            joiner.add("is_town_area_with_address_numbers_per_koaza = :isTownAreaWithAddressNumbersPerKoaza");
            params.addValue("isTownAreaWithAddressNumbersPerKoaza", criteria.isTownAreaWithAddressNumbersPerKoaza() ? "1" : "0");
        }

        if (Objects.nonNull(criteria.isTownAreaWithChome())) {
            joiner.add("is_town_area_with_chome = :isTownAreaWithChome");
            params.addValue("isTownAreaWithChome", criteria.isTownAreaWithChome() ? "1" : "0");
        }

        if (Objects.nonNull(criteria.isPostcodeWithMultipleTownAreas())) {
            joiner.add("is_postcode_with_multiple_town_areas = :isPostcodeWithMultipleTownAreas");
            params.addValue("isPostcodeWithMultipleTownAreas", criteria.isPostcodeWithMultipleTownAreas() ? "1" : "0");
        }

        if (Objects.nonNull(criteria.isChanged())) {
            joiner.add("is_changed = :isChanged");
            params.addValue("isChanged", criteria.isChanged() ? "1" : "0");
        }

        if (Objects.nonNull(criteria.changeReason())) {
            joiner.add("change_reason = :changeReason");
            params.addValue("changeReason", criteria.changeReason().getCode());
        }

        joiner.add("creation_epoch_millis = :creationEpochMillis");
        params.addValue("creationEpochMillis", criteria.creationEpochMillis());

        String criteriaQuery = joiner.toString();

        return new PageImpl<>(
                query(criteriaQuery, params, pageable),
                pageable,
                count(criteriaQuery, params));
    }

    @Override
    public List<Long> findAllCreationEpochMillis() {
        return namedJdbcOps.query(
                """
                    SELECT
                        creation_epoch_millis
                    FROM
                        posts
                    GROUP BY
                        creation_epoch_millis
                """,
                new SingleColumnRowMapper<>()
        );
    }

    @Override
    public List<Post> saveAll(Iterable<Post> entities) {
        final int batchSize = 2000;
        List<SqlParameterSource> batchParams = new ArrayList<>(batchSize);

        for (Post entity : entities) {
            batchParams.add(new MapSqlParameterSource()
                    .addValue("id", entity.id())
                    .addValue("localGovCode", entity.localGovCode())
                    .addValue("postcode5", entity.postcode5())
                    .addValue("postcode7", entity.postcode7())
                    .addValue("prefectureName", entity.prefectureName())
                    .addValue("prefectureNameKatakana", entity.prefectureNameKatakana())
                    .addValue("municipalityName", entity.municipalityName())
                    .addValue("municipalityNameKatakana", entity.municipalityNameKatakana())
                    .addValue("townAreaName", entity.townAreaName())
                    .addValue("townAreaNameKatakana", entity.townAreaNameKatakana())
                    .addValue("isTownAreaWithMultiplePostcodes", entity.isTownAreaWithMultiplePostcodes() ? "1" : "0")
                    .addValue("isTownAreaWithAddressNumbersPerKoaza", entity.isTownAreaWithAddressNumbersPerKoaza() ? "1" : "0")
                    .addValue("isTownAreaWithChome", entity.isTownAreaWithChome() ? "1" : "0")
                    .addValue("isPostcodeWithMultipleTownAreas", entity.isPostcodeWithMultipleTownAreas() ? "1" : "0")
                    .addValue("isChanged", entity.isChanged() ? "1" : "0")
                    .addValue("changeReason", Optional.ofNullable(entity.changeReason()).map(ChangeReasons::getCode).orElse(null))
                    .addValue("creationEpochMillis", entity.creationEpochMillis()));

            if (batchParams.size() >= batchSize) {
                batchUpdate(batchParams);
                batchParams.clear();
            }
        }

        if (!batchParams.isEmpty()) {
            batchUpdate(batchParams);
        }

        // To prioritise performance, returns an empty list.
        return Collections.emptyList();
    }

    @Override
    public void deleteAllByCreationEpochMillis(Iterable<Long> creationEpochMillis) {
        namedJdbcOps.batchUpdate(
                """
                    DELETE FROM
                        posts
                    WHERE
                        creation_epoch_millis = :creationEpochMillis
                """,
                StreamSupport.stream(creationEpochMillis.spliterator(), false)
                        .map(millis -> new MapSqlParameterSource()
                                .addValue("creationEpochMillis", millis))
                        .toArray(SqlParameterSource[]::new));
    }

    private List<Post> query(String criteriaQuery, SqlParameterSource criteriaParams) {
        return query(criteriaQuery, criteriaParams,null);
    }

    private List<Post> query(String criteriaQuery, SqlParameterSource criteriaParams, Pageable pageable) {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(String.class, ChangeReasons.class, ChangeReasons::of);

        MapSqlParameterSource params = new MapSqlParameterSource();
        for (String name : Optional.ofNullable(criteriaParams)
                .map(SqlParameterSource::getParameterNames).orElse(new String[0])) {
            params.addValue(name, criteriaParams.getValue(name), criteriaParams.getSqlType(name));
        }

        if (pageable != null) {
            params.addValue("limit", pageable.getPageSize());
            params.addValue("offset", pageable.getOffset());
        }

        return namedJdbcOps.query(
                """
                    SELECT
                        id,
                        local_gov_code,
                        postcode5,
                        postcode7,
                        prefecture_name,
                        prefecture_name_katakana,
                        municipality_name,
                        municipality_name_katakana,
                        town_area_name,
                        town_area_name_katakana,
                        is_town_area_with_multiple_postcodes,
                        is_town_area_with_address_numbers_per_koaza,
                        is_town_area_with_chome,
                        is_postcode_with_multiple_town_areas,
                        is_changed,
                        change_reason,
                        creation_epoch_millis
                    FROM
                        posts
                """
                + Optional
                        .ofNullable(criteriaQuery)
                        .filter(q -> !q.isBlank())
                        .map(q -> " WHERE " + q + " ")
                        .orElse(" ")
                + """
                ORDER BY
                    postcode7,
                    prefecture_name_katakana,
                    municipality_name_katakana,
                    town_area_name_katakana
                """
                + Optional.ofNullable(pageable)
                        .map(_ -> " LIMIT :limit OFFSET :offset ")
                        .orElse(" "),
                params,
                DataClassRowMapper.newInstance(Post.class, conversionService));
    }

    private int count(String criteriaQuery, SqlParameterSource criteriaParams) {
        return namedJdbcOps.queryForObject(
                """
                    SELECT
                        COUNT(*)
                    FROM
                        posts
                """
                + Optional
                        .ofNullable(criteriaQuery)
                        .filter(q -> !q.isBlank())
                        .map(q -> " WHERE " + q)
                        .orElse(""),
                criteriaParams,
                Integer.class);
    }

    private void batchUpdate(List<SqlParameterSource> params) {
        namedJdbcOps.batchUpdate(
                """
                    INSERT INTO posts (
                        id,
                        local_gov_code,
                        postcode5,
                        postcode7,
                        prefecture_name,
                        prefecture_name_katakana,
                        municipality_name,
                        municipality_name_katakana,
                        town_area_name,
                        town_area_name_katakana,
                        is_town_area_with_multiple_postcodes,
                        is_town_area_with_address_numbers_per_koaza,
                        is_town_area_with_chome,
                        is_postcode_with_multiple_town_areas,
                        is_changed,
                        change_reason,
                        creation_epoch_millis
                    ) VALUES (
                        :id,
                        :localGovCode,
                        :postcode5,
                        :postcode7,
                        :prefectureName,
                        :prefectureNameKatakana,
                        :municipalityName,
                        :municipalityNameKatakana,
                        :townAreaName,
                        :townAreaNameKatakana,
                        :isTownAreaWithMultiplePostcodes,
                        :isTownAreaWithAddressNumbersPerKoaza,
                        :isTownAreaWithChome,
                        :isPostcodeWithMultipleTownAreas,
                        :isChanged,
                        :changeReason,
                        :creationEpochMillis
                    )
                """,
                params.toArray(MapSqlParameterSource[]::new));
    }
}
