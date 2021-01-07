package com.mediscreen.history.manager.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This Data Transfer Object is used to transfer the patient's medical history (all the notes taken by the practitioners
 * during visits.
 *
 * @author Thierry SCHREINER
 * @since 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MedicalFileDTO {

    /**
     * Medical file id that is equal to the patientId used in Patient and in PatientManager applications.
     */
    private String patientId;

    /**
     * First name of the patient.
     */
    private String firstName;

    /**
     * Last name of the patient.
     */
    private String lastName;

    /**
     * Current age of the patient.
     */
    private int age;

    /**
     * List of the patient medical visits.
     */
    private List<VisitDTO> visits = new ArrayList<>();

    /**
     * Method that allows user to add a new visit to the list of visits.
     *
     * @param pVisit
     */
    public void addVisit(final VisitDTO pVisit) {
        visits.add(pVisit);
    }

}
