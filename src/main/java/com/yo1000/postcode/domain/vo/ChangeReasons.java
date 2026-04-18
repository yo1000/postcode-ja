package com.yo1000.postcode.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChangeReasons {
    NO_CHANGE("0", "変更なし"),
    MUNICIPAL_REORGANIZATION("1", "市政・区政・町政・分区・政令指定都市施行"),
    ADDRESS_REFORM_IMPLEMENTED("2", "住居表示の実施"),
    LAND_READJUSTMENT("3", "区画整理"),
    POSTAL_DISTRICT_ADJUSTMENT("4", "郵便区調整等"),
    CORRECTION("5", "訂正"),
    ABOLITION("6", "廃止"),
    ;

    private final String code;
    private final String displayName;

    ChangeReasons(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static ChangeReasons of(@JsonProperty("code") String code) {
        return Arrays.stream(values())
                .filter(v -> Objects.equals(v.code, code))
                .findFirst()
                .orElse(null);
    }
}
