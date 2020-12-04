# Stage 1 : build on Gradle image
FROM gradle:6.7.1-jdk8 AS build-env-stage
ENV GRADLE_USER_HOME=/home/gradle
COPY --chown=gradle:gradle src $GRADLE_USER_HOME/src
COPY --chown=gradle:gradle build.gradle gradle.properties $GRADLE_USER_HOME/
WORKDIR $GRADLE_USER_HOME
RUN gradle clean build test

# Stage 2 : create the Docker final distroless image
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/java:8
COPY --from=build-env-stage /home/gradle/build/libs/*.jar /app/spring-boot-vertx-app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=docker", \
    "-Dvertx.server.address=0.0.0.0", \
    "-Dvertx.server.port=8080", \
    "-jar", "spring-boot-vertx-app.jar"]
