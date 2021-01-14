package com.mediscreen.patientmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;
import com.mediscreen.patientmanager.enums.Gender;
import com.mediscreen.patientmanager.service.PatientManagerService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(SpringExtension.class)
public class PatientManagerServiceTest {

    private static MockWebServer mockWebServerPatient = new MockWebServer();

    private PatientManagerService patientAdmManagerService = new PatientManagerService(WebClient
            .create(mockWebServerPatient
                    .url("http://localhost:" + mockWebServerPatient.getPort())
                    .toString()));

    private static List<PatientDTO> patients = new ArrayList<>();
    static {
        patients.add(new PatientDTO(UUID.randomUUID(), "John", "DOE", LocalDate.of(1963, 9, 16), Gender.M));
        patients.add(new PatientDTO(UUID.randomUUID(), "Jane", "DOE", LocalDate.of(1967, 9, 16), Gender.F));
        patients.add(new PatientDTO(UUID.randomUUID(), "Alan", "STEVENS", LocalDate.of(1987, 3, 29), Gender.M));
        patients.add(new PatientDTO(UUID.randomUUID(), "Jack", "RYAN", LocalDate.of(1990, 5, 24), Gender.M));
        patients.add(new PatientDTO(UUID.randomUUID(), "Jamie", "SUMMERS", LocalDate.of(1991, 11, 12), Gender.F));
    }

    private static String jsonResult5 = "{ \"_embedded\" : { \"patients\" : ["
            + " { \"firstName\" : \"John\", \"lastName\" : \"DOE\", \"birthDate\" : \"1963-09-16\","
            + " \"gender\" : \"M\", \"phone\" : \"019 526 6114\", \"addressLine1\" : \"15 Main Street\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/12f29747-85c7-485a-8c4d-4d8c4abd6000\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/12f29747-85c7-485a-8c4d-4d8c4abd6000\""
            + " } } },"
            + " { \"firstName\" : \"Alan\", \"lastName\" : \"STEVENS\", \"birthDate\" : \"1987-03-29\","
            + " \"gender\" : \"M\", \"phone\" : \"019 526 6114\", \"addressLine1\" : \"127 Jackson Street\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/32b4ed34-1ba2-49a0-92c0-a91f5928f026\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/32b4ed34-1ba2-49a0-92c0-a91f5928f026\""
            + " } } },"
            + " { \"firstName\" : \"Jaimie\", \"lastName\" : \"SOMMERS\", \"birthDate\" : \"1991-11-11\","
            + " \"gender\" : \"F\", \"phone\" : \"019 523 4568\", \"addressLine1\" : \"705 Pat Garret Avenue\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/3c99b6f7-c5f3-44e4-a056-973d295f9c61\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/3c99b6f7-c5f3-44e4-a056-973d295f9c61\""
            + " } } },"
            + " { \"firstName\" : \"Jack\", \"lastName\" : \"RYAN\", \"birthDate\" : \"1990-05-24\","
            + " \"gender\" : \"M\", \"phone\" : \"019 526 6777\", \"addressLine1\" : \"912 Billy-the-Kid Avenue\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/857e2a41-f088-4bdf-ba47-d2f31043e37e\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/857e2a41-f088-4bdf-ba47-d2f31043e37e\""
            + " } } },"
            + " { \"firstName\" : \"Jane\", \"lastName\" : \"DOE\", \"birthDate\" : \"1967-09-16\","
            + " \"gender\" : \"F\", \"phone\" : \"019 526 6114\", \"addressLine1\" : \"15 Main Street\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/8c4e6fd6-5804-4f7f-b2ca-1596ae4fd4b8\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/8c4e6fd6-5804-4f7f-b2ca-1596ae4fd4b8\""
            + " } } } ] },"
            + " \"_links\" : { \"self\" : { \"href\" : \"http://localhost:7788/patients\" },"
            + " \"profile\" : { \"href\" : \"http://localhost:7788/profile/patients\" },"
            + " \"search\" : { \"href\" : \"http://localhost:7788/patients/search\" } },"
            + " \"page\" : { \"size\" : 20, \"totalElements\" : 5, \"totalPages\" : 1, \"number\" : 0 } }";

