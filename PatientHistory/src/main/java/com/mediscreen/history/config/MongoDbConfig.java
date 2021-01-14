package com.mediscreen.history.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mediscreen.history.repository.MedicalFlieRepository;

@EnableMongoRepositories(basePackageClasses = MedicalFlieRepository.class)
@Configuration
public class MongoDbConfig {

}
