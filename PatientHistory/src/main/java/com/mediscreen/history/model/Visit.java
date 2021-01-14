package com.mediscreen.history.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class used to store the data of a medical visit. Used as sub collection in MedicalFile document.
 *
 * @author Thierry SCHREINER
 * @since 2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Visit {

    /**
     * The date time of this medical visit.
     */
    private LocalDateTime visitDate;

    /**
     * The name of the practitioner who sees the patient.
     */
    private String practitioner;

    /**
     * The notes taken by the practitioner during this visit.
     */
    private String notes;
}
