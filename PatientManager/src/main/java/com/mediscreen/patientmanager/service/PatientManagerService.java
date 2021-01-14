package com.mediscreen.patientmanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;

import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;
import com.mediscreen.patientmanager.enums.Gender;
import com.mediscreen.patientmanager.exceptions.PatientNotFoundException;
import com.mediscreen.patientmanager.exceptions.UnauthorizedException;
import com.mediscreen.patientmanager.exceptions.ForbiddenException;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * This service class use a WebClient to request the API Patient.
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<PatientDTO> readResponseWithAPatientList(final String getPatientsUri)
            throws UnauthorizedException, ForbiddenException, PatientNotFoundException {
        Mono<? extends EntityModel> patientMono;
        patientMono = webClientPatientAdm.get()
                .uri(getPatientsUri)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
                        response -> Mono.just(new UnauthorizedException("401 Unauthorized")))
                .onStatus(httpStatus -> HttpStatus.FORBIDDEN.equals(httpStatus),
                        response -> Mono.just(new ForbiddenException("403 Forbidden")))
                .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        response -> Mono.just(new PatientNotFoundException("404 Not found")))
                .bodyToMono(EntityModel.of(Object.class).getClass());

        Object requestResult = Optional.ofNullable((EntityModel) patientMono.block()).orElseThrow();
        LinkedHashMap<String, LinkedHashMap<String, List<?>>> object = Optional.ofNullable(
                (LinkedHashMap<String, LinkedHashMap<String, List<?>>>) ((EntityModel) requestResult).getContent())
                .orElseThrow();

        List<LinkedHashMap<String, Object>> patientsHashMapList = (List<LinkedHashMap<String, Object>>) object
                .get("_embedded").get("patients");

        List<PatientDTO> listOfPatientDTO = new ArrayList<>();
        patientsHashMapList.forEach(p -> {
            LinkedHashMap<String, LinkedHashMap<String, String>> links =
                    (LinkedHashMap<String, LinkedHashMap<String, String>>) p
                    .get("_links");
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public PatientDetailsDTO showPersonalInfo(final UUID patientId)
            throws UnauthorizedException, ForbiddenException, PatientNotFoundException {
        Mono<? extends EntityModel> patientMono = findbyIdSubMethod(patientId);

        EntityModel requestResult = Optional.ofNullable((EntityModel) patientMono.block()).orElseThrow();
        LinkedHashMap<String, String> patientHashMap = Optional.ofNullable((LinkedHashMap<String, String>) requestResult
                .getContent()).orElseThrow();
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
     * Sub method used to make a findById request on patient with WebClient. This method is called by showPersonalInfo,
     * update and addPatient methods.
     *
     * @param patientId
     * @return a Mono<? extends EntityModel>
     */
    @SuppressWarnings("rawtypes")
    private Mono<? extends EntityModel> findbyIdSubMethod(final UUID patientId)
            throws UnauthorizedException, ForbiddenException, PatientNotFoundException {
        final String getPatientsUri = "/patients/" + patientId;
        Mono<? extends EntityModel> patientMono = webClientPatientAdm.get()
                .uri(getPatientsUri)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.UNAUTHORIZED.equals(httpStatus),
                        response -> Mono.just(new UnauthorizedException("401 Unauthorized")))
                .onStatus(httpStatus -> HttpStatus.FORBIDDEN.equals(httpStatus),
                        response -> Mono.just(new ForbiddenException("403 Forbidden")))
                .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        response -> Mono.just(new PatientNotFoundException("404 Not found")))
                .bodyToMono(EntityModel.of(Object.class).getClass());
        return patientMono;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public PatientDetailsDTO update(final UUID patientId, final PatientDetailsDTO newPatientDetails)
            throws UnauthorizedException, ForbiddenException, PatientNotFoundException {
        final String getPatientsUri = "/patients/" + patientId;

        findbyIdSubMethod(patientId);

        Mono<? extends EntityModel> patientMono = webClientPatientAdm.put()
                .uri(getPatientsUri)
                .bodyValue(newPatientDetails)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        EntityModel requestResult = Optional.ofNullable(patientMono.block()).orElseThrow();
        LinkedHashMap<String, String> patientHashMap = Optional.ofNullable((LinkedHashMap<String, String>) requestResult
                .getContent()).orElseThrow();
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public PatientDetailsDTO addPatient(final PatientDTO newPatient)
            throws UnauthorizedException, ForbiddenException {

        PatientDetailsDTO patientDetailsDTO = null;
        // TODO: Intercept 409 conflict (person already exist)

        final String getPatientsUri = "/patients";

        Mono<? extends EntityModel> patientMono = webClientPatientAdm.post()
                .uri(getPatientsUri)
                .bodyValue(newPatient)
                .accept(MediaTypes.HAL_JSON)
                .retrieve()
                .bodyToMono(EntityModel.of(Object.class).getClass());

        EntityModel<?> addedPatient = Optional.ofNullable(patientMono.block()).orElseThrow();
        LinkedHashMap<String, Object> patientHashMap = Optional
                .ofNullable((LinkedHashMap<String, Object>) addedPatient.getContent()).orElseThrow();
        log.debug("PatientDetailsDTO --> {}", patientHashMap.toString());
        patientDetailsDTO = new PatientDetailsDTO.Builder(
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
