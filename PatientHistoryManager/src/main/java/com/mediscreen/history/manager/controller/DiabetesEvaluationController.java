package com.mediscreen.history.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.history.manager.dto.DiabetesEvalReportDTO;
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.service.IDiabetesEvaluationService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class DiabetesEvaluationController {

    /**
     * Declares an implementation of IMedicalFileService and asks Spring to inject a PatientManagerService bean when
     * PatientManagerController is created.
     */
    @Autowired
    private IDiabetesEvaluationService diabetesEvaluationService;

    /**
     * HTTP POST request used to generate the Diabetes evaluation report from a patient's medical file.
     *
     * @param medicalFileDTO
     * @return a DiabetesEvalReportDTO
     * @throws Exception
     */
    @ApiOperation(value = "Return the diabetes evaluation report generated from a patient medical file.",
            notes = "This endpoint of PatientHistoryManager allows user to get a Diabetes evaluation of the patient."
                    + "This report is made from a risk factor key words search in a concatenation of all practitioner's"
                    + " notes. Taking into consideration the patient's gender and age, the count of risk factors decide"
                    + "the classification into one of the four categories (None, Borderline, In Danger, Early onset).",
            response = DiabetesEvalReportDTO.class)
    @PostMapping("/diabetesEvaluations")
    @ResponseStatus(HttpStatus.OK)
    public DiabetesEvalReportDTO medicalFileById(@RequestBody final MedicalFileDTO medicalFileDTO) throws Exception {
        log.info("NEW HTTP GET REQUEST on /diabetesEvaluations");
        DiabetesEvalReportDTO report = diabetesEvaluationService.evaluateDiabetes(medicalFileDTO);
        log.info("Request result = {}", report.toString());
        return report;
    }

}
