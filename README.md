# payslip_reader
Microservice that read and stores payslip information. It can be used on personal financial systems where you need a 
service that stores and retrieves essential information from your payslip. It reads only PDF files (Feel free to add 
more types into it), and you'll need to apply the proper regex for your payslip model. Regexes can be placed on 
application.properties file.  

## **Setting Up**

### Creating application.yaml file:
Under src/main/resources create an application.yaml file:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    driver:
      class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/payslipreader
    username: 'root'
    password: '[YOUR_PASSWORD_HERE]'

#Spring boot actuator properties
management:
  endpoints:
    web:
      exposure:
        include: '*'
        exclude: beans,caches,conditions,configprops,env,loggers
  endpoint:
    health:
      group:
        health-group:
          include: ping,diskSpace
      show-details: ALWAYS
  info:
    env:
      enabled: 'true'
info:
  app:
    name: '@project.name@'
    java:
      version: '@java.version@'
    version: '@project.version@'
    description: '@project.description@'

#Regex configuration
payslip:
  regex:
    baseSalaryRegex: '[YOUR_REGEX_HERE]'
    netSalaryRegex: '[YOUR_REGEX_HERE]'
    totalYearRegex: '[YOUR_REGEX_HERE]'
    dateRegex: '[YOUR_REGEX_HERE]'
    bonusRegex: '[YOUR_REGEX_HERE]'
```

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
