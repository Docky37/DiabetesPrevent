package com.mediscreen.patientmanager.dto;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mediscreen.patientmanager.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    /**
     * The UUID of this patient.
     */
    private UUID patientId;

    /**
     * The first name of this patient. Mandatory attribute.
     */
    @NotEmpty(message = "The first name of the patient is mandatory!")
    private String firstName;

    /**
     * The last name of this patient. Mandatory attribute.
     */
    @NotEmpty(message = "The last name of the patient is mandatory!")
    private String lastName;

    /**
     * The birth date of the patient.
     */
    @NotEmpty(message = "The last name of the patient is mandatory!")
    @Past(message = "The birth date must be in the past!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * The gender of this patient. Available values are 'M' for Male or 'F' for Female.
     */
    private Gender gender;

}
