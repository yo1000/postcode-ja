package com.yo1000.postcode.application.port;

import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PostCsvTests {
    @Test
    void test() {
        PostCsv postCsv = new PostCsv(
                "01101",
                "060  ","0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                "0","0","0","0",
                "0","0");

        // When
        Post post = postCsv.toPost();

        // Then
        Assertions.assertThat(post).isEqualTo(new Post(
                "01101",
                "060  ","0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false,false,false,false,
                false, ChangeReasons.NO_CHANGE));
    }
}
