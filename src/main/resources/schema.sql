CREATE TABLE posts (
    id                                          varchar(64),
    local_gov_code                              varchar(5),
    postcode5                                   varchar(5),
    postcode7                                   varchar(7),
    prefecture_name                             varchar(8),
    prefecture_name_katakana                    varchar(8),
    municipality_name                           varchar(250),
    municipality_name_katakana                  varchar(250),
    town_area_name                              varchar(250),
    town_area_name_katakana                     varchar(250),
    is_town_area_with_multiple_postcodes        varchar(1),
    is_town_area_with_address_numbers_per_koaza varchar(1),
    is_town_area_with_chome                     varchar(1),
    is_postcode_with_multiple_town_areas        varchar(1),
    is_changed                                  varchar(1),
    change_reason                               varchar(1),
    creation_epoch_millis                       bigint,
    PRIMARY KEY (id, creation_epoch_millis)
);

CREATE INDEX idx__posts__postcode5          ON posts (postcode5, creation_epoch_millis);
CREATE INDEX idx__posts__postcode7          ON posts (postcode7, creation_epoch_millis);
CREATE INDEX idx__posts__address            ON posts (prefecture_name, municipality_name, town_area_name, creation_epoch_millis);
CREATE INDEX idx__posts__address_katakana   ON posts (prefecture_name_katakana, municipality_name_katakana, town_area_name_katakana, creation_epoch_millis);
CREATE INDEX idx__posts__creation_epoch     ON posts (creation_epoch_millis);
