package com.spaceflightsnews;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@SpringBootApplication
@EnableScheduling
@EnableAsync
@OpenAPIDefinition(info = @Info(title = "Back-end Challenge \uD83C\uDFC5 2021 - Space Flight News",
		version = "2.0", description = "Back-end Challenge Information"))
public class SpaceflightsnewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceflightsnewsApplication.class, args);
	}

}
