package io.github.azagniotov.app.verticles;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServerVerticleFactory implements VerticleFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public boolean blockingCreate() {
        // Usually verticle instantiation is fast but since our verticles are Spring Beans,
        // they might depend on other beans/resources which are slow to build/lookup.
        return true;
    }

    @Override
    public String prefix() {
        return "quarkus";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        // Get from Spring an instance of ServerVerticle bean with all its dependencies autowired
        return applicationContext.getBean(ServerVerticle.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
