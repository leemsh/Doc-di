package com.dd.server.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public DefaultUriBuilderFactory builderFactory(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return factory;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .uriBuilderFactory(builderFactory())
                .build();
    }
}
