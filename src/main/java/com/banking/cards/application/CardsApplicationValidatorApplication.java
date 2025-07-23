package com.banking.cards.application;

import com.banking.cards.application.config.Topics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Topics.class)
public class CardsApplicationValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplicationValidatorApplication.class, args);
	}

}
