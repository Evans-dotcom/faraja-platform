package com.example.farajaplatform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = FarajaPlatformApplicationTests.class )
class FarajaPlatformApplicationTests {

	@Configuration
	static class TestConfig {
	}

	@Test
	void contextLoads() {
	}

}
