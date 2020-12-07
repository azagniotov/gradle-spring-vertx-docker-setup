package io.github.azagniotov.app.verticles;

import io.github.azagniotov.app.VertxServerConfig;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed. A bean with prototype scope will
// return a different instance every time it is requested from the container. By default, Spring beans are of singleton
// scope which means the container creates a single instance of that bean.
@Scope(SCOPE_PROTOTYPE)
public class ServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private final VertxServerConfig vertxServerConfig;
    private final HttpServerOptions httpServerOptions;

    public ServerVerticle(final VertxServerConfig vertxServerConfig,
                          final HttpServerOptions httpServerOptions) {
        this.vertxServerConfig = vertxServerConfig;
        this.httpServerOptions = httpServerOptions;
    }

    @Override
    public void start(Promise<Void> promise) {
        startHttpServer().onComplete(asyncResult -> {
            if (asyncResult.succeeded()) {
                promise.complete();
            } else {
                promise.fail(asyncResult.cause());
            }
        });
    }

    private Future<Void> startHttpServer() {
        Promise<Void> promise = Promise.promise();
        HttpServer server = vertx.createHttpServer(httpServerOptions);

        Router router = Router.router(vertx);
        router.get("/ping").handler(routingContext -> routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .setStatusCode(HttpResponseStatus.OK.code())
                .end(Json.encodePrettily(new JsonObject().put("status", "pong"))));

        server.requestHandler(router)
                .listen(vertxServerConfig.getServerPort(), vertxServerConfig.getServerAddress(), asyncResult -> {
                    if (asyncResult.succeeded()) {
                        LOGGER.info(String.format("HTTP server running on %s:%s", vertxServerConfig.getServerAddress(), vertxServerConfig.getServerPort()));
                        promise.complete();

                    } else {
                        LOGGER.error("Could not start a HTTP server", asyncResult.cause());
                        promise.fail(asyncResult.cause());
                    }
                });

        return promise.future();
    }
}
