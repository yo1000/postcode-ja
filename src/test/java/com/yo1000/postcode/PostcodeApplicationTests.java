package com.yo1000.postcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
class PostcodeApplicationTests {
	@Container
	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest");

	@DynamicPropertySource
	static void configureProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
		registry.add("spring.datasource.username", () -> postgres.getUsername());
		registry.add("spring.datasource.password", () -> postgres.getPassword());

		// Required to apply `schema.sql`.
		registry.add("spring.sql.init.mode", () -> "always");
	}

	@Test
	void contextLoads() {}
}
