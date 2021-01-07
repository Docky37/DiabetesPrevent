package com.mediscreen.history.manager.service;

import java.util.UUID;

import com.mediscreen.history.manager.dto.MedicalFileDTO;

public interface IMedicalFileManagerService {

    /**
     * This method returns the Medical File of the patient who has the given id.
     *
     * @param patientId
     * @return a MedicalFileDTO
     */
    MedicalFileDTO findMedicalFileById(UUID patientId);

    /**
     * This method allows you to update an existing medicalFile.
     *
     * @param medicalFileDTO
     * @return a MedicalFileDTO
     */
    MedicalFileDTO updateMedicalFile(MedicalFileDTO medicalFileDTO);

}
