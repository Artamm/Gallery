FROM  openjdk:8-alpine
ADD  zk/target/zk-1.0-SNAPSHOT.war zk-1.0-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT  ["java","-jar","zk-1.0-SNAPSHOT.war"]

