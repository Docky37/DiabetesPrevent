package com.mediscreen.patientmanager.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediscreen.patientmanager.dto.PatientDTO;
import com.mediscreen.patientmanager.dto.PatientDetailsDTO;
import com.mediscreen.patientmanager.enums.Gender;
import com.mediscreen.patientmanager.service.IPatientManagerService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PatientManagerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IPatientManagerService patientManagerService;

    private static String jsonContent = "{ \"patientId\" : \"2d53c2b2-7ba5-438e-ab7f-584b1e3644b9\","
            + " \"firstName\" : \"Jane\", \"lastName\" : \"DOE\", \"birthDate\" : \"1967-09-16\","
            + " \"gender\" : \"F\", \"phone\" : \"019 526 6114\","
            + " \"addressLine1\" : \"15 Main Street\", \"addressLine2\" : \"\", \"city\" : \"DALLAS\","
            + " \"zipCode\" : \"75001\"}";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void whenRequestAllPatients_thenCallServiceFindAllMethod() throws Exception {
        // GIVEN
        given(patientManagerService.findAll()).willReturn(new ArrayList<PatientDTO>());
        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(patientManagerService).findAll();
    }

    @Test
    public void whenRequestPatientsByName_thenCallServiceFindByLastNameMethod() throws Exception {
        // GIVEN
        given(patientManagerService.findByLastName(anyString())).willReturn(new ArrayList<PatientDTO>());
        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/patients/name?lastName=DOE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(patientManagerService).findByLastName(anyString());
    }

    @Test
    public void whenRequestPatientDetails_thenCallServiceMethod() throws Exception {
        // GIVEN
        UUID patientId = UUID.randomUUID();
        given(patientManagerService.showPersonalInfo(patientId)).willReturn(new PatientDetailsDTO());
        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/patients/id?patientId=" + patientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(patientManagerService).showPersonalInfo(patientId);
    }

    @Test
    public void whenUpdatePatientDetails_thenCallServiceMethod() throws Exception {
        // GIVEN
        UUID patientId = UUID.fromString("2d53c2b2-7ba5-438e-ab7f-584b1e3644b9");
        PatientDetailsDTO newPatientDetails = new PatientDetailsDTO.Builder(
                patientId,
                "Jane",
                "DOE",
                LocalDate.parse("1967-09-16"),
                Gender.F)
                .setPhone("019 526 6114")
                .setAddressLine1("15 Main Street")
                .setAddressLine2("")
                .setCity("DALLAS")
                .setZipCode("75001").build();

        given(patientManagerService.update(patientId, newPatientDetails)).willReturn(new PatientDetailsDTO());
        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
System.out.println(mapper.writeValueAsString(newPatientDetails));
        mvc.perform(MockMvcRequestBuilders.put("/patients/id?patientId=" + patientId)
                .content(mapper.writeValueAsString(newPatientDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(patientManagerService).update(any(UUID.class), any(PatientDetailsDTO.class));
    }

    @Test
    public void whenAddPatient_thenCallServiceMethod() throws Exception {
        // GIVEN
        PatientDetailsDTO newPatient = new PatientDetailsDTO.Builder(
                null, "Jane", "DOE", LocalDate.parse("1967-09-16"), Gender.F).build();

        PatientDetailsDTO newPatientDetails = new PatientDetailsDTO.Builder(
                UUID.fromString("2d53c2b2-7ba5-438e-ab7f-584b1e3644b9"),
                "Jane",
                "DOE",
                LocalDate.parse("1967-09-16"),
                Gender.F)
                .setPhone("")
                .setAddressLine1("15 Main Street")
                .setAddressLine2("")
                .setCity("DALLAS")
                .setZipCode("75001").build();


        given(patientManagerService.addPatient(newPatient)).willReturn(newPatient);
        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(newPatient));
        mvc.perform(MockMvcRequestBuilders.post("/patients")
                .content(mapper.writeValueAsString(newPatient))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        // THEN
        verify(patientManagerService).addPatient(newPatient);
    }

}
