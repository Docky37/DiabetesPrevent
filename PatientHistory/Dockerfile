FROM adoptopenjdk:11-jre-hotspot
ADD build/libs/patient-history.jar patient-history.jar
EXPOSE 7799
ENTRYPOINT ["java","-jar","patient-history.jar"]
