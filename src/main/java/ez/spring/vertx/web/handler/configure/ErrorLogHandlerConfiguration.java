package ez.spring.vertx.web.handler.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.ActiveProfiles;
import ez.spring.vertx.web.VertxWebConfiguration;
import ez.spring.vertx.web.handler.ErrorLogHandler;
import io.netty.handler.codec.http.HttpResponseStatus;

@Lazy
@ConfigurationProperties(VertxWebConfiguration.PREFIX + "error-log-handler")
@Configuration
public class ErrorLogHandlerConfiguration extends AbstractHandlerConfiguration {
    private String handler = ErrorLogHandler.class.getCanonicalName();
    private Integer order = -700;

    /**
     * {@link io.vertx.ext.web.handler.impl.HttpStatusException}
     * with code greater then this value will be log as error
     *
     * @see HttpResponseStatus#INTERNAL_SERVER_ERROR
     */
    private int errorCode = 500;
    /**
     * {@link io.vertx.ext.web.handler.impl.HttpStatusException}
     * with code greater then this value will be log as warning
     *
     * @see HttpResponseStatus#BAD_REQUEST
     */
    private int warnCode = 400;
    private boolean showWarnStack = ActiveProfiles.getInstance().isDev();

    @Lazy
    @ConditionalOnMissingBean(ErrorLogHandler.class)
    @Bean
    public ErrorLogHandler errorLogHandler() {
        return new ErrorLogHandler()
                .setErrorCode(getErrorCode())
                .setWarnCode(getWarnCode())
                .setShowWarnStack(isShowWarnStack());
    }

    @Override
    public String getHandler() {
        return handler;
    }

    public ErrorLogHandlerConfiguration setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public ErrorLogHandlerConfiguration setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ErrorLogHandlerConfiguration setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public int getWarnCode() {
        return warnCode;
    }

    public ErrorLogHandlerConfiguration setWarnCode(int warnCode) {
        this.warnCode = warnCode;
        return this;
    }

    public boolean isShowWarnStack() {
        return showWarnStack;
    }

    public ErrorLogHandlerConfiguration setShowWarnStack(boolean showWarnStack) {
        this.showWarnStack = showWarnStack;
        return this;
    }
}
