from openjdk:12-alpine
MAINTAINER github/ElizeusRamos
COPY ./target/spaceflightsnews-0.0.1-SNAPSHOT.jar /app/spaceflightsnews-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "spaceflightsnews-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080