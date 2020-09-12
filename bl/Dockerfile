FROM  maven:4.0-jdk-8
VOLUME /bl
ARG JAR_FILE
COPY ${JAR_FILE} bl-1.0-SNAPSHOT.jar
ENTRYPOINT["java","-jar","/bl-1.0-SNAPSHOT.jar"]