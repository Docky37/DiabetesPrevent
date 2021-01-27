FROM adoptopenjdk:11-jre-hotspot
ADD build/libs/patient-history-manager.jar patient-history-manager.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","patient-history-manager.jar"]
