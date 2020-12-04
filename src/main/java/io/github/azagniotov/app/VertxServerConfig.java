package io.github.azagniotov.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VertxServerConfig {

    private final String serverAddress;
    private final Integer serverPort;

    @Autowired
    VertxServerConfig(@Value("${vertx.server.address}") final String serverAddress,
                      @Value("${vertx.server.port}") final Integer serverPort) {

        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }
}
