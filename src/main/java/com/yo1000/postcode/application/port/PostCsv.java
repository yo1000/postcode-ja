package com.yo1000.postcode.application.port;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.yo1000.postcode.domain.Post;
import com.yo1000.postcode.domain.vo.ChangeReasons;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PostCsv(
        String localGovCode,
        String postcode5,
        String postcode7,
        String prefectureName,
        String prefectureNameKatakana,
        String municipalityName,
        String municipalityNameKatakana,
        String townAreaName,
        String townAreaNameKatakana,
        String isTownAreaWithMultiplePostcodes,
        String isTownAreaWithAddressNumbersPerKoaza,
        String isTownAreaWithChome,
        String isPostcodeWithMultipleTownAreas,
        String isChanged,
        String changeReason
) {
    public static final CsvSchema SCHEMA = CsvSchema.builder()
            .addColumn("localGovCode", CsvSchema.ColumnType.STRING)
            .addColumn("postcode5", CsvSchema.ColumnType.STRING)
            .addColumn("postcode7", CsvSchema.ColumnType.STRING)
            .addColumn("prefectureNameKatakana", CsvSchema.ColumnType.STRING)
            .addColumn("municipalityNameKatakana", CsvSchema.ColumnType.STRING)
            .addColumn("townAreaNameKatakana", CsvSchema.ColumnType.STRING)
            .addColumn("prefectureName", CsvSchema.ColumnType.STRING)
            .addColumn("municipalityName", CsvSchema.ColumnType.STRING)
            .addColumn("townAreaName", CsvSchema.ColumnType.STRING)
            .addColumn("isTownAreaWithMultiplePostcodes", CsvSchema.ColumnType.STRING)
            .addColumn("isTownAreaWithAddressNumbersPerKoaza", CsvSchema.ColumnType.STRING)
            .addColumn("isTownAreaWithChome", CsvSchema.ColumnType.STRING)
            .addColumn("isPostcodeWithMultipleTownAreas", CsvSchema.ColumnType.STRING)
            .addColumn("isChanged", CsvSchema.ColumnType.STRING)
            .addColumn("changeReason", CsvSchema.ColumnType.STRING)
            .build();

    public Post toPost(long creationEpochMillis) {
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
                Objects.equals(isTownAreaWithMultiplePostcodes(), "1"),
                Objects.equals(isTownAreaWithAddressNumbersPerKoaza(), "1"),
                Objects.equals(isTownAreaWithChome(), "1"),
                Objects.equals(isPostcodeWithMultipleTownAreas(), "1"),
                Objects.equals(isChanged(), "1"),
                ChangeReasons.of(changeReason()),
                creationEpochMillis
        );
    }
}
