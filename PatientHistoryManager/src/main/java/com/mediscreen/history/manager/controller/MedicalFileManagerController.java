package com.mediscreen.history.manager.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.service.IMedicalFileManagerService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class MedicalFileManagerController {

    /**
     * Declares an implementation of IMedicalFileService and asks Spring to inject a PatientManagerService bean when
     * PatientManagerController is created.
     */
    @Autowired
    private IMedicalFileManagerService medicalFileManagerService;

    /**
     * HTTP GET request used to get the medical file of the patient by his given patientId.
     *
     * @param patientId
     * @return a MedicalFileDTO
     * @throws Exception
     */
    @GetMapping("/medicalFiles")
    public MedicalFileDTO medicalFileById(@RequestParam final UUID patientId) throws Exception {
        log.info("NEW HTTP GET REQUEST on /medicalFiles");
        MedicalFileDTO medicalFileDTO = medicalFileManagerService.findMedicalFileById(patientId);
        log.info("Request result = {}", medicalFileDTO.toString());
        return medicalFileDTO;
    }

    /**
     * HTTP PUT request used to update the medical file of the patient.
     *
     * @param patientId
     * @param medicalFileDTO
     * @return a MedicalFileDTO (the updated medical file)
     * @throws Exception
     */
    @PutMapping("/medicalFiles")
    public MedicalFileDTO updateMedicalFile(@RequestParam final UUID patientId,
            @RequestBody final MedicalFileDTO medicalFileDTO) throws Exception {
        log.info("\nNEW HTTP PUT REQUEST on /medicalFiles with idPatient = {}", patientId.toString());
        MedicalFileDTO updatedMedicalFileDTO = medicalFileManagerService.updateMedicalFile(medicalFileDTO);
        log.info("Request result = {}", updatedMedicalFileDTO.toString());
        return updatedMedicalFileDTO;
    }

}
