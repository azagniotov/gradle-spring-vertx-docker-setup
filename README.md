[![CircleCI build master branch][circleci-badge]][circleci-link]

# Gradle Spring & Vert.x Docker Application Skeleton
Basic Gradle `v6.7.1` multi-source-set project setup for Spring Boot & Vert.x application skeleton. Can be built using multi-stage Docker build.

The project skeleton helps to be up and running quickly without wasting too much time on setting up & wiring things from scratch. Also, it serves as a refresher for basic things like:

* Gradle project setup boiler plate
* Gluing Spring Boot Dependency Injection & Vert.x verticle factory
* Enabling Netty native transport (https://netty.io/wiki/native-transports.html) on BSD (OSX) and Linux through Vert.x
* Deploying on each available CPU core a dedicated Vert.x server verticle instance
* Building & running in Docker environment using a multi-stage Docker build.

## Building & Running
1. `docker build -t spring-vertx-gradle-in-docker .`
2. `docker run -p 8080:8080 spring-vertx-gradle-in-docker`
3. `curl -X GET "http://localhost:8080/ping"`

#### IMPORTANT: Docker on MacOS Caveats
If your cURLs fail with Connection refused, it means that the mapping of host machine port `8080` to the Docker container port `8080` did not work. You may need to setup port forwarding for port `8080` in `VirtualBox`: `Settings` => `Network` => `Adapter 1 (NAT)` => `Advanced` => `Port Forwarding`.

## License
MIT. See LICENSE for details

<!-- references -->

[circleci-badge]: https://circleci.com/gh/azagniotov/gradle-spring-vertx-docker-setup.svg?style=shield
[circleci-link]: https://circleci.com/gh/azagniotov/gradle-spring-vertx-docker-setup
