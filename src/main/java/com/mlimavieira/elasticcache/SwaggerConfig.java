package com.mlimavieira.elasticcache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Elastic-Cache-Sample")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.regex("/.*"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("TVPD Server Session")
				.description("Sample Application of Spring Cache + AWS ElasticCache")
				.contact("Marcio Vieira")
				.license("Apache License Version 2.0")
				.version("2.0")
				.build();
	}

}
