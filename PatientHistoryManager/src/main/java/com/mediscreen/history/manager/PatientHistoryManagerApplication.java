package com.mediscreen.history.manager;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * This class contains the main function that launches the Spring Boot Application.
 *
 * @author Thierry Schreiner
 */
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class PatientHistoryManagerApplication {

    /**
     * Method that launches the Spring Boot Application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(PatientHistoryManagerApplication.class, args);
    }

    /**
     * Protected no args class constructor.
     */
    protected PatientHistoryManagerApplication() {

    }

    /**
     * Configuration bean of Swagger documentation.
     *
     * @return a Docket
     */
    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.regex("(?!/error.*).*"))
                .apis(RequestHandlerSelectors
                        .basePackage("com.mediscreen.patientmanager"))
                .build().apiInfo(infoDetails());
    }

    /**
     * Personalize the Swagger documentation.
     *
     * @return an ApiInfo
     */
    private ApiInfo infoDetails() {
        return new ApiInfo("DiabetesPrevent - PatientHistoryManager",
                "Help to prevent Diabetes", "v0.1", "Mediscreen Software",
                new springfox.documentation.service.Contact("Thierry Schreiner",
                        "http://doc.diabetes-prevent.com/",
                        "DockyDocs@diabetes-prevent.com"),
                "API License", "http://diabetes-prevent.com",
                Collections.emptyList());
    }

}
