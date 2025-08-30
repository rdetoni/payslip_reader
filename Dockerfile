#Dockerfile for payslip project
#Local environment image
#FROM openjdk

#Raspberry-PI image
FROM eclipse-temurin:17-jdk

WORKDIR /payslip-reader

COPY target/payslip-0.0.1-SNAPSHOT.jar /app/payslip_reader.jar

ENTRYPOINT ["java", "-jar", "/app/payslip_reader.jar"]