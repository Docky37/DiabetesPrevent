package com.mediscreen.patient.adm.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.mediscreen.patient.adm.constants.PatientsAdmConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("Person")
@Data
@EqualsAndHashCode(callSuper = false)
public class Person extends Patient {

    /**
     * The phone number of this patient.
     */
    private String phone;

    /**
     * First line of the address.
     */
    @Column(length = PatientsAdmConstants.ADDRESS_LINES_MAX_LENGTH)
    private String addressLine1;

    /**
     * Optional second line, if first is not enough.
     */
    @Column(length = PatientsAdmConstants.ADDRESS_LINES_MAX_LENGTH)
    private String addressLine2;

    /**
     * The city where is located this address.
     */
    @Column(length = PatientsAdmConstants.CITY_MAX_LENGTH)
    private String city;

    /**
     * The Zip code of this address.
     */
    private String zipCode;

}
