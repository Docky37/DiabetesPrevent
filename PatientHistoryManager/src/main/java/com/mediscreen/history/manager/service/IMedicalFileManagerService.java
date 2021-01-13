package com.mediscreen.history.manager.service;

import java.util.UUID;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.PatientDTO;
import com.mediscreen.history.manager.dto.VisitDTO;
import com.mediscreen.history.manager.exceptions.ForbiddenException;
import com.mediscreen.history.manager.exceptions.ConflictException;
import com.mediscreen.history.manager.exceptions.MedicalFileNotFoundException;
import com.mediscreen.history.manager.exceptions.UnauthorizedException;

public interface IMedicalFileManagerService {

    /**
     * This method returns the Medical File of the patient who has the given id.
     *
     * @param patientId
     * @return a MedicalFileDTO
     * @throws MedicalFileNotFoundException
     * @throws ForbiddenException
     * @throws UnauthorizedException
     */
    MedicalFileDTO findMedicalFileById(UUID patientId)
            throws MedicalFileNotFoundException, UnauthorizedException, ForbiddenException;

    /**
     * This method allows you to update an existing medicalFile.
     *
     * @param patientId
     * @param medicalFileDTO
     * @return a MedicalFileDTO (the updated medical file)
     * @throws UnauthorizedException
     * @throws ForbiddenException
     * @throws MedicalFileNotFoundException
     */
    MedicalFileDTO updateMedicalFile(UUID patientId, MedicalFileDTO medicalFileDTO)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException;

    /**
     * This method allows you to add a new medicalFile for a new patient.
     *
     * @param patientDTO
     * @return a MedicalFileDTO (the added medical file)
     * @throws ForbiddenException
     * @throws UnauthorizedException
     * @throws MedicalFileNotFoundException
     * @throws ConflictException
     */
    MedicalFileDTO addMedicalFile(PatientDTO patientDTO)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException,
            ConflictException;

    /**
     * This method allows you to add a new note to the medicalFile for a patient.
     *
     * @param patientId
     * @param visitDTO
     * @return a MedicalFileDTO (the updated medical file)
     * @throws MedicalFileNotFoundException
     * @throws ForbiddenException
     * @throws UnauthorizedException
     */
    MedicalFileDTO addNoteToMedicalFile(UUID patientId, VisitDTO visitDTO)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException;

}
