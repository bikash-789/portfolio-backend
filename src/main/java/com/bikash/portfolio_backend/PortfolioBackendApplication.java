package com.bikash.portfolio_backend;

import com.bikash.portfolio_backend.config.AdminEmailConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
@EnableConfigurationProperties(AdminEmailConfig.class)
public class PortfolioBackendApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		System.out.println("Application timezone set to: " + TimeZone.getDefault().getDisplayName());
	}

	public static void main(String[] args) {
		SpringApplication.run(PortfolioBackendApplication.class, args);
	}
}
