package com.mediscreen.history.manager.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mediscreen.history.manager.constants.DiabetesEvaluation;
import com.mediscreen.history.manager.dto.DiabetesEvalReportDTO;
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;

@ExtendWith(SpringExtension.class)
public class DiabetesEvaluationServiceTest {

    private IDiabetesEvaluationService diabetesEvaluationService = new DiabetesEvaluationService();

    @Test
    @DisplayName("Given a patient's medical file, when call evaluateDiabetes method, then returns Borderline")
    public void givenAPatientMedicalFile_whenCallEvaluateDiabetes_thenReturnsBorderline() {
        // GIVEN
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.now(), "Doctor Mickael JONES", "Taille Poids Fumeur");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setBirthDate(LocalDate.of(1975, 9, 16));
        medicalFileDTO.setGender("M");
        medicalFileDTO.setAge(45);
        medicalFileDTO.addVisit(visitDTO);
        // WHEN
        DiabetesEvalReportDTO diabetesEvalReport = diabetesEvaluationService.evaluateDiabetes(medicalFileDTO);
        // THEN
        assertThat(diabetesEvalReport.getRiskFactorsCount()).isEqualTo(3);
        assertThat(diabetesEvalReport.getEvalResult()).isEqualTo(DiabetesEvaluation.BORDERLINE);
    }
    
    @Test
    @DisplayName("Given a patient's list of visits, when call concatNotes, then return the concatenation of all notes" +
            " then return BORDER_LINE.")
    public void givenAPatientListOfVisits_whenConcatNotes_thenReturnConcatenationOfAllNotes() throws Exception {
        // GIVEN
        List<VisitDTO> visitList = new ArrayList<>();
        visitList.add(new VisitDTO(LocalDateTime.of(2020, 3, 7, 15, 30), "Dr. Mickael Jones", "This is note one."));
        visitList.add(new VisitDTO(LocalDateTime.of(2020, 5, 7, 8, 30), "Dr. Mickael Jones", "This is a second note."));
        visitList.add(new VisitDTO(LocalDateTime.of(2020, 11, 10, 10, 15), "Dr. Mickael Jones", "This is last one."));

        // WHEN
        String notesConcatenation = diabetesEvaluationService.concatenateNotes(visitList);
        // THEN
        assertThat(notesConcatenation.toString()).isEqualTo("THIS IS NOTE ONE. THIS IS A SECOND NOTE. THIS IS LAST ONE.");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/countRiskData.csv", numLinesToSkip = 1)
    @DisplayName("Given a medical notes concatenation, when call countRiskFactors, then returns risk factors count.")
    public void givenANotesConcatenation_whenCallCountRiskFactors_thenReturnsRiskFactorsCount(String concatenatedNotes,
            String stringCount) throws Exception {
        // GIVEN
        // WHEN
        int countOfRiskFactors = diabetesEvaluationService.countRiskFactors(concatenatedNotes.toUpperCase());
        // THEN
        System.out.println(concatenatedNotes.toUpperCase() + "  " + stringCount);
        assertThat(countOfRiskFactors).isEqualTo(Integer.parseInt(stringCount));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/diabetesEvaluationReport.csv", numLinesToSkip = 1)
    @DisplayName("Given age, gender and risk factors count, when call generateDiabetesEvaluation," +
            " then return report value.")
    public void givenAgeRiskFactorsCountAndGender_whenGenerateDiabetesEvaluation_thenReturnsRightReportValue(
            String age, String riskFactorsCount, String gender, String reportValue)
            throws Exception {
        // GIVEN
        // WHEN
        String diabetesEvalReport = diabetesEvaluationService.generateDiabetesEvaluation(Integer.parseInt(age),
                Integer.parseInt(riskFactorsCount), gender);
        // THEN
        assertThat(diabetesEvalReport).isEqualTo(reportValue);
    }

}
