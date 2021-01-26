# DiabetesPrevent - v1.0.1 release (2021/01/27)

## Overview:
Diabetes prevent is a Mediscreen application that helps the Practitioner to make Diabetes evaluation reports.
This software is based on a microservices architecture for a Docker containers deployment.

The current 'DiabetesPrevent' GitHub repository contains backend microservices.
An Angular frontend application named 'DiabetesPrevent_UI' is available on the same 'Docky37' directory.

The backend is divided in two parts

- the first part is in charge of the personal information and contains the SpringBoot application 'PatientManager' and 
the Spring Data Rest application 'Patient' connected to a MySQL database.

- the other part manages the medical files of patients and contains the SpringBoot application 'PatientHistoryManager' 
and the Spring Data Rest application 'PatientHistory' connected to a MongoDB database.


**Here, we are in the folder of 'PatientHistoryManager' microservice.**


## Technical

- Application is build with SpringBoot v2.4 and Gradle 6.7
- The requests between PatientHistoryManager and PatientHistory are done with a WebClient interface.
- JUnit5 manages the tests and MockWebServer is used to mock the WebClient.
- Jacoco unit tests covers more than 80% of code.
- Checkstyle and SpotBugs checks have been made.
- Swagger v3.0 provide documentation.
