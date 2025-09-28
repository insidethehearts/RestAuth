package me.therimuru.RestAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class RestAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAuthApplication.class, args);
	}

}
