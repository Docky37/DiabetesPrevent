package com.mediscreen.history.manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
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
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.VisitDTO;
import com.mediscreen.history.manager.service.IMedicalFileManagerService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MedicalFileManagerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IMedicalFileManagerService medicalFileManagerService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void givenAPatientId_whenRequestMedicalFileById_thenCallServiceMethod() throws Exception {
        // GIVEN
        UUID patientId = UUID.fromString("390ef9a0-9f50-4d63-9740-c7a235115170");
        given(medicalFileManagerService.findMedicalFileById(any(UUID.class))).willReturn(new MedicalFileDTO());
        // WHEN
        mvc.perform(MockMvcRequestBuilders.get("/medicalFiles?patientId=" + patientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(medicalFileManagerService).findMedicalFileById(any(UUID.class));
    }

    @Test
    public void givenAMedicalFile_whenRequestUpdateMedicalFile_thenCallServiceMethod() throws Exception {
        // GIVEN
        UUID patientId = UUID.fromString("390ef9a0-9f50-4d63-9740-c7a235115170");
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.parse("2021-01-02T15:30"), "Doctor Mickael JONES",
                "My notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);
        given(medicalFileManagerService.updateMedicalFile(any(MedicalFileDTO.class))).willReturn(medicalFileDTO);

        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(medicalFileDTO));
        mvc.perform(MockMvcRequestBuilders.put("/medicalFiles?patientId=" + patientId)
                .content(mapper.writeValueAsString(medicalFileDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // THEN
        verify(medicalFileManagerService).updateMedicalFile(any(MedicalFileDTO.class));
    }

    @Test
    public void givenANewPatient_whenRequestAddMedicalFile_thenCallServiceMethod() throws Exception {
        // GIVEN
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        given(medicalFileManagerService.addMedicalFile(any(MedicalFileDTO.class))).willReturn(medicalFileDTO);

        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(medicalFileDTO));
        mvc.perform(MockMvcRequestBuilders.post("/medicalFiles")
                .content(mapper.writeValueAsString(medicalFileDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // THEN
        verify(medicalFileManagerService).addMedicalFile(any(MedicalFileDTO.class));
    }

    @Test
    public void givenAPatient_whenRequestAddNoteToMedicalFile_thenCallServiceMethods() throws Exception {
        UUID patientId = UUID.fromString("390ef9a0-9f50-4d63-9740-c7a235115170");
        VisitDTO visitDTO = new VisitDTO(LocalDateTime.parse("2021-01-02T15:30"), "Doctor Mickael JONES",
                "My notes about your health.");
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        medicalFileDTO.setPatientId("390ef9a0-9f50-4d63-9740-c7a235115170");
        medicalFileDTO.setFirstName("John");
        medicalFileDTO.setLastName("DOE");
        medicalFileDTO.setAge(52);
        medicalFileDTO.addVisit(visitDTO);
        given(medicalFileManagerService.findMedicalFileById(any(UUID.class))).willReturn(new MedicalFileDTO());
        given(medicalFileManagerService.updateMedicalFile(any(MedicalFileDTO.class))).willReturn(medicalFileDTO);
        given(medicalFileManagerService.addNoteToMedicalFile(patientId, visitDTO))
                .willReturn(medicalFileDTO);

        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(visitDTO));
        mvc.perform(MockMvcRequestBuilders.post("/medicalFiles/visits?patientId=" + patientId)
                .content(mapper.writeValueAsString(visitDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // THEN
        verify(medicalFileManagerService).addNoteToMedicalFile(patientId, visitDTO);
        verify(medicalFileManagerService).findMedicalFileById(any(UUID.class));
        verify(medicalFileManagerService).updateMedicalFile(any(MedicalFileDTO.class));
    }
}
