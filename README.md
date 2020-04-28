# agent-tech-chat

## Prerequisites

### For frontend
- Angular cli
- npm 
- NodeJS

### For backend
- java
- wildfly 11
- [gson library](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.6)
- Eclipse
## Running the application 

This application comes with the frontend already built. To access all the features in production mode follow these steps:

- Import the project into Eclipse IDE
- Set wildfly server 
- Import the Gson library (follow steps [here](https://medium.com/programmers-blockchain/importing-gson-into-eclipse-ec8cf678ad52) and [here](https://metamug.com/article/java/eclipse-gson-class-not-found.html?fbclid=IwAR2_5r2ESyQitvINdM-m8cp50n-REQMrHGSHxQ9oRqpqggLyjb35P6Sf6Vw))
- Run the application on server
- access http://localhost:8080/WAR2020/

## Running the application - developement

In order to start the backend of this applicaiton follow the steps from the previous section.
Running the frontend in developement mode requires running `npm install` followed by `npm start` from the root of the frontend (chat-agent directory). The application will be available at http://localhost:4200
