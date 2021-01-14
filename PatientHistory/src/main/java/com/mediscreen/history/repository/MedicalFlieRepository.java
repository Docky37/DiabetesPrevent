package com.mediscreen.history.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mediscreen.history.model.MedicalFile;

public interface MedicalFlieRepository extends MongoRepository<MedicalFile, String>{

}
