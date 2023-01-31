package com.pot.benefits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.pot")
@EnableJpaAuditing
@EnableScheduling
@EnableEurekaClient
public class BenefitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BenefitServiceApplication.class, args);
	}

}
