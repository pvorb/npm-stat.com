FROM openjdk:8-jdk-alpine
MAINTAINER Paul Vorbach <paul@vorba.ch>
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
