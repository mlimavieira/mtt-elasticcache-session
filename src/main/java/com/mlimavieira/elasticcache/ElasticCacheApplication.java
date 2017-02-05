package com.mlimavieira.elasticcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile({ "local", "aws" })
public class ElasticCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticCacheApplication.class, args);
	}
}
