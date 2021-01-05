package com.mediscreen.patientmanager.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mediscreen.patientmanager.enums.Gender;
import com.mediscreen.patientmanager.utils.AgeCalculation;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data transfert object used to show patient personal info.
 *
 * @author Thierry Schreiner
 */
@Getter
@NoArgsConstructor
public final class PatientDetailsDTO extends PatientDTO {

    /**
     * The phone number of this patient.
     */
    private String phone;

    /**
     * First line of the address.
     */
    private String addressLine1;

    /**
     * Optional second line, if first is not enough.
     */
    private String addressLine2;

    /**
     * The city where is located this address.
     */
    private String city;

    /**
     * The Zip code of this address.
     */
    private String zipCode;

    /**
     * Class constructor with builder parameter.
     *
     * @param builder
     */
    private PatientDetailsDTO(final Builder builder) {
        super.setPatientId(builder.patientId);
        super.setFirstName(builder.firstName);
        super.setLastName(builder.lastName);
        super.setBirthDate(builder.birthDate);
        super.setGender(builder.gender);
        super.setAge(builder.age);
        this.phone = builder.phone;
        this.addressLine1 = builder.addressLine1;
        this.addressLine2 = builder.addressLine2;
        this.city = builder.city;
        this.zipCode = builder.zipCode;

    }

    /**
     * Internal builder class.
     */
    public static class Builder {
        /**
         * The id of the patient.
         */
        private UUID patientId;
        /**
         * The first name of the patient.
         */
        private String firstName;
        /**
         * The last name of the patient.
         */
        private String lastName;
        /**
         * The birth date of the patient.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        /**
         * The gender of the patient.
         */
        private Gender gender;

        /**
         * The patient age in years. Calculate value from birthDate.
         */
        private int age;

        /**
         * Setter of Phone.
         *
         * @param pPhone
         * @return Builder
         */
        public Builder setPhone(final String pPhone) {
            this.phone = pPhone;
            return this;
        }

        /**
         * Setter of addressLine1.
         *
         * @param pAddressLine1
         * @return Builder
         */
        public Builder setAddressLine1(final String pAddressLine1) {
            this.addressLine1 = pAddressLine1;
            return this;
        }

        /**
         * Setter of addressLine2.
         *
         * @param pAddressLine2
         * @return Builder
         */
        public Builder setAddressLine2(final String pAddressLine2) {
            this.addressLine2 = pAddressLine2;
            return this;
        }

        /**
         * Setter of city.
         *
         * @param pCity
         * @return Builder
         */
        public Builder setCity(final String pCity) {
            this.city = pCity;
            return this;
        }

        /**
         * Setter of zipCode.
         *
         * @param pZipCode
         * @return Builder
         */
        public Builder setZipCode(final String pZipCode) {
            this.zipCode = pZipCode;
            return this;
        }

        /**
         * The phone number of this patient.
         */
        private String phone;
        /**
         * First line of the address.
         */
        private String addressLine1;
        /**
         * Optional second line, if first is not enough.
         */
        private String addressLine2;
        /**
         * The city where is located this address.
         */
        private String city;
        /**
         * The Zip code of this address.
         */
        private String zipCode;

        /**
         * Builder method use to build an instance of this class.
         *
         * @param pPatientId
         * @param pFirstName
         * @param pLastName
         * @param pBirthDate
         * @param pGender
         */
        public Builder(final UUID pPatientId, final String pFirstName, final String pLastName,
                final LocalDate pBirthDate, final Gender pGender) {
            patientId = pPatientId;
            firstName = pFirstName;
            lastName = pLastName;
            birthDate = pBirthDate;
            gender = pGender;
            age = AgeCalculation.calculateAge(pBirthDate);
        }

        /**
         * Method that is called to construct the object.
         *
         * @return a PatientDetailsDTO
         */
        public PatientDetailsDTO build() {
            return new PatientDetailsDTO(this);
        }

    }

}
