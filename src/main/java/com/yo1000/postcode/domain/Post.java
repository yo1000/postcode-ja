package com.yo1000.postcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yo1000.postcode.domain.vo.ChangeReasons;

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
 * @param townAreaWithMultiplePostcodes 一町域が二以上の郵便番号で表される
 * @param townAreaWithAddressNumbersPerKoaza 小字毎に番地が起番されている町域
 * @param townAreaWithChome 丁目を有する町域
 * @param postcodeWithMultipleTownAreas 一つの郵便番号で二以上の町域を表す
 * @param changed 更新の有無
 * @param changeReason 変更理由
 */
public record Post(
        String localGovCode,
        String postcode5,
        String postcode7,
        String prefectureName,
        String prefectureNameKatakana,
        String municipalityName,
        String municipalityNameKatakana,
        String townAreaName,
        String townAreaNameKatakana,
        boolean townAreaWithMultiplePostcodes,
        boolean townAreaWithAddressNumbersPerKoaza,
        boolean townAreaWithChome,
        boolean postcodeWithMultipleTownAreas,
        boolean changed,
        ChangeReasons changeReason
) {
    /** 郵便番号（7桁）のエイリアス */
    @JsonIgnore
    public String postcode() {
        return postcode7();
    }
}
