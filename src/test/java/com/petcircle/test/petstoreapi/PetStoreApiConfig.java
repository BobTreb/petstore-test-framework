package com.petcircle.test.petstoreapi;

import com.petcircle.utils.TestUtils.YamlPropertySourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@PropertySource(
        value = "classpath:config/petstoreapi/application.yaml",
        factory = YamlPropertySourceFactory.class
)
public class PetStoreApiConfig {

    @Value("${petstore.api.baseurl}")
    private String baseUrl;

    @Bean
    public String baseUrl() {
        return baseUrl;
    }
}
