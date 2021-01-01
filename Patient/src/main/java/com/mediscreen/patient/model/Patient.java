package com.mediscreen.patient.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.mediscreen.patient.constants.PatientsConstants;
import com.mediscreen.patient.enums.Gender;

import lombok.Data;

/**
 * Entity class that contains the personal info of a patient and that is mapped with the table patient of patient_adm_db
 * database.
 *
 * @author Thierry SCHREINER
 */
@Entity
@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = { "firstName", "lastName",
        "birthDate" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Patient")
@Data
public class Patient {

    /**
     * The UUID of this patient.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID patientId;

    /**
     * The first name of this patient. Mandatory attribute.
     */
    @NotEmpty(message = "The first name of the patient is mandatory!")
    @Column(nullable = false, length = PatientsConstants.FIRSTNAME_MAX_LENGTH)
    private String firstName;

    /**
     * The last name of this patient. Mandatory attribute.
     */
    @NotEmpty(message = "The last name of the patient is mandatory!")
    @Column(nullable = false, length = PatientsConstants.LASTNAME_MAX_LENGTH)
    private String lastName;

    /**
     * The birth date of the patient.
     */
    @Past(message = "The birth date must be in the past!")
    private LocalDate birthDate;

    /**
     * The gender of this patient. Available values are 'M' for Male or 'F' for Female.
     */
    private Gender gender;

}
