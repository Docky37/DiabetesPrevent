package com.mediscreen.patient.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mediscreen.patient.model.PatientDetails;

/**
 * JPA repository interface use to deal with patient_details table of patient_adm_db.
 *
 * @author Thierry SCHREINER
 */
@RepositoryRestResource(collectionResourceRel = "patients", path = "patients")
public interface PatientRepository extends JpaRepository<PatientDetails, UUID> {

    /**
     * Returns a list of person that got this given last name.
     *
     * @param lastName
     * @return a List<PatientDetails>
     */
    List<PatientDetails> findByLastName(@Param("lastName") String lastName);

}
