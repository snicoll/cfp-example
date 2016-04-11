package com.example.cfp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties(CfpProperties.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class CfpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfpApplication.class, args);
	}

}
