package com.yo1000.postcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PostcodeApplicationTests {

	@Test
	void contextLoads() {
	}

}
