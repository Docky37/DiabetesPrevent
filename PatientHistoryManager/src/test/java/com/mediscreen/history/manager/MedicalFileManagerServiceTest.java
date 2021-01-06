package com.mediscreen.history.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import com.mediscreen.history.manager.service.IMedicalFileManagerService;
import com.mediscreen.history.manager.service.MedicalFileManagerService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class MedicalFileManagerServiceTest {

    private static MockWebServer mockWebServerMedicalFile = new MockWebServer();

    private IMedicalFileManagerService medicalFileManagerService = new MedicalFileManagerService(WebClient
            .create(mockWebServerMedicalFile
                    .url("http://localhost:" + mockWebServerMedicalFile.getPort())
                    .toString()));

    private static String jsonResult1 = "{ \"firstName\" : \"John\", \"lastName\" : \"Doe\", \"age\" : 52,"
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
    public void givenARegistredPatient_whenFindMedicalFilById_thenReturnsPatientMedicalFile() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        mockWebServerMedicalFile.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult1));

        // WHEN
        MedicalFileDTO medicalFileDTO = medicalFileManagerService.findMedicalFileById(id);
        // THEN
        assertThat(medicalFileDTO).isNotNull();
        assertThat(medicalFileDTO.getAge()).isEqualTo(52);
    }

}
