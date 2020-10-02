FROM  openjdk:8-alpine
ADD  ui/target/ui-1.0-SNAPSHOT.war ui-1.0-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT  ["java","-jar","ui-1.0-SNAPSHOT.war"]

