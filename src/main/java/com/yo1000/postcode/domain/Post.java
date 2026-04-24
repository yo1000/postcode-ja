package com.yo1000.postcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yo1000.postcode.domain.vo.ChangeReasons;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.StringJoiner;

/**
 *
 * @param localGovCode 全国地方公共団体コード（JIS X0401、X0402）
 * @param postcode5 （旧）郵便番号（5桁）
 * @param postcode7 郵便番号（7桁）
 * @param prefectureName 都道府県名
 * @param prefectureNameKatakana 都道府県名カタカナ
 * @param municipalityName 市区町村名
 * @param municipalityNameKatakana 市区町村名カタカナ
 * @param townAreaName 町域名
 * @param townAreaNameKatakana 町域名カタカナ
 * @param isTownAreaWithMultiplePostcodes 一町域が二以上の郵便番号で表される
 * @param isTownAreaWithAddressNumbersPerKoaza 小字毎に番地が起番されている町域
 * @param isTownAreaWithChome 丁目を有する町域
 * @param isPostcodeWithMultipleTownAreas 一つの郵便番号で二以上の町域を表す
 * @param isChanged 更新の有無
 * @param changeReason 変更理由
 * @param creationEpochMillis
 */
public record Post(
        String id,
        String localGovCode,
        String postcode5,
        String postcode7,
        String prefectureName,
        String prefectureNameKatakana,
        String municipalityName,
        String municipalityNameKatakana,
        String townAreaName,
        String townAreaNameKatakana,
        Boolean isTownAreaWithMultiplePostcodes,
        Boolean isTownAreaWithAddressNumbersPerKoaza,
        Boolean isTownAreaWithChome,
        Boolean isPostcodeWithMultipleTownAreas,
        Boolean isChanged,
        ChangeReasons changeReason,
        long creationEpochMillis
) {
    public Post(
            String localGovCode,
            String postcode5,
            String postcode7,
            String prefectureName,
            String prefectureNameKatakana,
            String municipalityName,
            String municipalityNameKatakana,
            String townAreaName,
            String townAreaNameKatakana,
            Boolean isTownAreaWithMultiplePostcodes,
            Boolean isTownAreaWithAddressNumbersPerKoaza,
            Boolean isTownAreaWithChome,
            Boolean isPostcodeWithMultipleTownAreas,
            Boolean isChanged,
            ChangeReasons changeReason,
            long creationEpochMillis) {
        this(genId(localGovCode, postcode5, postcode7, prefectureName, municipalityName, townAreaName),
                localGovCode,
                postcode5,
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
                changeReason,
                creationEpochMillis);
    }

    public Post updateCreationEpochMillis(long creationEpochMillis) {
        return new Post(
                localGovCode(),
                postcode5(),
                postcode7(),
                prefectureName(),
                prefectureNameKatakana(),
                municipalityName(),
                municipalityNameKatakana(),
                townAreaName(),
                townAreaNameKatakana(),
                isTownAreaWithMultiplePostcodes(),
                isTownAreaWithAddressNumbersPerKoaza(),
                isTownAreaWithChome(),
                isPostcodeWithMultipleTownAreas(),
                isChanged(),
                changeReason(),
                creationEpochMillis);
    }

    /** 郵便番号（7桁）のエイリアス */
    @JsonIgnore
    public String postcode() {
        return postcode7();
    }

    public static String genId(
            String localGovCode,
            String postcode5,
            String postcode7,
            String prefectureName,
            String municipalityName,
            String townAreaName) {
        // Invisible separator
        StringJoiner joiner = new StringJoiner("\u001F");
        joiner.add(localGovCode)
                .add(postcode5)
                .add(postcode7)
                .add(prefectureName)
                .add(municipalityName)
                .add(townAreaName);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(joiner.toString().getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