    private static String jsonResult2 = "{ \"_embedded\" : { \"patients\" : ["
            + " { \"firstName\" : \"John\", \"lastName\" : \"DOE\", \"birthDate\" : \"1963-09-16\","
            + " \"gender\" : \"M\", \"phone\" : \"019 526 6114\", \"addressLine1\" : \"15 Main Street\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/12f29747-85c7-485a-8c4d-4d8c4abd6000\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/12f29747-85c7-485a-8c4d-4d8c4abd6000\""
            + " } } },"
            + " { \"firstName\" : \"Jane\", \"lastName\" : \"DOE\", \"birthDate\" : \"1967-09-16\","
            + " \"gender\" : \"F\", \"phone\" : \"019 526 6114\", \"addressLine1\" : \"15 Main Street\","
            + " \"addressLine2\" : null, \"city\" : \"DALLAS\", \"zipCode\" : \"75001\", \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/8c4e6fd6-5804-4f7f-b2ca-1596ae4fd4b8\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/8c4e6fd6-5804-4f7f-b2ca-1596ae4fd4b8\""
            + " } } } ] },"
            + " \"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/search/findByLastName?lastName=DOE\" }}}";

    private static String jsonResult0 = "{ \"_embedded\" : { \"patients\" : [ ] },\"_links\" : {"
            + " \"self\" : { \"href\" : \"http://localhost:7788/patients/search/findByLastName?lastName=JONES\"}}}";

    private static String jsonResultPatient = "{ \"firstName\" : \"Jane\", \"lastName\" : \"DOE\","
            + " \"birthDate\" : \"1967-09-16\", \"gender\" : \"F\", \"phone\" : \"019 526 6114\","
            + " \"addressLine1\" : \"15 Main Street\", \"addressLine2\" : \"\", \"city\" : \"DALLAS\","
            + " \"zipCode\" : \"75001\", \"_links\" : { \"self\" : {"
            + " \"href\" : \"http://localhost:7788/patients/2d53c2b2-7ba5-438e-ab7f-584b1e3644b9\" },"
            + " \"patient\" : { \"href\" : \"http://localhost:7788/patients/2d53c2b2-7ba5-438e-ab7f-584b1e3644b9\"}}}";

