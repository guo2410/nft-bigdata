FROM openjdk:8-jdk-slim

LABEL maintainer=guoch

COPY target/analysis-factory-0.0.1-SNAPSHOT.jar /analysis.jar

ENTRYPOINT ["java","-jar","/analysis.jar"]