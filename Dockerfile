# Stage 1 : build the app
FROM gradle:6.7.1-jdk8-openj9 AS build-jar-stage
ENV GRADLE_USER_HOME=/home/gradle
COPY --chown=gradle:gradle src $GRADLE_USER_HOME/src
COPY --chown=gradle:gradle build.gradle gradle.properties $GRADLE_USER_HOME/
WORKDIR $GRADLE_USER_HOME
RUN gradle clean build test

# Stage 2 : create the Docker final image
FROM adoptopenjdk/openjdk8-openj9:alpine
RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build-jar-stage /home/gradle/build/libs/*.jar /app/spring-boot-vertx-app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", \
    "-Dspring.profiles.active=docker", \
    "-Dvertx.server.address=0.0.0.0", \
    "-Dvertx.server.port=8080", \
    "-jar", "spring-boot-vertx-app.jar"]
