package com.mediscreen.history.manager.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * This service class use a WebClient to request the API PatientHistory.
 *
 * @author Thierry Schreiner
 */
@Service
@Log4j2
public class MedicalFileManagerService implements IMedicalFileManagerService {

    /**
     * A MedicalFile Webclient declaration. The bean is injected by Spring with the class constructor @Autowired
     * annotation.
     */
    private WebClient webClientMedicalFile;

    /**
     * This class constructor allows Spring to inject a WebClient bean.
     *
     * @param pWebClientMedicalFile
     */
    @Autowired
    public MedicalFileManagerService(final WebClient pWebClientMedicalFile) {
        webClientMedicalFile = pWebClientMedicalFile;
    }

    /**
     * This method returns the Medical File of the patient who has the given id.
     * 
     * @param patientId
     * @return a MedicalFileDTO
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MedicalFileDTO findMedicalFileById(final UUID patientId) {
        final String getMedicalFileUri = "/medicalFiles/" + patientId.toString();
        Mono<? extends EntityModel> medFileMono = webClientMedicalFile.get()
                .uri(getMedicalFileUri)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        LinkedHashMap<String, Object> medFileHashMap = (LinkedHashMap<String, Object>) medFileMono.block()
                .getContent();
        log.debug("MedicalFileDTO --> {}", medFileHashMap.toString());

        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();

        medicalFileDTO.setPatientId(patientId.toString());
        medicalFileDTO.setFirstName((String) medFileHashMap.get("firstName"));
        medicalFileDTO.setLastName((String) medFileHashMap.get("lastName"));
        medicalFileDTO.setAge((int)(medFileHashMap.get("age")));

        List<VisitDTO> visits = (List<VisitDTO>) medFileHashMap.get("visits");
        medicalFileDTO.setVisits(visits);

        return medicalFileDTO;
    }

}
