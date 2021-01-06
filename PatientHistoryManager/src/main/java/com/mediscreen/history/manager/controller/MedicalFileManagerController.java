package com.mediscreen.history.manager.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/medicalFiles")
    public MedicalFileDTO medicalFileById(@RequestParam final UUID patientId) throws Exception {
        log.info("NEW HTTP GET REQUEST on /medicalFiles");
        MedicalFileDTO medicalFileDTO = medicalFileManagerService.findMedicalFileById(patientId);
        log.info("Request result = {}");
        return medicalFileDTO;
    }

}
