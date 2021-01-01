package com.mediscreen.patientmanager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.patientmanager.PatientManagerApplication;
import com.mediscreen.patientmanager.controller.PatientManagerController;

@SpringBootTest(classes=PatientManagerApplication.class)
class PatientManagerApplicationTests {

    @Autowired
    PatientManagerController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

   @Test
    public void applicationContextTest() {
       PatientManagerApplication.main(new String[] {});
    }

}
