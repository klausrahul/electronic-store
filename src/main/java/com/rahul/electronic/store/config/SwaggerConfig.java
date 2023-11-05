package com.rahul.electronic.store.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket docket() {

		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(getApiInfo());

		docket.securityContexts(Arrays.asList(getSecurityContext()));
		docket.securitySchemes(Arrays.asList(getSchemes()));

		// To show tha API in Swagger

		ApiSelectorBuilder builder = docket.select();
		builder.apis(RequestHandlerSelectors.any());
		builder.paths(PathSelectors.any());
		Docket build = builder.build();

		return build;
	}

	private SecurityContext getSecurityContext() {

		SecurityContext context = SecurityContext.builder().securityReferences(getSecurityRefrences()).build();

		return context;
	}

	private List<SecurityReference> getSecurityRefrences() {
		AuthorizationScope[] scopes = { new AuthorizationScope("Global", "Access Every Thing") };

		return Arrays.asList(new SecurityReference("JWT", scopes));
	}

	private ApiKey getSchemes() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private ApiInfo getApiInfo() {

		ApiInfo apiInfo = new ApiInfo("Electronic Store Backend : APIS", "This is backend Created by rahul", "1.0.0V",
				"https://www.google.com", new Contact("Rahul Mittal", "https://www.google.com", "raul@dev.in"),
				"Lincense Of APIS", "https://www.google.com", new ArrayList<>());
		return apiInfo;

	}
}
