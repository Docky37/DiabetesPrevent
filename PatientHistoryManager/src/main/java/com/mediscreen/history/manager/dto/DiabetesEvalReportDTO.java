package com.mediscreen.history.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiabetesEvalReportDTO {

    /**
     * The count of the patient's diabetes risk factors.
     */
    private int riskFactorsCount;

    /**
     * The result of the patient's diabetes evaluation.
     */
    private String evalResult;

}
