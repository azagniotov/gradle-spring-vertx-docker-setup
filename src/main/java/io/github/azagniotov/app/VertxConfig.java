package io.github.azagniotov.app;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfig {

    @Bean
    public VertxOptions vertxOptions() {
        return new VertxOptions().setPreferNativeTransport(true);
    }

    @Bean
    @Autowired
    public Vertx vertx(final VertxOptions vertxOptions) {
        return Vertx.vertx(vertxOptions);
    }
}
