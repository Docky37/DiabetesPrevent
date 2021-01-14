package com.mediscreen.history.manager.config;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomizeWebClient {

    /**
     * Defines the WebClient beans to deal with PatientHistory API.
     *
     * @return a WebClient
     */
    @Bean
    public WebClient getWebClientPatientHistory() {
        return WebClient.create("http://127.0.0.1:7799"); // run from IDE
    }

    /**
     * Configures WebClient to consume Hypertext Application Language.
     *
     * @param configurer
     * @return a WebClientCustomizer
     */
    @Bean
    WebClientCustomizer hypermediaWebClientCustomizer(final HypermediaWebClientConfigurer configurer) {
        return webClientBuilder -> {
            configurer.registerHypermediaTypes(webClientBuilder);
        };
    }

}
