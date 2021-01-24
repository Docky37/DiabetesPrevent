package com.mediscreen.history.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            response = String.class)
    @PostMapping("/diabetesEvaluations")
    public DiabetesEvalReportDTO medicalFileById(@RequestBody final MedicalFileDTO medicalFileDTO) throws Exception {
        log.info("NEW HTTP GET REQUEST on /diabetesEvaluations");
        DiabetesEvalReportDTO report = diabetesEvaluationService.evaluateDiabetes(medicalFileDTO);
        log.info("Request result = {}", report.toString());
        return report;
    }

}
