package com.yo1000.postcode.presentation;

import com.yo1000.postcode.application.PostApplicationService;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.vo.ChangeReasons;
import jakarta.validation.constraints.Pattern;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
@Validated
public class PostRestController {
    private final PostApplicationService postApplicationService;

    public PostRestController(PostApplicationService postApplicationService) {
        this.postApplicationService = postApplicationService;
    }

    @GetMapping("/{postcode:^(?:[0-9-]{5}|[0-9]{7})$}")
    public Page<Post> getByPostcode(
            @PathVariable
            @Pattern(regexp = "^(?:[0-9-]{5}|[0-9]{7})$")
            String postcode,
            @ParameterObject
            Pageable pageable) {
        if (postcode.length() == 5) {
            return postApplicationService.pageByPostcode5(
                    postcode.replace('-', ' '), pageable);
        } else {
            return postApplicationService.pageByPostcode7(
                    postcode, pageable);
        }
    }

    @GetMapping
    public Page<Post> getByCriteria(
            @RequestParam(value = "localGovCode", required = false)
            String localGovCode,
            @RequestParam(value = "postcode5", required = false)
            @Pattern(regexp = "^[0-9-]{5}$")
            String postcode5,
            @RequestParam(value = "postcode7", required = false)
            @Pattern(regexp = "^[0-9]{7}$")
            String postcode7,
            @RequestParam(value = "prefectureName", required = false)
            String prefectureName,
            @RequestParam(value = "prefectureNameKatakana", required = false)
            String prefectureNameKatakana,
            @RequestParam(value = "municipalityName", required = false)
            String municipalityName,
            @RequestParam(value = "municipalityNameKatakana", required = false)
            String municipalityNameKatakana,
            @RequestParam(value = "townAreaName", required = false)
            String townAreaName,
            @RequestParam(value = "townAreaNameKatakana", required = false)
            String townAreaNameKatakana,
            @RequestParam(value = "isTownAreaWithMultiplePostcodes", required = false)
            Boolean isTownAreaWithMultiplePostcodes,
            @RequestParam(value = "isTownAreaWithAddressNumbersPerKoaza", required = false)
            Boolean isTownAreaWithAddressNumbersPerKoaza,
            @RequestParam(value = "isTownAreaWithChome", required = false)
            Boolean isTownAreaWithChome,
            @RequestParam(value = "isPostcodeWithMultipleTownAreas", required = false)
            Boolean isPostcodeWithMultipleTownAreas,
            @RequestParam(value = "isChanged", required = false)
            Boolean isChanged,
            @RequestParam(value = "changeReason", required = false)
            String changeReason,
            @ParameterObject
            Pageable pageable) {
        return postApplicationService.pageByCriteria(new Post(
                localGovCode,
                Optional.ofNullable(postcode5)
                        .map(v -> v.replace('-', ' '))
                        .orElse(null),
                postcode7,
                prefectureName,
                prefectureNameKatakana,
                municipalityName,
                municipalityNameKatakana,
                townAreaName,
                townAreaNameKatakana,
                isTownAreaWithMultiplePostcodes,
                isTownAreaWithAddressNumbersPerKoaza,
                isTownAreaWithChome,
                isPostcodeWithMultipleTownAreas,
                isChanged,
                ChangeReasons.of(changeReason),
                0L),
                pageable);
    }
}
