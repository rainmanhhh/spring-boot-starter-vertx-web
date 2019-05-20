package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.ext.web.handler.CookieHandler;

@Lazy
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".cookie-handler")
@Configuration
public class CookieHandlerConfiguration extends AbstractHandlerConfiguration {
    private String handler = CookieHandler.class.getCanonicalName();
    private Integer order = -600;

    @Override
    public String getHandler() {
        return handler;
    }

    public CookieHandlerConfiguration setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public CookieHandlerConfiguration setOrder(Integer order) {
        this.order = order;
        return this;
    }

    @Lazy
    @ConditionalOnMissingBean(CookieHandler.class)
    @Bean
    public CookieHandler cookieHandler() {
        return CookieHandler.create();
    }
}