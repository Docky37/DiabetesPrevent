package com.mediscreen.history.manager.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;
import com.mediscreen.history.manager.exceptions.ForbiddenException;
import com.mediscreen.history.manager.exceptions.MedicalFileNotFoundException;
import com.mediscreen.history.manager.exceptions.UnauthorizedException;

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
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MedicalFileDTO findMedicalFileById(final UUID patientId)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException {
        final String getMedicalFileUri = "/medicalFiles/" + patientId.toString();
        Mono<? extends EntityModel> medFileMono;
        medFileMono = webClientMedicalFile.get()
                .uri(getMedicalFileUri)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
                        response -> Mono.just(new UnauthorizedException("401 Unauthorized")))
                .onStatus(httpStatus -> HttpStatus.FORBIDDEN.equals(httpStatus),
                        response -> Mono.just(new ForbiddenException("403 Forbidden")))
                .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        response -> Mono.just(new MedicalFileNotFoundException("404 Not found")))
                .bodyToMono(EntityModel.of(Object.class).getClass());

        Object requestResult = Optional.ofNullable((EntityModel) medFileMono.block()).orElseThrow();
        LinkedHashMap<String, Object> medFileHashMap = Optional
                .ofNullable((LinkedHashMap<String, Object>) ((EntityModel) requestResult).getContent())
                .orElseThrow();
        log.debug("MedicalFileDTO --> {}", medFileHashMap.toString());

        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();

        medicalFileDTO.setPatientId(patientId.toString());
        medicalFileDTO.setFirstName((String) medFileHashMap.get("firstName"));
        medicalFileDTO.setLastName((String) medFileHashMap.get("lastName"));
        medicalFileDTO.setAge((int) (medFileHashMap.get("age")));

        List<VisitDTO> visits = (List<VisitDTO>) medFileHashMap.get("visits");
        medicalFileDTO.setVisits(visits);

        return medicalFileDTO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws MedicalFileNotFoundException
     */
    @Override
    public MedicalFileDTO updateMedicalFile(final UUID patientId, final MedicalFileDTO medicalFileDTO)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException {

        findMedicalFileById(patientId);

        MedicalFileDTO updatedMedicalFileDTO = updateSubMethod(patientId, medicalFileDTO);

        return updatedMedicalFileDTO;

    }

    /**
     * Sub method use to make the medical file update. Used by updateMedicalFile and addNoteToMedicalFile methods.
     *
     * @param patientId
     * @param medicalFileDTO
     * @return a MedicalFileDTO
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private MedicalFileDTO updateSubMethod(final UUID patientId, final MedicalFileDTO medicalFileDTO) {
        final String getMedicalFileUri = "/medicalFiles/" + patientId;
        Mono<? extends EntityModel> medFileMono = webClientMedicalFile.put()
                .uri(getMedicalFileUri)
                .bodyValue(medicalFileDTO)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        Object requestResult = Optional.ofNullable((EntityModel) medFileMono.block()).orElseThrow();
        LinkedHashMap<String, Object> medFileHashMap = Optional
                .ofNullable((LinkedHashMap<String, Object>) ((EntityModel) requestResult).getContent())
                .orElseThrow();
        log.debug("MedicalFileDTO --> {}", medFileHashMap.toString());

        MedicalFileDTO updatedMedicalFileDTO = new MedicalFileDTO();

        updatedMedicalFileDTO.setPatientId(patientId.toString());
        updatedMedicalFileDTO.setFirstName((String) medFileHashMap.get("firstName"));
        updatedMedicalFileDTO.setLastName((String) medFileHashMap.get("lastName"));
        updatedMedicalFileDTO.setAge((int) (medFileHashMap.get("age")));

        List<VisitDTO> visits = (List<VisitDTO>) medFileHashMap.get("visits");
        updatedMedicalFileDTO.setVisits(visits);
        return updatedMedicalFileDTO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws MedicalFileNotFoundException
     * @throws ForbiddenException
     * @throws UnauthorizedException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public MedicalFileDTO addMedicalFile(final MedicalFileDTO medicalFileDTO)
            throws UnauthorizedException, ForbiddenException {

        MedicalFileDTO addedMedicalFileDTO = null;
        try {
            addedMedicalFileDTO = findMedicalFileById(UUID.fromString(medicalFileDTO.getPatientId()));
        } catch (Exception e) {
            e.printStackTrace();
            if (addedMedicalFileDTO == null) {
                final String getMedicalFileUri = "/medicalFiles/";
                Mono<? extends EntityModel> medFileMono = webClientMedicalFile.post()
                        .uri(getMedicalFileUri)
                        .bodyValue(medicalFileDTO)
                        .accept(MediaTypes.HAL_JSON)
                        .retrieve()
                        .onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
                                response -> Mono.just(new UnauthorizedException("401 Unauthorized")))
                        .onStatus(httpStatus -> HttpStatus.FORBIDDEN.equals(httpStatus),
                                response -> Mono.just(new MedicalFileNotFoundException("403 Forbidden")))
                        .bodyToMono(EntityModel.of(Object.class).getClass());

                Object requestResult = Optional.ofNullable((EntityModel) medFileMono.block()).orElseThrow();
                LinkedHashMap<String, Object> medFileHashMap = Optional
                        .ofNullable((LinkedHashMap<String, Object>) ((EntityModel) requestResult).getContent())
                        .orElseThrow();
                log.debug("MedicalFileDTO --> {}", medFileHashMap.toString());

                addedMedicalFileDTO = new MedicalFileDTO();
                addedMedicalFileDTO.setPatientId(medicalFileDTO.getPatientId());
                addedMedicalFileDTO.setFirstName((String) medFileHashMap.get("firstName"));
                addedMedicalFileDTO.setLastName((String) medFileHashMap.get("lastName"));
                addedMedicalFileDTO.setAge((int) (medFileHashMap.get("age")));
            }
        }

        return addedMedicalFileDTO;

    }

    /**
     * {@inheritDoc}
     *
     * @throws MedicalFileNotFoundException
     * @throws ForbiddenException
     * @throws UnauthorizedException
     */
    @Override
    public MedicalFileDTO addNoteToMedicalFile(final UUID patientId, final VisitDTO visitDTO)
            throws UnauthorizedException, ForbiddenException, MedicalFileNotFoundException {

        MedicalFileDTO medicalFileDTO = findMedicalFileById(patientId);

        medicalFileDTO.getVisits().add(visitDTO);

        return updateSubMethod(patientId, medicalFileDTO);
    }

}
