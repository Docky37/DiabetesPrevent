package com.mediscreen.history.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This model class is mapped to the medicalFiles MongoDB collection that is used to persist the notes taken by the
 * Doctor during visits.
 *
 * @author Thierry SCHREINER
 * @since 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "medicalFiles")
public class MedicalFile {

    /**
     * Medical file id that is equal to the patientId used in the Patient & in PatientManager.
     */
    @Id
    private String patientId;

    /**
     * First name of the patient.
     */
    @Field(value = "first_name")
    private String firstName;

    /**
     * Last name of the patient.
     */
    @Field(value = "last_name")
    private String lastName;

    /**
     * Current age of the patient.
     */
    private int age;

    /**
     * List of the patient medical visits.
     */
    private List<Visit> visits = new ArrayList<>();

    /**
     * Method that allows user to add a new visit to the list of visits.
     *
     * @param pVisit
     */
    public void addVisit(final Visit pVisit) {
        visits.add(pVisit);
    }

}
