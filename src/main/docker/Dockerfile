FROM openjdk:8-jre-alpine
MAINTAINER Paul Vorbach <paul@vorba.ch>

VOLUME /tmp
COPY /target/@project.build.finalName@.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
