package com.yo1000.postcode;

import org.springframework.boot.SpringApplication;

public class TestPostcodeApplication {

	public static void main(String[] args) {
		SpringApplication.from(PostcodeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
