package rest_service.com.minnibaev.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import common.com.minnibaev.utils.CryptoTool;

@Configuration
public class RestServiceConfiguration {

	@Value("${salt}")
	private String salt;

	@Bean
	public CryptoTool getCryptoTool() {
		return new CryptoTool(salt);
	}
	
}
