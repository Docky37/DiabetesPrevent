FROM adoptopenjdk:11-jre-hotspot
ADD build/libs/patient-manager.jar patient-manager.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","patient-manager.jar"]
