package com.mediscreen.history.manager.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.PatientDTO;
import com.mediscreen.history.manager.dto.VisitDTO;
import com.mediscreen.history.manager.exceptions.ForbiddenException;
import com.mediscreen.history.manager.exceptions.MedicalFileNotFoundException;
import com.mediscreen.history.manager.exceptions.UnauthorizedException;
import com.mediscreen.history.manager.service.IMedicalFileManagerService;

import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "Return the medical file of the patient by his patientId.", notes = "This endpoint of"
            + " PatientHistoryManager allows user to get medical file of a patient by his given patientId, in order"
            + " to see his history (list of his medical visits, with the notes taken by the practitioner).",
            response = MedicalFileDTO.class)
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
    @ApiOperation(value = "Update the medical file of the patient corresponding to the given patientId.",
            notes = "This endpoint of PatientHistoryManager allows user to update the medical file of the patient"
                    + " that corresponding to the given patientId.",
            response = MedicalFileDTO.class)
    @PutMapping("/medicalFiles")
    public MedicalFileDTO updateMedicalFile(@RequestParam final UUID patientId,
            @RequestBody final MedicalFileDTO medicalFileDTO) throws Exception {
        log.info("\nNEW HTTP PUT REQUEST on /medicalFiles with idPatient = {}", patientId.toString());
        MedicalFileDTO updatedMedicalFileDTO = medicalFileManagerService.updateMedicalFile(patientId, medicalFileDTO);
        log.info("Request result = {}", updatedMedicalFileDTO.toString());
        return updatedMedicalFileDTO;
    }

    /**
     * HTTP POST request used to add a medical file of a new patient.
     *
     * @param patientDTO
     * @return a MedicalFileDTO (the added medical file)
     * @throws Exception
     */
    @ApiOperation(value = "Add the medical file of a new patient.",
            notes = "This endpoint of PatientHistoryManager allows user to create the medical file of a new patient"
                    + " who does not yet have one.",
            response = MedicalFileDTO.class)
    @PostMapping("/medicalFiles")
    @ResponseStatus(HttpStatus.CREATED)
        public MedicalFileDTO addMedicalFile(@RequestBody final PatientDTO patientDTO) throws Exception {
        log.info("\nNEW HTTP POST REQUEST on /medicalFiles with content = {}", patientDTO);
        MedicalFileDTO addedMedicalFileDTO = medicalFileManagerService.addMedicalFile(patientDTO);
        log.info("Request result = {}", addedMedicalFileDTO.toString());
        return addedMedicalFileDTO;
    }

    /**
     * HTTP POST request used to add a note to the medical file of a new patient.
     *
     * @param patientId
     * @param visitDTO
     * @return a MedicalFileDTO (the added medical file)
     * @throws Exception
     */
    @ApiOperation(value = "Add a new visit, with practitioner's notes to the medical file of a patient.",
            notes = "This endpoint of PatientHistoryManager allows practitioner to add the notes of a medical visit"
                    + " to the medical file of a patient",
            response = MedicalFileDTO.class)
    @PostMapping("/medicalFiles/visits")
    @ResponseStatus(HttpStatus.OK)
    public MedicalFileDTO addMedicalFile(@RequestParam final UUID patientId,
            @RequestBody final VisitDTO visitDTO) throws Exception {
        log.info("\nNEW HTTP POST REQUEST on /medicalFiles with content = {}", visitDTO.toString());
        MedicalFileDTO addedMedicalFileDTO = medicalFileManagerService.addNoteToMedicalFile(patientId, visitDTO);
        log.info("Request result = {}", addedMedicalFileDTO.toString());
        return addedMedicalFileDTO;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private void unauthorizedHandler(
            final UnauthorizedException e) {
        log.info("END of Request with Status 401 Unauthorized");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    private void forbiddenHandler(
            final ForbiddenException e) {
        log.info("END of Request with Status 403 Forbidden");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void patientNotFoundHandler(
            final MedicalFileNotFoundException e) {
        log.info("END of Request with Status 404 NOT FOUND");
    }

}
