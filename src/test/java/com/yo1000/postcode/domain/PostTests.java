package com.yo1000.postcode.domain;

import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PostTests {
    @Test
    void test() {
        Post post = new Post(
                "01101",
                "060  ","0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false,false,false,false,
                false, ChangeReasons.NO_CHANGE);

        // When
        String postcode = post.postcode();

        // Then
        Assertions.assertThat(postcode).isEqualTo(post.postcode7());
    }
}
