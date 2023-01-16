#Dockerfile for payslip project
FROM openjdk

WORKDIR /payslip-reader

COPY target/payslip-0.0.1-SNAPSHOT.jar /app/payslip_reader.jar

ENTRYPOINT ["java", "-jar", "/app/payslip_reader.jar"]