package com.yo1000.postcode.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChangeReasonsTests {
    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            0 | 0 | 変更なし
            1 | 1 | 市政・区政・町政・分区・政令指定都市施行
            2 | 2 | 住居表示の実施
            3 | 3 | 区画整理
            4 | 4 | 郵便区調整等
            5 | 5 | 訂正
            6 | 6 | 廃止
            """)
    void test(String code, String expectCode, String expectDisplayName) {
        // When
        ChangeReasons changeReasons = ChangeReasons.of(code);

        // Then
        Assertions.assertThat(changeReasons).isNotNull();
        Assertions.assertThat(changeReasons.getCode()).isEqualTo(expectCode);
        Assertions.assertThat(changeReasons.getDisplayName()).isEqualTo(expectDisplayName);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            -1
            7
            66
            A
            """)
    void test_null(String code) {
        // When
        ChangeReasons changeReasons = ChangeReasons.of(code);

        // Then
        Assertions.assertThat(changeReasons).isNull();
    }
}
