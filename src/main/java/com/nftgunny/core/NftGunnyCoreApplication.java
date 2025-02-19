package com.nftgunny.core;

import com.nftgunny.core.utils.StarkliUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NftGunnyCoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(NftGunnyCoreApplication.class, args);

//		StarkliUtils starkliUtils = new StarkliUtils();
//		starkliUtils.testCommandLine();
	}
}
