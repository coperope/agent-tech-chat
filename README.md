# agent-tech-chat

## Prerequisites

### For frontend
- Angular cli
- npm 
- NodeJS

### For backend
- java
- wildfly 11
- [gson library](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.6) (jar file provided in jars directory)
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

## Communication between servers

This part of application that includes communication between master and non-master nodes has been developed (tested) using [ngrok](https://ngrok.com/). 
In order to make this communication available, one has to hard-code the URL's into the code. To do so make these adjustments to file `comunications.java`:
For master node:
- Set `master` variable to `null`
- Set `nodeName` variable to current URL provided by `ngrok`

For non-master node:
- Set `master` variable to URL provided by ngrok on master node
- Set `nodeName` variable to current URL provided by `ngrok`

These URL's have to be set without preceeding `http://`.

### Important NOTE:

The developement of server-server communication has been done using ngrok only because of lack of resources on developer's machine. 
There has been not enough disk space to install VM, so the developer has used an old PC to test server-server capabilities.

