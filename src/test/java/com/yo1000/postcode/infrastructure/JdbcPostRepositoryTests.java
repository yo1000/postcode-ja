package com.yo1000.postcode.infrastructure;

import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DataJdbcTest
@Testcontainers
public class JdbcPostRepositoryTests {
    @Container
    static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Required to apply `schema.sql`.
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    NamedParameterJdbcOperations namedJdbcOps;

    @Test
    @Sql(statements = {
            """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                1000
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                800
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                '99999', '99999', '9999999',
                '検査県', 'ケンサケン',
                '検査市', 'ケンサシ',
                '検査町', 'ケンサチョウ',
                '1', '1', '1', '1',
                '1', '1',
                1000
            );
            """
    })
    void test_findByPostcode5() {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        Page<Post> results = repos.findByPostcode5("060  ", Pageable.ofSize(10));

        // Then
        Assertions.assertThat(results.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(results.getContent().get(0)).isEqualTo(new Post(
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                "01101", "060  ", "0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false, false, false, false,
                false, ChangeReasons.NO_CHANGE, 1000L));
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                1000
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                800
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                '99999', '99999', '9999999',
                '検査県', 'ケンサケン',
                '検査市', 'ケンサシ',
                '検査町', 'ケンサチョウ',
                '1', '1', '1', '1',
                '1', '1',
                1000
            );
            """
    })
    void test_findByPostcode7() {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        Page<Post> results = repos.findByPostcode7("0600000", Pageable.ofSize(10));

        // Then
        Assertions.assertThat(results.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(results.getContent().get(0)).isEqualTo(new Post(
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                "01101", "060  ", "0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false, false, false, false,
                false, ChangeReasons.NO_CHANGE, 1000L));
    }

    @ParameterizedTest
    @Sql(statements = {
            """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                1000
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                '99999', '99999', '9999999',
                '検査県', 'ケンサケン',
                '検査市', 'ケンサシ',
                '検査町', 'ケンサチョウ',
                '1', '1', '1', '1',
                '1', '1',
                1000
            );
            """
    })
    @CsvSource(delimiter = '|',  textBlock =
            """
            '01101' |         |           |       |            |            |                    |                  |                       |       |       |       |       |       |
                    | '060  ' |           |       |            |            |                    |                  |                       |       |       |       |       |       |
                    |         | '0600000' |       |            |            |                    |                  |                       |       |       |       |       |       |
                    |         |           | 北海道 |            |            |                    |                  |                       |       |       |       |       |       |
                    |         |           |       | ホッカイドウ |            |                    |                  |                       |       |       |       |       |       |
                    |         |           |       |            | 札幌市中央区 |                    |                  |                       |       |       |       |       |       |
                    |         |           |       |            |            | サッポロシチュウオウク |                  |                       |       |       |       |       |       |
                    |         |           |       |            |            |                    | 以下に掲載がない場合 |                       |       |       |       |       |       |
                    |         |           |       |            |            |                    |                  | イカニケイサイガナイバアイ |       |       |       |       |       |
                    |         |           |       |            |            |                    |                  |                       | false |       |       |       |       |
                    |         |           |       |            |            |                    |                  |                       |       | false |       |       |       |
                    |         |           |       |            |            |                    |                  |                       |       |       | false |       |       |
                    |         |           |       |            |            |                    |                  |                       |       |       |       | false |       |
                    |         |           |       |            |            |                    |                  |                       |       |       |       |       | false |
                    |         |           |       |            |            |                    |                  |                       |       |       |       |       |       | 0
            """)
    void test_findByCriteria(
            String localGovCode, String postcode5, String postcode7,
            String prefectureName, String prefectureNameKatakana,
            String municipalityName, String municipalityNameKatakana,
            String townAreaName, String townAreaNameKatakana,
            Boolean isTownAreaWithMultiplePostcodes,
            Boolean isTownAreaWithAddressNumbersPerKoaza,
            Boolean isTownAreaWithChome,
            Boolean isPostcodeWithMultipleTownAreas,
            Boolean isChanged,
            String changeReason) {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        Page<Post> results = repos.findByCriteria(
                new Post(
                        null,
                        localGovCode, postcode5, postcode7,
                        prefectureName, prefectureNameKatakana,
                        municipalityName, municipalityNameKatakana,
                        townAreaName, townAreaNameKatakana,
                        isTownAreaWithMultiplePostcodes,
                        isTownAreaWithAddressNumbersPerKoaza,
                        isTownAreaWithChome,
                        isPostcodeWithMultipleTownAreas,
                        isChanged, ChangeReasons.of(changeReason),
                        1000L),
                Pageable.ofSize(10));

        // Then
        Assertions.assertThat(results.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(results.getContent().size()).isEqualTo(1);
        Assertions.assertThat(results.getContent().get(0)).isEqualTo(new Post(
                "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                "01101", "060  ", "0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false, false, false, false,
                false, ChangeReasons.NO_CHANGE, 1000L));
    }

    @Test
    void test_saveAll() {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        repos.saveAll(List.of(
                new Post(
                        "01101", "060  ", "0600000",
                        "北海道", "ホッカイドウ",
                        "札幌市中央区", "サッポロシチュウオウク",
                        "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                        false, false, false, false,
                        false, ChangeReasons.NO_CHANGE, 1000L),
                new Post(
                        "13101", "100  ", "1000000",
                        "東京都", "トウキョウト",
                        "千代田区", "チヨダク",
                        "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                        true, true, true, true,
                        true, ChangeReasons.MUNICIPAL_REORGANIZATION, 1000L),
                new Post(
                        "14101", "230  ", "2300000",
                        "神奈川県", "カナガワケン",
                        "横浜市鶴見区", "ヨコハマシツルミク",
                        "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                        false, false, false, false,
                        false, ChangeReasons.NO_CHANGE, 1000L)));

        // Then
        List<Map<String, Object>> results= namedJdbcOps.query(
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
                    ORDER BY
                        postcode7
                """,
                new ColumnMapRowMapper());

        Assertions.assertThat(results.size()).isEqualTo(3);

        Assertions.assertThat(results.get(0).get("local_gov_code")).isEqualTo("01101");
        Assertions.assertThat(results.get(0).get("postcode5")).isEqualTo("060  ");
        Assertions.assertThat(results.get(0).get("postcode7")).isEqualTo("0600000");
        Assertions.assertThat(results.get(0).get("prefecture_name")).isEqualTo("北海道");
        Assertions.assertThat(results.get(0).get("prefecture_name_katakana")).isEqualTo("ホッカイドウ");
        Assertions.assertThat(results.get(0).get("municipality_name")).isEqualTo("札幌市中央区");
        Assertions.assertThat(results.get(0).get("municipality_name_katakana")).isEqualTo("サッポロシチュウオウク");
        Assertions.assertThat(results.get(0).get("town_area_name")).isEqualTo("以下に掲載がない場合");
        Assertions.assertThat(results.get(0).get("town_area_name_katakana")).isEqualTo("イカニケイサイガナイバアイ");
        Assertions.assertThat(results.get(0).get("is_town_area_with_multiple_postcodes")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_town_area_with_address_numbers_per_koaza")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_postcode_with_multiple_town_areas")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_changed")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("change_reason")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("creation_epoch_millis")).isEqualTo(1000L);

        Assertions.assertThat(results.get(1).get("local_gov_code")).isEqualTo("13101");
        Assertions.assertThat(results.get(1).get("postcode5")).isEqualTo("100  ");
        Assertions.assertThat(results.get(1).get("postcode7")).isEqualTo("1000000");
        Assertions.assertThat(results.get(1).get("prefecture_name")).isEqualTo("東京都");
        Assertions.assertThat(results.get(1).get("prefecture_name_katakana")).isEqualTo("トウキョウト");
        Assertions.assertThat(results.get(1).get("municipality_name")).isEqualTo("千代田区");
        Assertions.assertThat(results.get(1).get("municipality_name_katakana")).isEqualTo("チヨダク");
        Assertions.assertThat(results.get(1).get("town_area_name")).isEqualTo("以下に掲載がない場合");
        Assertions.assertThat(results.get(1).get("town_area_name_katakana")).isEqualTo("イカニケイサイガナイバアイ");
        Assertions.assertThat(results.get(1).get("is_town_area_with_multiple_postcodes")).isEqualTo("1");
        Assertions.assertThat(results.get(1).get("is_town_area_with_address_numbers_per_koaza")).isEqualTo("1");
        Assertions.assertThat(results.get(1).get("is_postcode_with_multiple_town_areas")).isEqualTo("1");
        Assertions.assertThat(results.get(1).get("is_changed")).isEqualTo("1");
        Assertions.assertThat(results.get(1).get("change_reason")).isEqualTo("1");
        Assertions.assertThat(results.get(1).get("creation_epoch_millis")).isEqualTo(1000L);

        Assertions.assertThat(results.get(2).get("local_gov_code")).isEqualTo("14101");
        Assertions.assertThat(results.get(2).get("postcode5")).isEqualTo("230  ");
        Assertions.assertThat(results.get(2).get("postcode7")).isEqualTo("2300000");
        Assertions.assertThat(results.get(2).get("prefecture_name")).isEqualTo("神奈川県");
        Assertions.assertThat(results.get(2).get("prefecture_name_katakana")).isEqualTo("カナガワケン");
        Assertions.assertThat(results.get(2).get("municipality_name")).isEqualTo("横浜市鶴見区");
        Assertions.assertThat(results.get(2).get("municipality_name_katakana")).isEqualTo("ヨコハマシツルミク");
        Assertions.assertThat(results.get(2).get("town_area_name")).isEqualTo("以下に掲載がない場合");
        Assertions.assertThat(results.get(2).get("town_area_name_katakana")).isEqualTo("イカニケイサイガナイバアイ");
        Assertions.assertThat(results.get(2).get("is_town_area_with_multiple_postcodes")).isEqualTo("0");
        Assertions.assertThat(results.get(2).get("is_town_area_with_address_numbers_per_koaza")).isEqualTo("0");
        Assertions.assertThat(results.get(2).get("is_postcode_with_multiple_town_areas")).isEqualTo("0");
        Assertions.assertThat(results.get(2).get("is_changed")).isEqualTo("0");
        Assertions.assertThat(results.get(2).get("change_reason")).isEqualTo("0");
        Assertions.assertThat(results.get(2).get("creation_epoch_millis")).isEqualTo(1000L);
    }

    @Test
    void test_saveAll_tooManyPosts() {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        List<Post> entities = new ArrayList<>();
        for (int i = 0; i < 2002; i++) {
            String id = "0000" + i;
            entities.add(new Post(
                    id.substring(id.length() - 4),
                    "01101", "060  ", "0600000",
                    "北海道", "ホッカイドウ",
                    "札幌市中央区", "サッポロシチュウオウク",
                    "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                    false, false, false, false,
                    false, ChangeReasons.NO_CHANGE, 1000L));
        }

        repos.saveAll(entities);

        // Then
        List<Map<String, Object>> results= namedJdbcOps.query(
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
                        ORDER BY
                            id
                    """,
                new ColumnMapRowMapper());

        Assertions.assertThat(results.size()).isEqualTo(2002);

        Assertions.assertThat(results.get(0).get("id")).isEqualTo("0000");
        Assertions.assertThat(results.get(0).get("local_gov_code")).isEqualTo("01101");
        Assertions.assertThat(results.get(0).get("creation_epoch_millis")).isEqualTo(1000L);

        Assertions.assertThat(results.get(2001).get("id")).isEqualTo("2001");
        Assertions.assertThat(results.get(2001).get("local_gov_code")).isEqualTo("01101");
        Assertions.assertThat(results.get(2001).get("creation_epoch_millis")).isEqualTo(1000L);
    }

    @Test
    @Sql(statements = {
            """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcde1',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                1000
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcde2',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                2000
            );
            """, """
            INSERT INTO posts (
                id,
                local_gov_code, postcode5, postcode7,
                prefecture_name, prefecture_name_katakana,
                municipality_name, municipality_name_katakana,
                town_area_name, town_area_name_katakana,
                is_town_area_with_multiple_postcodes,
                is_town_area_with_address_numbers_per_koaza,
                is_town_area_with_chome,
                is_postcode_with_multiple_town_areas,
                is_changed, change_reason,
                creation_epoch_millis
            ) VALUES (
                '1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcde3',
                '01101', '060  ', '0600000',
                '北海道', 'ホッカイドウ',
                '札幌市中央区', 'サッポロシチュウオウク',
                '以下に掲載がない場合', 'イカニケイサイガナイバアイ',
                '0', '0', '0', '0',
                '0', '0',
                3000
            );
            """
    })
    void test_deleteAllByCreationEpochMillis() {
        // Given
        JdbcPostRepository repos = new JdbcPostRepository(namedJdbcOps);

        // When
        repos.deleteAllByCreationEpochMillis(List.of(1000L, 3000L));

        // Then
        List<Map<String, Object>> results= namedJdbcOps.query(
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
                        ORDER BY
                            postcode7
                    """,
                new ColumnMapRowMapper());

        Assertions.assertThat(results.size()).isEqualTo(1);

        Assertions.assertThat(results.get(0).get("id")).isEqualTo("1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcde2");
        Assertions.assertThat(results.get(0).get("local_gov_code")).isEqualTo("01101");
        Assertions.assertThat(results.get(0).get("postcode5")).isEqualTo("060  ");
        Assertions.assertThat(results.get(0).get("postcode7")).isEqualTo("0600000");
        Assertions.assertThat(results.get(0).get("prefecture_name")).isEqualTo("北海道");
        Assertions.assertThat(results.get(0).get("prefecture_name_katakana")).isEqualTo("ホッカイドウ");
        Assertions.assertThat(results.get(0).get("municipality_name")).isEqualTo("札幌市中央区");
        Assertions.assertThat(results.get(0).get("municipality_name_katakana")).isEqualTo("サッポロシチュウオウク");
        Assertions.assertThat(results.get(0).get("town_area_name")).isEqualTo("以下に掲載がない場合");
        Assertions.assertThat(results.get(0).get("town_area_name_katakana")).isEqualTo("イカニケイサイガナイバアイ");
        Assertions.assertThat(results.get(0).get("is_town_area_with_multiple_postcodes")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_town_area_with_address_numbers_per_koaza")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_postcode_with_multiple_town_areas")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("is_changed")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("change_reason")).isEqualTo("0");
        Assertions.assertThat(results.get(0).get("creation_epoch_millis")).isEqualTo(2000L);
    }
}
