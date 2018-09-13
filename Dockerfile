FROM openjdk:8-jdk-alpine

# Spring Boot creates working directories for Tomcat by default.
# So we create a temporary file on the host under "/var/lib/docker" and link it to the container under "/tmp".
VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# for Tomcat faster initialization:
# java.security.egd=file:/dev/./urandom
# https://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