    private static String jsonAddedPatient = "{ \"content\": {\"firstName\":\"Jane\", \"lastName\":\"DOE\","
            + " \"birthDate\":\"1991-11-11\", \"gender\":\"F\", \"phone\": null, \"addressLine1\": null,"
            + " \"addressLine2\": null, \"city\": null, \"zipCode\": null},"
            + " \"links\": [\"http://127.0.0.1:7788/patients/7e47cb67-5f94-4467-81b1-6350aecafe93\"] }";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServerPatient.start();
        System.out.println("\n\n -> Port for Server: " + mockWebServerPatient.getPort() + "\n");
    }

    @Test
    @DisplayName("Given N registred patients, when call findAll, then returns a list of N patients.")
    public void givenNRegistredPatients_whenFindAll_thenReturnsAListOfNPatients() throws Exception {
        // GIVEN
        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult5));
        // WHEN
        List<PatientDTO> listOfPatientsDTO = patientAdmManagerService.findAll();
        // THEN
        assertThat(listOfPatientsDTO.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Given patients with 2 named 'DOE', when call findByLastName equals DOE, then returns a list of 2 patients.")
    public void givenPatientsWithTwoDoe_whenFindByLastNameEqualToDoe_thenReturnsAListOfTwoPatients() throws Exception {
        // GIVEN
        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult2));
        // WHEN
        List<PatientDTO> listOfPatientsDTO = patientAdmManagerService.findByLastName("DOE");
        // THEN
        assertThat(listOfPatientsDTO.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Given no patient named 'JONES' when call findByLastName equals 'JONES', then returns a empty list.")
    public void givenNoPatientNamedJones_whenFindByLastNameEqualToJones_thenReturnsAnEmptyList() throws Exception {
        // GIVEN
        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResult0));

        // WHEN
        // THEN
        assertThrows(Exception.class, () -> {
            patientAdmManagerService.findByLastName("JONES");
        });
    }

    @Test
    @DisplayName("Given a patient, when call showPersonalInfo, then returns the corresponding PatientDetailsDTO.")
    public void givenAPatientDTO_whenShowPersonalInfo_thenReturnsThePatientDetailsDTO() throws Exception {
        // GIVEN
        UUID patientId = UUID.fromString("2d53c2b2-7ba5-438e-ab7f-584b1e3644b9");
        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResultPatient));

        // WHEN
        PatientDetailsDTO patientDetailsDTO = patientAdmManagerService.showPersonalInfo(patientId);
        // THEN
        assertThat(patientDetailsDTO).isNotNull();
    }

    @Test
    @DisplayName("Given a patient to update when call update, then returns an updated patient.")
    public void givenAPatient_whenUpdate_thenPatientIsUpdated() throws Exception {
        // GIVEN
        PatientDetailsDTO patientDetails = new PatientDetailsDTO.Builder(
                UUID.fromString("2d53c2b2-7ba5-438e-ab7f-584b1e3644b9"),
                "Jane",
                "DOE",
                LocalDate.parse("1967-09-16"),
                Gender.F)
                        .build();
        PatientDetailsDTO newPatientDetails = new PatientDetailsDTO.Builder(
                UUID.fromString("2d53c2b2-7ba5-438e-ab7f-584b1e3644b9"),
                "Jane",
                "DOE",
                LocalDate.parse("1967-09-16"),
                Gender.F)
                        .setPhone("019 526 6114")
                        .setAddressLine1("15 Main Street")
                        .setAddressLine2("")
                        .setCity("DALLAS")
                        .setZipCode("75001").build();

        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonResultPatient));

        // WHEN
        PatientDetailsDTO updatedPatientDetailsDTO = patientAdmManagerService.update(patientDetails.getPatientId(),
                newPatientDetails);
        // THEN
        assertThat(updatedPatientDetailsDTO.getFirstName()).isEqualTo(patientDetails.getFirstName());
        assertThat(updatedPatientDetailsDTO.getLastName()).isEqualTo(patientDetails.getLastName());
        assertThat(updatedPatientDetailsDTO.getBirthDate()).isEqualTo(patientDetails.getBirthDate());
        assertThat(updatedPatientDetailsDTO.getPhone()).isEqualTo(newPatientDetails.getPhone());
        assertThat(updatedPatientDetailsDTO.getAddressLine1()).isEqualTo(newPatientDetails.getAddressLine1());
        assertThat(updatedPatientDetailsDTO.getAddressLine2()).isEqualTo(newPatientDetails.getAddressLine2());
        assertThat(updatedPatientDetailsDTO.getCity()).isEqualTo(newPatientDetails.getCity());
        assertThat(updatedPatientDetailsDTO.getZipCode()).isEqualTo(newPatientDetails.getZipCode());

    }
    
    @Test
    @DisplayName("Given a new patient when call addPatient, then returns a new patient.")
    public void givenAPatient_whenAdd_thenReturnsANewPatient() throws Exception {
        // GIVEN
        PatientDetailsDTO newPatient = new PatientDetailsDTO.Builder(
                null, "Jane", "DOE", LocalDate.parse("1991-11-11"), Gender.F).build();

        mockWebServerPatient.enqueue(
                new MockResponse()
                        .setResponseCode(201)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(jsonAddedPatient));

        // WHEN
        PatientDetailsDTO addedPatientDetails = patientAdmManagerService.addPatient(newPatient);
        // THEN
        assertThat(addedPatientDetails.getFirstName()).isEqualTo(newPatient.getFirstName());
        assertThat(addedPatientDetails.getLastName()).isEqualTo(newPatient.getLastName());
        assertThat(addedPatientDetails.getBirthDate()).isEqualTo(newPatient.getBirthDate());
        assertThat(addedPatientDetails.getGender()).isEqualTo(newPatient.getGender());
    }

}
