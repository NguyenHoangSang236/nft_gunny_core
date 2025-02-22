package com.nftgunny.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class},
		scanBasePackages = {
				"com.nftgunny.core",
				"com.nftgunny.core.common",
				"com.nftgunny.core.config",
				"com.nftgunny.core.entities",
				"com.nftgunny.core.repository",
				"com.nftgunny.core.utils",
		}
)
@ComponentScan(basePackages = {
		"com.nftgunny.core",
		"com.nftgunny.core.common",
		"com.nftgunny.core.config",
		"com.nftgunny.core.entities",
		"com.nftgunny.core.repository",
		"com.nftgunny.core.utils",
})
public class NftGunnyCoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(NftGunnyCoreApplication.class, args);
	}
}
