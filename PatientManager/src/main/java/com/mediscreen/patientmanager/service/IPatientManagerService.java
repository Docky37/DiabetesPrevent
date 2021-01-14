package com.mediscreen.patientmanager.service;

import java.util.List;
import java.util.UUID;

import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;;

/**
 * This service interface provides methods that allows to request Patient API via a WebClient interface.
 *
 * @author Thierry Schreiner
 */
public interface IPatientManagerService {

    /**
     * This method makes a Get request on the /patient endpoint of Patient API.
     *
     * @return a List<PatientDTO>
     */
    List<PatientDTO> findAll() throws Exception;;

    /**
     * This method makes a Get request on the patients/search/findByLastName endpoint of Patient API.
     *
     * @param lastName
     * @return a List<PatientDTO>
     * @throws Exception
     */
    List<PatientDTO> findByLastName(String lastName) throws Exception;

    /**
     * This method makes a Get request on the patients/{patientId} endpoint of Patient API.
     *
     * @param patientId
     * @return a PatientDetailsDTO
     */
    PatientDetailsDTO showPersonalInfo(UUID patientId) throws Exception;

    /**
     * This method is used to update the details of the patient who is identified with the given patientId.
     *
     * @param patientId
     * @param newPatientDetails
     * @return a PatientDetailsDTO
     * @throws Exception
     */
    PatientDetailsDTO update(UUID patientId, PatientDetailsDTO newPatientDetails) throws Exception;

    /**
     * This method is used to add a new patient in Patient Database.
     *
     * @param newPatient
     * @return a PatientDetailsDTO
     * @throws Exception
     */
    PatientDetailsDTO addPatient(PatientDTO newPatient) throws Exception;

}
