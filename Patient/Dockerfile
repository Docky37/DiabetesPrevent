FROM adoptopenjdk:11-jre-hotspot
ADD build/libs/patient.jar patient.jar
EXPOSE 7788
ENTRYPOINT ["java","-jar","patient.jar"]
