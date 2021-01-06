package com.mediscreen.history.manager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VisitDTO {

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
