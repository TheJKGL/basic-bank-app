# Start with a base image containing Java runtime
FROM amazoncorretto:21-alpine-jdk
# Add Maintainer Info
LABEL maintainer="malakhov747@gmail.com"
ARG JAR_FILE=target/basic-bank-application-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} basic-bank-application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/basic-bank-application.jar"]


