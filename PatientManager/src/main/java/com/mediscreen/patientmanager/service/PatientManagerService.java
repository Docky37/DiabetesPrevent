package com.mediscreen.patientmanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;

import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;
import com.mediscreen.patientmanager.enums.Gender;
import com.mediscreen.patientmanager.exception.PatientNotFoundException;
import com.mediscreen.patientmanager.exception.UnauthorizedException;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * This service class use a WebClient to request the API PatientAdm.
 *
 * @author Thierry Schreiner
 */
@Service
@Log4j2
public class PatientManagerService implements IPatientManagerService {

    /**
     * Index of the patientId first character in _links / self / href.
     */
    private static final int ID_FIRST_CHARACTER_INDEX = 31;

    /**
     * UUID size.
     */
    private static final int UUID_SIZE = 36;

    /**
     * A PatientAdm Webclient declaration. The bean is injected by Spring with the class constructor @Autowired
     * annotation.
     */
    private WebClient webClientPatientAdm;

    /**
     * This class constructor allows Spring to inject a WebClient bean.
     *
     * @param pWebClientPatientAdm
     */
    @Autowired
    public PatientManagerService(final WebClient pWebClientPatientAdm) {
        webClientPatientAdm = pWebClientPatientAdm;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public List<PatientDTO> findAll() throws Exception {
        final String getPatientsUri = "/patients";

        List<PatientDTO> patients = readResponseWithAPatientList(getPatientsUri);

        return patients;
    }

    /**
     * Private method used to consume the HAL WebClient response of PatientAdm. After process this method return a list
     * of Patient.
     *
     * @param getPatientsUri
     * @return a List<PatientDTO>
     * @throws Exception
     */
    public List<PatientDTO> readResponseWithAPatientList(final String getPatientsUri) throws Exception {
        @SuppressWarnings("rawtypes")
        Mono<? extends EntityModel> patientMono;

        try {
            patientMono = webClientPatientAdm.get()
                    .uri(getPatientsUri)
                    .accept(MediaTypes.HAL_JSON)
                    .retrieve()
                    .bodyToMono(EntityModel.of(Object.class).getClass());

            @SuppressWarnings("unchecked")
            LinkedHashMap<String, LinkedHashMap<String, List<?>>> object =
                    (LinkedHashMap<String, LinkedHashMap<String, List<?>>>) patientMono.block().getContent();
            @SuppressWarnings("unchecked")
            List<LinkedHashMap<String, Object>> patientsHashMapList = (List<LinkedHashMap<String, Object>>) object
                    .get("_embedded").get("patients");

            List<PatientDTO> listOfPatientDTO = new ArrayList<>();
            patientsHashMapList.forEach(p -> {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, LinkedHashMap<String, String>> links =
                        (LinkedHashMap<String, LinkedHashMap<String, String>>) p.get("_links");
                UUID patientId = UUID.fromString(links.get("self").get("href").substring(ID_FIRST_CHARACTER_INDEX));

                listOfPatientDTO.add(new PatientDTO(
                        patientId,
                        (String) p.get("firstName"),
                        (String) p.get("lastName"),
                        LocalDate.parse((String) p.get("birthDate")),
                        p.get("gender").equals("M") ? Gender.M : Gender.F));
            });

            log.debug("List of PatientDTO --> {}", listOfPatientDTO.toString());
            return listOfPatientDTO;

        } catch (WebClientResponseException e) {
            switch (e.getStatusCode()) {
            case UNAUTHORIZED:
                log.error("401 - Unauthorized\n" + e);
                throw new UnauthorizedException();
            case FORBIDDEN:
                log.error("403 - Forbidden\n" + e);
                break;
            case NOT_FOUND:
                log.error("404 - Not found\n" + e);
                throw new PatientNotFoundException("message");
            default:
                log.error(e);
                throw new Exception();
            }

        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public List<PatientDTO> findByLastName(final String lastName) throws Exception {
        final String getPatientsUri = "/patients/search/findByLastName?lastName=" + lastName;
        List<PatientDTO> patients = readResponseWithAPatientList(getPatientsUri);
        if (patients.isEmpty()) {
            throw new PatientNotFoundException("message");
        } else {
            return patients;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public PatientDetailsDTO showPersonalInfo(final UUID patientId) throws Exception {
        try {
            final String getPatientsUri = "/patients/" + patientId;
            @SuppressWarnings("rawtypes")
            Mono<? extends EntityModel> patientMono = webClientPatientAdm.get()
                    .uri(getPatientsUri)
                    .accept(MediaTypes.HAL_JSON)
                    .retrieve()
                    .bodyToMono(EntityModel.of(Object.class).getClass());

            LinkedHashMap<String, String> patientHashMap = (LinkedHashMap<String, String>) patientMono.block()
                    .getContent();
            log.debug("PatientDetailsDTO --> {}", patientHashMap.toString());

            PatientDetailsDTO patientDetailsDTO = new PatientDetailsDTO.Builder(
                    patientId,
                    patientHashMap.get("firstName"),
                    patientHashMap.get("lastName"),
                    LocalDate.parse((String) patientHashMap.get("birthDate")),
                    patientHashMap.get("gender").equals("M") ? Gender.M : Gender.F)
                            .setPhone(patientHashMap.get("phone"))
                            .setAddressLine1(patientHashMap.get("addressLine1"))
                            .setAddressLine2(patientHashMap.get("addressLine2"))
                            .setCity(patientHashMap.get("city"))
                            .setZipCode(patientHashMap.get("zipCode")).build();

            log.debug("personsDTO --> {}", patientDetailsDTO.toString());
            return patientDetailsDTO;
        } catch (WebClientResponseException e) {
            switch (e.getStatusCode()) {
            case UNAUTHORIZED:
                log.error("401 - Unauthorized\n" + e);
                throw new UnauthorizedException();
            case FORBIDDEN:
                log.error("403 - Forbidden\n" + e);
                break;
            case NOT_FOUND:
                log.error("404 - Not found\n" + e);
                throw new PatientNotFoundException();
            default:
                log.error(e);
                throw new Exception();
            }

        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public PatientDetailsDTO update(final UUID patientId, final PatientDetailsDTO newPatientDetails) {
        final String getPatientsUri = "/patients/" + patientId;
        @SuppressWarnings("rawtypes")
        Mono<? extends EntityModel> patientMono = webClientPatientAdm.put()
                .uri(getPatientsUri)
                .bodyValue(newPatientDetails)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        LinkedHashMap<String, String> patientHashMap = (LinkedHashMap<String, String>) patientMono.block()
                .getContent();
        log.debug("PatientDetailsDTO --> {}", patientHashMap.toString());

        PatientDetailsDTO patientDetailsDTO = new PatientDetailsDTO.Builder(
                patientId,
                patientHashMap.get("firstName"),
                patientHashMap.get("lastName"),
                LocalDate.parse((String) patientHashMap.get("birthDate")),
                patientHashMap.get("gender").equals("M") ? Gender.M : Gender.F)
                        .setPhone(patientHashMap.get("phone"))
                        .setAddressLine1(patientHashMap.get("addressLine1"))
                        .setAddressLine2(patientHashMap.get("addressLine2"))
                        .setCity(patientHashMap.get("city"))
                        .setZipCode(patientHashMap.get("zipCode")).build();

        log.debug("personsDTO --> {}", patientDetailsDTO.toString());
        return patientDetailsDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public PatientDetailsDTO addPatient(final PatientDTO newPatient) {
        final String getPatientsUri = "/patients";
        @SuppressWarnings("rawtypes")
        Mono<? extends EntityModel> patientMono = webClientPatientAdm.post()
                .uri(getPatientsUri)
                .bodyValue(newPatient)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        EntityModel<?> addedPatient = patientMono.block();
        LinkedHashMap<String, Object> patientHashMap = (LinkedHashMap<String, Object>) addedPatient.getContent();
        log.debug("PatientDetailsDTO --> {}", patientHashMap.toString());
        PatientDetailsDTO patientDetailsDTO = new PatientDetailsDTO.Builder(
                UUID.fromString(addedPatient.getLinks().toString().substring(ID_FIRST_CHARACTER_INDEX + 1,
                        ID_FIRST_CHARACTER_INDEX + 1 + UUID_SIZE)),
                (String) patientHashMap.get("firstName"),
                (String) patientHashMap.get("lastName"),
                LocalDate.parse((String) patientHashMap.get("birthDate")),
                patientHashMap.get("gender").equals("M") ? Gender.M : Gender.F)
                        .build();

        log.debug("personsDTO --> {}", patientDetailsDTO.toString());
        return patientDetailsDTO;
    }

}
