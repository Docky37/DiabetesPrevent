package com.mediscreen.history.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class MedicalFileManagerServiceTest {

    private static MockWebServer mockWebServerMedicalFile = new MockWebServer();

    private IMedicalFileManagerService medicalFileManagerService = new MedicalFileManagerService(WebClient
            .create(mockWebServerMedicalFile
                    .url("http://localhost:" + mockWebServerMedicalFile.getPort())
                    .toString()));

    private static String jsonResult = "";

    private static String jsonResult0 = "{ \"firstName\" : \"John\", \"lastName\" : \"DOE\", \"age\" : 52,"
            + " \"_links\" : { \"self\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" },"
            + " \"medicalFile\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" } } }";

    private static String jsonResult1 = "{ \"firstName\" : \"John\", \"lastName\" : \"DOE\", \"age\" : 52,"
            + " \"visits\" : [ { \"visitDate\" : \"2019-12-15T10:12:00\", \"practitioner\" : \"Dr. Mickael JONES\","
            + " \"notes\" : \"Patient states that they are feeling a great deal of stress at work.\\n"
            + "Patient also complains that their hearing seems Abnormal as of late.\" }],"
            + " \"_links\" : { \"self\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" },"
            + " \"medicalFile\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" } } }";

    private static String jsonResult2 = "{ \"firstName\" : \"John\", \"lastName\" : \"DOE\", \"age\" : 52,"
            + " \"visits\" : [ { \"visitDate\" : \"2019-12-15T10:12:00\", \"practitioner\" : \"Dr. Mickael JONES\","
            + " \"notes\" : \"Patient states that they are feeling a great deal of stress at work.\\n"
            + "Patient also complains that their hearing seems Abnormal as of late.\" },"
            + " { \"visitDate\" : \"2020-02-25T15:15:00\", \"practitioner\" : \"Dr. Mickael JONES\", \"notes\" :"
            + " \"Patient states that they have had a Reaction to medication within last 3 months.\\n"
            + "Patient also complains that their hearing continues to be problematic\" } ],"
            + " \"_links\" : { \"self\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" },"
            + " \"medicalFile\" : { \"href\" : \"http://127.0.0.1:8080/medicalFiles/390ef9a0-9f50-4d63-9740-c7a235115170\" } } }";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServerMedicalFile.start();
        System.out.println("\n\n -> Port for Server: " + mockWebServerMedicalFile.getPort() + "\n");
    }

    @Test
    @DisplayName("Given a registred patient, when call findMedicalFileById, then returns patient's medical file.")
    public void givenARegistredPatient_whenFindMedicalFindById_thenReturnsPatientMedicalFile() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult2));

        // WHEN
        MedicalFileDTO medicalFileDTO = medicalFileManagerService.findMedicalFileById(id);
        // THEN
        assertThat(medicalFileDTO).isNotNull();
        assertThat(medicalFileDTO.getAge()).isEqualTo(52);
        assertThat(medicalFileDTO.getVisits().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Givena WebClient error, when call findMedicalFileById, then error is thrown.")
    public void givenAWebClientError__whenFindMedicalFindById_thenErrorIsThrown() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        // WHEN
        // THEN
        assertThrows(Exception.class, () -> medicalFileManagerService.findMedicalFileById(id));
    }

    @Test
    @DisplayName("Given a MedicalFile, when call updateMedicalFile, then returns an updated medical file.")
    public void givenAMedicalFile_whenUpdateMedicalFile_thenReturnsAnUpdatedMedicalFile() throws Exception {
        // GIVEN
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.now(), "Doctor Mickael JONES", "MY notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);

        // Mock response to findMedicalFileById (Because service first checks that Medical file exists)
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult0));
        // Mock response to the update
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult1));

        // WHEN
        MedicalFileDTO updatedMedicalFileDTO = medicalFileManagerService
                .updateMedicalFile(UUID.fromString(medicalFileDTO.getPatientId()), medicalFileDTO);
        // THEN
        assertThat(updatedMedicalFileDTO).isNotNull();
        assertThat(updatedMedicalFileDTO.getAge()).isEqualTo(52);
        assertThat(updatedMedicalFileDTO.getVisits().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Given a WebClientError, when call updateMedicalFile, then error is thrown.")
    public void givenAWebClientError_whenUpdateMedicalFile_thenErrorIsThrown() throws Exception {
        // GIVEN
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.now(), "Doctor Mickael JONES", "MY notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);

        // Mock response to findMedicalFileById (Because service first checks that Medical file exists)
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult0));

        // WHEN
        // THEN
        assertThrows(Exception.class, () -> medicalFileManagerService
                .updateMedicalFile(UUID.fromString(medicalFileDTO.getPatientId()), medicalFileDTO));
    }

    @Test
    @DisplayName("Given a new patient, when call addMedicalFile, then returns his new medical file.")
    public void givenANewPatient_whenAddMedicalFile_thenReturnsHisNewMedicalFile() throws Exception {
        // GIVEN
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);

        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(201)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult0));

        // WHEN
        MedicalFileDTO addedMedicalFileDTO = medicalFileManagerService.addMedicalFile(medicalFileDTO);
        // THEN
        assertThat(addedMedicalFileDTO).isNotNull();
        assertThat(addedMedicalFileDTO.getFirstName()).isEqualTo("John");
        assertThat(addedMedicalFileDTO.getLastName()).isEqualTo("DOE");
        assertThat(addedMedicalFileDTO.getAge()).isEqualTo(52);
    }

    @Test
    @DisplayName("Given MedicalFile alreadyexist, when call addMedicalFile, then returns existing medical file.")
    public void givenAlreadyExistingMedicalFile_whenAddMedicalFile_thenReturnsExistingMedicalFile() throws Exception {
        // GIVEN
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);

        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult0));

        // WHEN
        MedicalFileDTO addedMedicalFileDTO = medicalFileManagerService.addMedicalFile(medicalFileDTO);
        // THEN
        assertThat(addedMedicalFileDTO).isNotNull();
        assertThat(addedMedicalFileDTO.getFirstName()).isEqualTo("John");
        assertThat(addedMedicalFileDTO.getLastName()).isEqualTo("DOE");
        assertThat(addedMedicalFileDTO.getAge()).isEqualTo(52);
    }

    @Test
    @DisplayName("Given a patient, when call addNotesToMedicalFile, then returns his updated medical file.")
    public void givenAPatient_whenAddNoteToMedicalFile_thenReturnsUpdatedMedicalFile() throws Exception {
        // GIVEN
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.now(), "Doctor Mickael JONES", "My notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);

        // Mock response to findMedicalFileById (Because service first checks that Medical file exists)
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult1));
        // Mock response to the update
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult2));

        // WHEN
        MedicalFileDTO updatedMedicalFileDTO = medicalFileManagerService
                .addNoteToMedicalFile(UUID.fromString(medicalFileDTO.getPatientId()), visitDTO);
        // THEN
        assertThat(updatedMedicalFileDTO).isNotNull();
        assertThat(updatedMedicalFileDTO.getAge()).isEqualTo(52);
        assertThat(updatedMedicalFileDTO.getVisits().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Given an unknown patientId, when call addNotesToMedicalFile, then error is thrown.")
    public void givenAnUnknownPatientId_whenAddNoteToMedicalFile_thenErrorIsThrown() throws Exception {
        // GIVEN
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.now(), "Doctor Mickael JONES", "My notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);

        // Mock response to findMedicalFileById (Because service first checks that Medical file exists)
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult));

        // WHEN
        // THEN
        assertThrows(Exception.class, () -> medicalFileManagerService
                .addNoteToMedicalFile(UUID.fromString(medicalFileDTO.getPatientId()), visitDTO));
    }

}
