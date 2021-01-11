package com.mediscreen.patientmanager.controller;

import java.util.List;
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

import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;
import com.mediscreen.patientmanager.exceptions.ForbiddenException;
import com.mediscreen.patientmanager.exceptions.PatientNotFoundException;
import com.mediscreen.patientmanager.exceptions.UnauthorizedException;
import com.mediscreen.patientmanager.service.IPatientManagerService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

/**
 * The RestController class PatientManagerController provides three endpoints used to extract patients (Find all
 * patients, Find by lastName and Show Personal info).
 *
 * @author Thierry Schreiner
 */
@RestController
@Log4j2
public class PatientManagerController {

    /**
     * Declares an implementation of IPatientManagerService and asks Spring to inject a PatientManagerService bean when
     * PatientManagerController is created.
     */
    @Autowired
    private IPatientManagerService patientManagerService;

    /**
     * HTTP GET request used to get the list of all registered patients.
     *
     * @return List<PatientDTO>
     * @throws Exception
     */
    @ApiOperation(value = "Return the list of all patients", notes = "This endpoint of PatientManager allows user to"
            + " get the list of all patients registred in Patient API database",
            response = PatientDTO.class, responseContainer = "List")
    @GetMapping("/patients")
    public List<PatientDTO> patients() throws Exception {
        log.info("New HTTP GET Request on /patients");
        List<PatientDTO> patients = patientManagerService.findAll();
        log.info("  -> Result = {}", patients.toString());
        return patients;
    }

    /**
     * HTTP GET request used to get the list of all registered patients who responds to the given last name.
     *
     * @param lastName
     * @return List<PatientDTO>
     * @throws Exception
     */
    @ApiOperation(value = "Return the list of patients with name that matches the given last name",
            notes = "This endpoint of PatientManager allows user to get the list of patients registred in Patient API"
            + " database who have a last name that matches the the given last name ",
            response = PatientDTO.class, responseContainer = "List")
    @GetMapping("/patients/name")
    public List<PatientDTO> patientsByLastName(@RequestParam final String lastName) throws Exception {
        log.info("New HTTP GET Request on /patients/name");
        List<PatientDTO> patients = patientManagerService.findByLastName(lastName);
        log.info("  -> Result = {}", patients.toString());
        return patients;
    }

    /**
     * HTTP GET request used to display the personal info of patients.
     *
     * @param patientId
     * @return a PatientDetailsDTO
     * @throws Exception
     */
    @ApiOperation(value = "Return the details of the patient corresponding to the given patientId",
            notes = "This endpoint of PatientManager allows user to get the details of the patient registred in Patient"
            + " API database with the given patientId", response = PatientDetailsDTO.class)
    @GetMapping("/patients/id")
    public PatientDetailsDTO patientById(@RequestParam final UUID patientId) throws Exception {
        log.info("New HTTP GET Request on /patients/id");
        PatientDetailsDTO patientDetailsDTO = patientManagerService.showPersonalInfo(patientId);
        log.info("  -> Result = {}", patientDetailsDTO.toString());
        return patientDetailsDTO;
    }

    /**
     * HTTP PUT request used to update the personal info of patients.
     *
     * @param patientId
     * @param newPatientDetails
     * @return a PatientDetailsDTO
     * @throws Exception
     */
    @ApiOperation(value = "Update the details of the patient corresponding to the given patientId",
            notes = "This endpoint of PatientManager allows user to update the details of the patient registred in"
                    + " Patient API database with the given patientId", response = PatientDetailsDTO.class)
    @PutMapping("/patients/{id}")
    public PatientDetailsDTO updatePatientDetail(@RequestParam final UUID patientId,
            @RequestBody final PatientDetailsDTO newPatientDetails) throws Exception {
        log.info("New HTTP PUT Request on /patients/id");
        PatientDetailsDTO updatedPatientDetailsDTO = patientManagerService.update(patientId, newPatientDetails);
        log.info("  -> Result = {}", updatedPatientDetailsDTO.toString());
        return updatedPatientDetailsDTO;
    }

    /**
     * HTTP POST request used to add a new patient.
     *
     * @param newPatient
     * @return a PatientDetailsDTO
     * @throws Exception
     */
     @ApiOperation(value = "Add the given patient",
            notes = "This endpoint of PatientManager allows user to add a new patient in the Patient API database.",
            response = PatientDetailsDTO.class)
    @PostMapping("/patients")
    @ResponseStatus(HttpStatus.CREATED)
        public PatientDetailsDTO addPatient(@RequestBody final PatientDTO newPatient) throws Exception {
        log.info("New HTTP POST Request on /patients");
        PatientDetailsDTO addedPatientDetailsDTO = patientManagerService.addPatient(newPatient);
        log.info("  -> Result = {}", addedPatientDetailsDTO.toString());
        return addedPatientDetailsDTO;
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
            final PatientNotFoundException e) {
        log.info("END of Request with Status 404 NOT FOUND");
    }
}
