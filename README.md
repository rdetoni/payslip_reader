# payslip_reader
Microservice that read and stores payslip information

## **Setting Up**

### Creating application.properties file:
Under src/main/resources create an application.properties file:

#Data Source configuration (For MySQL database)<br>
spring.jpa.hibernate.ddl-auto=update<br>
spring.datasource.url=jdbc:mysql://localhost:PORT_NUM/DB_NAME<br>
spring.datasource.username=YOUR_USERNAME<br>
spring.datasource.password=YOUR_PASSWORD<br>
spring.datasource.driver.class-name=com.mysql.cj.jdbc.Driver<br>