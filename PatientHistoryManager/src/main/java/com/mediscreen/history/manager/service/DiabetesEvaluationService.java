package com.mediscreen.history.manager.service;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mediscreen.history.manager.constants.DiabetesEvaluation;
import com.mediscreen.history.manager.dto.DiabetesEvalReportDTO;
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.PatientDetailsDTO;
import com.mediscreen.history.manager.dto.VisitDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DiabetesEvaluationService implements IDiabetesEvaluationService {

    /**
     * Constant that give a time to wait.
     */
    private static final int TIME_TO_WAIT = 500;
    /**
     * Variable used to store the requested PatientDetailsDTO that will be inserted in report.
     */
    private PatientDetailsDTO patient;
    /**
     * Variable used to store the concatenation of all practitionner's notes inserted in the medical file.
     */
    private String notesConcatenation = "";
    /**
     * Variable used to store the count of risk factors presented by the patient.
     */
    private int riskFactorCount = 0;
    /**
     * A MedicalFile Webclient declaration. The bean is injected by Spring with the class constructor @Autowired
     * annotation.
     */
    private WebClient webClientPatientManager;

    /**
     * This class constructor allows Spring to inject a WebClient bean.
     *
     * @param pWebClientPatientManager
     */
    @Autowired
    public DiabetesEvaluationService(
            @Qualifier("getWebClientPatientManager") final WebClient pWebClientPatientManager) {
        webClientPatientManager = pWebClientPatientManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiabetesEvalReportDTO evaluateDiabetes(final MedicalFileDTO medicalFileDTO) {
        String uri = "/patients/id?patientId=" + medicalFileDTO.getPatientId();

        Mono<PatientDetailsDTO> patientMono = webClientPatientManager.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(PatientDetailsDTO.class);

        patientMono.subscribe(p -> {
            patient = p;
        });

        String concatenation = concatenateNotes(medicalFileDTO.getVisits());
        log.debug("Notes concatenation = {}", concatenation);
        int count = countRiskFactors(concatenation);
        log.debug("Risk factors count = {}", count);
        String report = generateDiabetesEvaluation(medicalFileDTO.getAge(), count, medicalFileDTO.getGender());
        log.debug("Diabetes evaluation report = {}", report);

        while (patient == null) {
            try {
                Thread.sleep(TIME_TO_WAIT);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.info(patient.toString());
        return new DiabetesEvalReportDTO(patient, count, report);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String concatenateNotes(final List<VisitDTO> visitList) {

        notesConcatenation = "";
        visitList.forEach(visit -> {
            notesConcatenation = notesConcatenation.concat(visit.getNotes().toUpperCase() + " ");
        });
        notesConcatenation = notesConcatenation.substring(0, notesConcatenation.length() - 1);

        return notesConcatenation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countRiskFactors(final String pNotesConcatenation) {
        this.riskFactorCount = 0;
        String formattedNotes = removeDiacriticalMarks(pNotesConcatenation);
        DiabetesEvaluation.RISK_FACTORS.forEach(rf -> {
            if (formattedNotes.contains(rf)) {
                this.riskFactorCount += 1;
                System.out.println(rf);
            }
        });
        return riskFactorCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateDiabetesEvaluation(final int age, final int riskFactorsCount, final String gender) {
        String report = "";
        switch (riskFactorsCount) {
        case 0:
        case 1:
            report = DiabetesEvaluation.NONE;
            break;
        case 2:
            if (age > DiabetesEvaluation.PIVOT_AGE - 1) {
                report = DiabetesEvaluation.BORDERLINE;
            } else {
                report = DiabetesEvaluation.NONE;
            }
            break;
        case DiabetesEvaluation.THREE:
            if ((age < DiabetesEvaluation.PIVOT_AGE) && (gender.equals("M"))) {
                report = DiabetesEvaluation.DANGER;
            } else if (age < DiabetesEvaluation.PIVOT_AGE) {
                report = DiabetesEvaluation.NONE;
            } else {
                report = DiabetesEvaluation.BORDERLINE;
            }
            break;
        case DiabetesEvaluation.FOUR:
            if (age < DiabetesEvaluation.PIVOT_AGE) {
                report = DiabetesEvaluation.DANGER;
            } else {
                report = DiabetesEvaluation.BORDERLINE;
            }
            break;
        case DiabetesEvaluation.FIVE:
            if ((age < DiabetesEvaluation.PIVOT_AGE) && (gender.equals("M"))) {
                report = DiabetesEvaluation.EARLY_ONSET;
            } else if (age < DiabetesEvaluation.PIVOT_AGE) {
                report = DiabetesEvaluation.DANGER;
            } else {
                report = DiabetesEvaluation.BORDERLINE;
            }
            break;
        case DiabetesEvaluation.SIX:
            if ((age < DiabetesEvaluation.PIVOT_AGE) && (gender.equals("M"))) {
                report = DiabetesEvaluation.EARLY_ONSET;
            } else {
                report = DiabetesEvaluation.DANGER;
            }
            break;
        case DiabetesEvaluation.SEVEN:
            if (age < DiabetesEvaluation.PIVOT_AGE) {
                report = DiabetesEvaluation.EARLY_ONSET;
            } else {
                report = DiabetesEvaluation.DANGER;
            }
            break;
        case DiabetesEvaluation.EIGHT:
        case DiabetesEvaluation.NINE:
        case DiabetesEvaluation.TEN:
        case DiabetesEvaluation.ELEVEN:
            report = DiabetesEvaluation.EARLY_ONSET;
            break;
        default:
            log.error("The risk factors count has not a valid value!");
        }

        return report;
    }

    private String removeDiacriticalMarks(final String string) {
        return Normalizer.normalize(string, Form.NFKD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{ASCII}]", "");
    }

}
