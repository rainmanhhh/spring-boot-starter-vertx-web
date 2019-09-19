package ez.spring.vertx.web.handler;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.MDC;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("WeakerAccess")
public class MDCHandler implements RoutingHandler {
    public static final String DEFAULT_MDC_KEY = "WebReq";
    private String mdcKey = DEFAULT_MDC_KEY;

    public String getMdcKey() {
        return mdcKey;
    }

    public MDCHandler setMdcKey(String mdcKey) {
        this.mdcKey = mdcKey;
        return this;
    }

    @Override
    public Future<?> exec(RoutingContext event) {
        String key = getMdcKey();
        String value = event.get(key);
        if (value == null) value = createMDCValue();
        MDC.put(key, value);
        return Future.succeededFuture();
    }

    /**
     * @return new mdc trace id value created by {@link ThreadLocalRandom}
     */
    public String createMDCValue() {
        return String.valueOf(ThreadLocalRandom.current().nextDouble()).substring(2);
    }
}
