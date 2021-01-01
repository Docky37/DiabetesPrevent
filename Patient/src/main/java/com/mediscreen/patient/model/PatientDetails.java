package com.mediscreen.patient.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.mediscreen.patient.constants.PatientsConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("PatientDetails")
@Data
@EqualsAndHashCode(callSuper = false)
public class PatientDetails extends Patient {

    /**
     * The phone number of this patient.
     */
    private String phone;

    /**
     * First line of the address.
     */
    @Column(length = PatientsConstants.ADDRESS_LINES_MAX_LENGTH)
    private String addressLine1;

    /**
     * Optional second line, if first is not enough.
     */
    @Column(length = PatientsConstants.ADDRESS_LINES_MAX_LENGTH)
    private String addressLine2;

    /**
     * The city where is located this address.
     */
    @Column(length = PatientsConstants.CITY_MAX_LENGTH)
    private String city;

    /**
     * The Zip code of this address.
     */
    private String zipCode;

}
