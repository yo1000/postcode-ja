package com.yo1000.postcode.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class AppPropertiesTests {
    @DynamicPropertySource
    static void configureProps(DynamicPropertyRegistry registry) {
        registry.add("app.resource", () -> "classpath:/utf_ken_all.zip");
    }

    @Autowired
    AppProperties appProps;

    @Test
    void test() {
        Assertions.assertThat(appProps.getResource().exists()).isTrue();
    }
}
