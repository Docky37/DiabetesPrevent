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


**Here, we are in the folder of 'Patient' microservice.**


## Technical

- Application is build with SpringBoot v2.4 and Gradle 6.7
- This application only contains the model entities 'Patient' & 'PatientDetails' and a repository interface.
- Spring Data Rest is in charge to expose CRUD endpoints that will be requested by 'PatientManager' WebClient.
- The entities are mapped (with Hibernate ORM) to the patientDetails table of the MySQL database named 'patientDB'.