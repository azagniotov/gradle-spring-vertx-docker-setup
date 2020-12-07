package io.github.azagniotov.app;

import io.vertx.core.http.HttpServerOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public HttpServerOptions httpServerOptions() {
        // cURL first sends "Expect: 100-continue" header before the actual POST payload and
        // waits blocking for a header response "HTTP/1.1 100 Continue". The waiting can take more than 500ms.
        //
        // https://www.w3.org/Protocols/rfc2616/rfc2616-sec8.html => 8.2.3 Use of the 100 (Continue) Status
        final HttpServerOptions httpServerOptions = new HttpServerOptions().setHandle100ContinueAutomatically(true);
        final String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("linux")) {
            return httpServerOptions.setTcpFastOpen(true)
                    .setTcpCork(true)
                    .setTcpQuickAck(true)
                    .setReusePort(true);
        } else {
            return httpServerOptions.setReusePort(true);
        }
    }
}
