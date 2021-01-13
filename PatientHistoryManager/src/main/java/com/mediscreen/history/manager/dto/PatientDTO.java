package com.mediscreen.history.manager.dto;

import java.time.LocalDate;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mediscreen.history.manager.enums.Gender;
import com.mediscreen.history.manager.utils.AgeCalculation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDTO {

    /**
     * The UUID of this patient.
     */
    private UUID patientId;

    /**
     * The first name of this patient. Mandatory attribute.
     */
    private String firstName;

    /**
     * The last name of this patient. Mandatory attribute.
     */
    private String lastName;

    /**
     * The birth date of the patient.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * The gender of this patient. Available values are 'M' for Male or 'F' for Female.
     */
    private Gender gender;

    /**
     * Calculate value from birthDate.
     */
    private int age;

    /**
     * All args class constructor.
     *
     * @param pPatientId
     * @param pFirstName
     * @param pLastName
     * @param pBirthDate
     * @param pGender
     */
    public PatientDTO(final UUID pPatientId, final String pFirstName, final String pLastName,
            final LocalDate pBirthDate, final Gender pGender) {
        patientId = pPatientId;
        firstName = pFirstName;
        lastName = pLastName;
        birthDate = pBirthDate;
        gender = pGender;
        age = AgeCalculation.calculateAge(pBirthDate);
    }

    /**
     * Setter of age attribute.
     */
    public void setAge() {
        age = AgeCalculation.calculateAge(getBirthDate());
    }

}
