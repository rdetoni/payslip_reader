# payslip_reader
Microservice that read and stores payslip information

#Setting Up

#Creating application.properties file:
Under src/main/resources create an application.properties file:
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:PORT_NUM/DB_NAME
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver.class-name=com.mysql.cj.jdbc.Driver
