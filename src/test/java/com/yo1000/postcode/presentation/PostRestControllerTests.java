package com.yo1000.postcode.presentation;

import com.yo1000.postcode.application.PostApplicationService;
import com.yo1000.postcode.config.WebConfig;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

@WebMvcTest(PostRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ExceptionHandlerAdvice.class, WebConfig.class})
public class PostRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    PostApplicationService postApp;

    @Test
    void test_getByPostcode7() {
        Mockito.doReturn(new PageImpl<>(List.of(
                new Post(
                        "01101",
                        "060  ","0600000",
                        "北海道", "ホッカイドウ",
                        "札幌市中央区", "サッポロシチュウオウク",
                        "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                        false,false,false,false,
                        false, ChangeReasons.NO_CHANGE, 1000L)),
                        PageRequest.of(0, 10),
                        1L))
                .when(postApp)
                .pageByPostcode7(Mockito.anyString(), Mockito.any(Pageable.class));

        mockMvc.get().uri("/posts/0600000")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("localGovCode", "01101")
                            .containsEntry("postcode5", "060  ")
                            .containsEntry("postcode7", "0600000")
                            .containsEntry("prefectureName", "北海道")
                            .containsEntry("prefectureNameKatakana", "ホッカイドウ")
                            .containsEntry("municipalityName", "札幌市中央区")
                            .containsEntry("municipalityNameKatakana", "サッポロシチュウオウク")
                            .containsEntry("townAreaName", "以下に掲載がない場合")
                            .containsEntry("townAreaNameKatakana", "イカニケイサイガナイバアイ")
                            .containsEntry("isTownAreaWithMultiplePostcodes", false)
                            .containsEntry("isTownAreaWithAddressNumbersPerKoaza", false)
                            .containsEntry("isTownAreaWithChome", false)
                            .containsEntry("isPostcodeWithMultipleTownAreas", false)
                            .containsEntry("isChanged", false)
                            .containsEntry("creationEpochMillis", 1000);
                })
                .hasPathSatisfying("$.content[0].changeReason", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("code", "0")
                            .containsEntry("displayName", "変更なし");
                });

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(postApp, Mockito.times(1))
                .pageByPostcode7(captor.capture(), Mockito.any(Pageable.class));
        Assertions.assertThat(captor.getValue()).isEqualTo("0600000");
    }

    @Test
    void test_getByPostcode5() {
        Mockito.doReturn(new PageImpl<>(List.of(
                new Post(
                        "01101",
                        "060  ","0600000",
                        "北海道", "ホッカイドウ",
                        "札幌市中央区", "サッポロシチュウオウク",
                        "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                        false,false,false,false,
                        false, ChangeReasons.NO_CHANGE, 1000L)),
                        PageRequest.of(0, 10),
                        1L)
                )
                .when(postApp)
                .pageByPostcode5(Mockito.anyString(), Mockito.any(Pageable.class));

        mockMvc.get().uri("/posts/060--")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("localGovCode", "01101")
                            .containsEntry("postcode5", "060  ")
                            .containsEntry("postcode7", "0600000")
                            .containsEntry("prefectureName", "北海道")
                            .containsEntry("prefectureNameKatakana", "ホッカイドウ")
                            .containsEntry("municipalityName", "札幌市中央区")
                            .containsEntry("municipalityNameKatakana", "サッポロシチュウオウク")
                            .containsEntry("townAreaName", "以下に掲載がない場合")
                            .containsEntry("townAreaNameKatakana", "イカニケイサイガナイバアイ")
                            .containsEntry("isTownAreaWithMultiplePostcodes", false)
                            .containsEntry("isTownAreaWithAddressNumbersPerKoaza", false)
                            .containsEntry("isTownAreaWithChome", false)
                            .containsEntry("isPostcodeWithMultipleTownAreas", false)
                            .containsEntry("isChanged", false)
                            .containsEntry("creationEpochMillis", 1000);
                })
                .hasPathSatisfying("$.content[0].changeReason", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("code", "0")
                            .containsEntry("displayName", "変更なし");
                });

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(postApp, Mockito.times(1))
                .pageByPostcode5(captor.capture(), Mockito.any(Pageable.class));
        Assertions.assertThat(captor.getValue()).isEqualTo("060  ");
    }

    @Test
    void test_getByCriteria() {
        Mockito.doReturn(new PageImpl<>(
                List.of(new Post(
                        "01101",
                        "060  ",
                        "0600000",
                        "北海道",
                        "ホッカイドウ",
                        "札幌市中央区",
                        "サッポロシチュウオウク",
                        "以下に掲載がない場合",
                        "イカニケイサイガナイバアイ",
                        false,
                        false,
                        false,
                        false,
                        false,
                        ChangeReasons.NO_CHANGE,
                        1000L)),
                        PageRequest.of(0, 10),
                        1L))
                .when(postApp)
                .pageByCriteria(Mockito.any(Post.class), Mockito.any(Pageable.class));

        mockMvc.get().uri("/posts")
                .param("localGovCode", "01101")
                .param("postcode5", "060--")
                .param("postcode7", "0600000")
                .param("prefectureName", "北海道")
                .param("prefectureNameKatakana", "ホッカイドウ")
                .param("municipalityName", "札幌市中央区")
                .param("municipalityNameKatakana", "サッポロシチュウオウク")
                .param("townAreaName", "以下に掲載がない場合")
                .param("townAreaNameKatakana", "イカニケイサイガナイバアイ")
                .param("isTownAreaWithMultiplePostcodes", "false")
                .param("isTownAreaWithAddressNumbersPerKoaza", "false")
                .param("isTownAreaWithChome", "false")
                .param("isPostcodeWithMultipleTownAreas", "false")
                .param("isChanged", "false")
                .param("changeReason", "0")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("localGovCode", "01101")
                            .containsEntry("postcode5", "060  ")
                            .containsEntry("postcode7", "0600000")
                            .containsEntry("prefectureName", "北海道")
                            .containsEntry("prefectureNameKatakana", "ホッカイドウ")
                            .containsEntry("municipalityName", "札幌市中央区")
                            .containsEntry("municipalityNameKatakana", "サッポロシチュウオウク")
                            .containsEntry("townAreaName", "以下に掲載がない場合")
                            .containsEntry("townAreaNameKatakana", "イカニケイサイガナイバアイ")
                            .containsEntry("isTownAreaWithMultiplePostcodes", false)
                            .containsEntry("isTownAreaWithAddressNumbersPerKoaza", false)
                            .containsEntry("isTownAreaWithChome", false)
                            .containsEntry("isPostcodeWithMultipleTownAreas", false)
                            .containsEntry("isChanged", false)
                            .containsEntry("creationEpochMillis", 1000);
                })
                .hasPathSatisfying("$.content[0].changeReason", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("code", "0")
                            .containsEntry("displayName", "変更なし");
                });

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(postApp, Mockito.times(1))
                .pageByCriteria(postCaptor.capture(), pageableCaptor.capture());

        Assertions.assertThat(postCaptor.getValue()).isEqualTo(new Post(
                "01101",
                "060  ","0600000",
                "北海道", "ホッカイドウ",
                "札幌市中央区", "サッポロシチュウオウク",
                "以下に掲載がない場合", "イカニケイサイガナイバアイ",
                false,false,false,false,
                false, ChangeReasons.NO_CHANGE, 0L
        ));
    }
}
