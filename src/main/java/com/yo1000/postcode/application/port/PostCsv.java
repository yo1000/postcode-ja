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
        String townAreaWithMultiplePostcodes,
        String townAreaWithAddressNumbersPerKoaza,
        String townAreaWithChome,
        String postcodeWithMultipleTownAreas,
        String changed,
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
            .addColumn("townAreaWithMultiplePostcodes", CsvSchema.ColumnType.STRING)
            .addColumn("townAreaWithAddressNumbersPerKoaza", CsvSchema.ColumnType.STRING)
            .addColumn("townAreaWithChome", CsvSchema.ColumnType.STRING)
            .addColumn("postcodeWithMultipleTownAreas", CsvSchema.ColumnType.STRING)
            .addColumn("changed", CsvSchema.ColumnType.STRING)
            .addColumn("changeReason", CsvSchema.ColumnType.STRING)
            .build();

    public Post toPost() {
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
                Objects.equals(townAreaWithMultiplePostcodes(), "1"),
                Objects.equals(townAreaWithAddressNumbersPerKoaza(), "1"),
                Objects.equals(townAreaWithChome(), "1"),
                Objects.equals(postcodeWithMultipleTownAreas(), "1"),
                Objects.equals(changed(), "1"),
                ChangeReasons.of(changeReason())
        );
    }
}
