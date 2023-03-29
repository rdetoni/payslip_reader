# payslip_reader
Microservice that read and stores payslip information. It can be used on personal financial systems where you need a 
service that stores and retrieves essential information from your payslip. It reads only PDF files (Feel free to add 
more types into it), and you'll need to apply the proper regex for your payslip model. Regexes can be placed on 
application.properties file.  

## **Setting Up**

### Creating application.properties file:
Under src/main/resources create an application.properties file:

#Data Source configuration (For MySQL database)<br>
spring.jpa.hibernate.ddl-auto=update<br>
spring.datasource.url=jdbc:mysql://localhost:PORT_NUM/DB_NAME<br>
spring.datasource.username=YOUR_USERNAME<br>
spring.datasource.password=YOUR_PASSWORD<br>
spring.datasource.driver.class-name=com.mysql.cj.jdbc.Driver<br>

#Spring boot actuator properties<br>
#management.endpoints.enabled-by-default=false<br>
#management.endpoint.info.enabled=true<br>
#management.endpoints.web.base-path=/manage<br>
management.endpoints.web.exposure.include=*<br>
management.endpoints.web.exposure.exclude=beans,caches,conditions,configprops,env,loggers<br>
management.endpoint.health.show-details=ALWAYS<br>
management.endpoint.health.group.health-group.include=ping,diskSpace<br>
management.info.env.enabled=true<br>

info.app.name=@project.name@<br>
info.app.description=@project.description@<br>
info.app.version=@project.version@<br>
info.app.java.version=@java.version@

#Regex configuration<br>
payslip.regex.dateRegex=**Your regex here**<br>
payslip.regex.totalYearRegex=**Your regex here**<br>
payslip.regex.baseSalaryRegex=**Your regex here**<br>
payslip.regex.bonusRegex=**Your regex here**<br>
payslip.regex.netSalaryRegex=**Your regex here**<br>

### Editing the docker-compose-example.yml
1. Rename the docker-compose-example.yml to docker-compose.yml<br> 
2. Replace all tags [YOUR_SOMETHING_HERE] with the information applicable to your system<br> 

### Running application
In order to get the app up and running, follow the steps below:<br>
1. Package application with maven: mvn clean install<br>
2. Go to the directory where docker-compose.yml is located<br>
3. Download all images, build them and run the containers using docker: docker-compose up --build<br>
4. Containers should be running with the application now.<br>

### Prometheus and Grafana
The app also has prometheus and grafana configured for usage. Grafana will be running on port 3000, and you can use 
payslipreader_dashboard.json file to import a pre-designed dashboard for the app.

### Endpoint: payslipReader/create
Method: **POST** <br>
Description: Creates a payslip and saves it to the database. The payslip is created from a PDF file that is uploaded as 
part of the request. A password is optional but it has to be provided to open the PDF file, in case it is 
encrypted.

Request Parameters:

    PDF file: The PDF file that the payslip will be created from. Required.
    Password: The password to open the PDF file. Optional.

Response:

    HTTP Code 201: The payslip was successfully created and saved.
    HTTP Code 500: An error occurred while processing the PDF file.
    HTTP Code 409: A payslip with the same name already exists.

### Endpoint: payslipReader/findByMonthAndYear
Method: **GET** <br>
Description: Retrieves the payslip for a given month and year. <br>

Request Parameters:

    Month: The month of the payslip to retrieve. Required.
    Year: The year of the payslip to retrieve. Required.

Response:

    HTTP Code 200: The payslip was successfully retrieved.
    HTTP Code 404: The payslip for the given month and year was not found.
    HTTP Code 422: The provided parameters are invalid.

### Other observations
 - Application was developed using JDK 18.
