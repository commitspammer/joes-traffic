package com.livetraffic.api.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Autowired
	@Bean
	public WebClient orionClient(@Value("${webservice.orion.baseurl}") String baseURL) {
		return WebClient.builder()
			.baseUrl(baseURL + "/v2/")
			.build();
	}

}
