package com.mediscreen.patient.adm.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mediscreen.patient.adm.model.Patient;
import com.mediscreen.patient.adm.model.Person;

/**
 * JPA repository interface use to deal with patient table of patient_adm_db.
 *
 * @author Thierry SCHREINER
 */
@RepositoryRestResource(collectionResourceRel = "persons", path = "persons")
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Returns a list of person that got this given last name.
     *
     * @param lastName
     * @return a List<Patient>
     */
    List<Person> findByLastName(String lastName);

}
