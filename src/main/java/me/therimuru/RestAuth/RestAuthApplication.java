package me.therimuru.RestAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.data.redis.autoconfigure.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.security.autoconfigure.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "me.therimuru.RestAuth.repository")
@EntityScan(basePackages = "me.therimuru.RestAuth.entity")
@SpringBootApplication (exclude = {SecurityAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class RestAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAuthApplication.class, args);
	}

}
