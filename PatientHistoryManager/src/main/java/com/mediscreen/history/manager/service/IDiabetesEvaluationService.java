package com.mediscreen.history.manager.service;

import java.util.List;

import com.mediscreen.history.manager.dto.DiabetesEvalReportDTO;
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;

public interface IDiabetesEvaluationService {

    /**
     * This method leads the service job. It starts with a call to concatenateNotes method with the visit list of the
     * given medicalFile as parameter, then sends the result to countRiskFactors, and finally call the
     * generateDiabetesEvaluation with parameters age, riskFactorsCount and gender.
     *
     * @param medicalFileDTO
     * @return a DiabetesEvalReportDTO
     */
    DiabetesEvalReportDTO evaluateDiabetes(MedicalFileDTO medicalFileDTO);

    /**
     * This method is used to extract and concatenate all the notes registered in the visit list of a medical record.
     *
     * @param visitList
     * @return a String
     */
    String concatenateNotes(List<VisitDTO> visitList);

    /**
     * This method is use to count the number of risk factors written in the concatenated notes of a medical record.
     *
     * @param notesConcatenation
     * @return a int
     */
    int countRiskFactors(String notesConcatenation);

    /**
     * This method generate the diabetes evaluation report of a patient from his data (age, gender and count of risk
     * factors).
     *
     * @param age
     * @param riskFactorsCount
     * @param gender
     * @return a String
     */
    String generateDiabetesEvaluation(int age, int riskFactorsCount, String gender);

}
