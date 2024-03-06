# Rest_Assured_CRUD_Operations

## Overview
In this Assignment, I have used the POM (Page Object Model) & created the diffrent classes for the Various Operations, we tend to perform in rest assured for the API testing.

## Website (refer to test)
```bash
https://gorest.co.in/
```

## Test Overview
1) I Created the "config.properties" file inside the config directory inside this i gave BASE_URL, and Token.
2) And I created "ConfigFileReader" inside the fileReader package for reading my "config.properties" file.
3) For Configuring All End-Points - I created "EndPoints.java" class inside the helper package where I gave all the End Points and use "DYNAMIC VARIABLES AS A ENDPOINT" here
4) For all random data generation, I created a 'RequestBodyBuilder.java' class in side the helper package
5) All Test Cases ( Including Positive & Negative ) - Present in "TestMain.java"
   
```bash
- pom.xml contains my all dependencies that is useful in the rest assured

```
6) For the Random Data generation I use 'Faker' class
```bash
  Faker faker = new Faker(); 
```
- and added the dependency in pom.xml
```bash
  <dependency>
    <groupId>com.github.javafaker</groupId>
    <artifactId>javafaker</artifactId>
    <version>1.0.2</version> <!-- or the latest version -->
    <scope>test</scope> <!-- specify the appropriate scope -->
  </dependency>
```
7) For the Print Operation - I use
```bash
logger.info("Starting test")
```
8) For using this logger -
#### In pom.xml (I add this dependency)
```bash
<dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.13.3</version>
 </dependency>
```
#### After that I defined Logger.js

```bash

import java.util.logging.Logger;
private static final Logger logger = Logger.getLogger(TestMain.class.getName());

```
### Created 
### How to clone the repo 
```bash
git clone https://github.com/Amanjha0008/Rest_Assured_CRUD.git
```

### How to run the Project?

1) #### In the TestMain.java file
```bash
Click the RUN (by clicking right)
```
or 

#### In the testng.xml
```bash
Click the RUN (by clicking right )
```
and 

3) #### See the Output

## Test Reports
#### For the Test report i use allureReports
##### I added the maven dependencies inside the pom.xml
```bash
  <dependency>
      <groupId>io.qameta.allure</groupId>
      <artifactId>allure-rest-assured</artifactId>
      <version>2.25.0</version>
  </dependency>
  
  <dependency>
      <groupId>io.qameta.allure</groupId>
      <artifactId>allure-testng</artifactId>
      <version>2.25.0</version>
  </dependency>
```
- After run the test it will create a "allure-results" directory.
- I added allure-reports in the .gitignore 



