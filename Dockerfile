# Stage 1 : build the app on 'openjdk8:alpine-slim' image, which is ~89mb
FROM adoptopenjdk/openjdk8:alpine-slim AS build-env-stage
RUN addgroup -S gradle && adduser -S gradle -G gradle
USER gradle:gradle
ENV GRADLE_USER_HOME=/home/gradle
COPY --chown=gradle:gradle src $GRADLE_USER_HOME/src
COPY --chown=gradle:gradle gradle $GRADLE_USER_HOME/gradle
COPY --chown=gradle:gradle build.gradle gradle.properties gradlew $GRADLE_USER_HOME/
WORKDIR $GRADLE_USER_HOME
RUN ./gradlew clean build test

# Stage 2 : create the Docker final distroless image
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/java:8
COPY --from=build-env-stage /home/gradle/build/libs/*.jar /app/spring-boot-vertx-app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", \
    "-Dspring.profiles.active=docker", \
    "-Dvertx.server.address=0.0.0.0", \
    "-Dvertx.server.port=8080", \
    "-jar", "spring-boot-vertx-app.jar"]
