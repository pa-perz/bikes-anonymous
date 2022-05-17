FROM openjdk:17-jdk-alpine
LABEL maintainer="pa_perz@outlook.es"
VOLUME /main-app
ADD target/bikes-anonymous-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]