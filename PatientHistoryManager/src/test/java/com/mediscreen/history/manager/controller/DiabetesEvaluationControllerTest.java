package com.mediscreen.history.manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
import com.mediscreen.history.manager.constants.DiabetesEvaluation;
import com.mediscreen.history.manager.dto.DiabetesEvalReportDTO;
import com.mediscreen.history.manager.dto.MedicalFileDTO;
import com.mediscreen.history.manager.dto.PatientDetailsDTO;
import com.mediscreen.history.manager.service.IDiabetesEvaluationService;;;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DiabetesEvaluationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IDiabetesEvaluationService diabetesEvaluationService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void givenAPatientMedicalFile_whenRequestEvaluateDiabetes_thenCallServiceMethod() throws Exception {
        // GIVEN
        MedicalFileDTO medicalFileDTO = new MedicalFileDTO();
        given(diabetesEvaluationService.evaluateDiabetes(any(MedicalFileDTO.class)))
                .willReturn(new DiabetesEvalReportDTO(new PatientDetailsDTO(), 3, DiabetesEvaluation.DANGER));
        // WHEN
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mvc.perform(MockMvcRequestBuilders.post("/diabetesEvaluations")
                .content(mapper.writeValueAsString(medicalFileDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // THEN
        verify(diabetesEvaluationService).evaluateDiabetes(any(MedicalFileDTO.class));
    }

}
