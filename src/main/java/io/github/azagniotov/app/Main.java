package io.github.azagniotov.app;

import io.github.azagniotov.app.verticles.ServerVerticle;
import io.github.azagniotov.app.verticles.ServerVerticleFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Autowired
    private Vertx vertx;

    @Autowired
    private ServerVerticleFactory serverVerticleFactory;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    public void deployServer() {

        final String serverVerticleClassName = ServerVerticle.class.getName();
        // https://groups.google.com/g/vertx/c/WjBbTbWBBqA/m/mRqfdzGrBQAJ
        final int availableProcessors = CpuCoreSensor.availableProcessors();
        LOGGER.info("There are {} available cores, will deploy {} instances of {} ",
                availableProcessors,
                availableProcessors,
                serverVerticleClassName);

        final DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(availableProcessors);

        vertx.registerVerticleFactory(serverVerticleFactory);
        vertx.deployVerticle(serverVerticleFactory.prefix() + ":" + serverVerticleClassName, deploymentOptions, (asyncResult) -> {
            if (asyncResult.succeeded()) {
                LOGGER.debug("This deployment ID is: " + asyncResult.result());
            } else {
                LOGGER.error("There was an error deploying verticle: ", asyncResult.cause());
            }
        });
    }
}
