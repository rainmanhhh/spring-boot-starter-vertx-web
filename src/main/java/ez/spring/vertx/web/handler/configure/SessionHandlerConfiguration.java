package ez.spring.vertx.web.handler.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import ez.spring.vertx.VertxConfiguration;
import ez.spring.vertx.web.VertxWebConfiguration;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;

@Lazy
@Import({VertxConfiguration.class, VertxWebConfiguration.class})
@ConfigurationProperties(VertxWebConfiguration.PREFIX + ".session-handler")
@Configuration
public class SessionHandlerConfiguration extends AbstractHandlerConfiguration {
  private String handler = SessionHandler.class.getCanonicalName();
  private Integer order = -500;

  /**
   * @see SessionHandler#DEFAULT_SESSION_COOKIE_NAME
   */
  private String cookieName = "vertx-web.session";
  /**
   * @see SessionHandler#DEFAULT_SESSION_COOKIE_PATH
   */
  private String cookiePath = "/";
  /**
   * default value is 30 minutes
   *
   * @see SessionHandler#DEFAULT_SESSION_TIMEOUT
   */
  private long timeout = 30 * 60 * 1000;
  /**
   * @see SessionHandler#DEFAULT_NAG_HTTPS
   */
  private boolean nagHttps = true;

  private boolean httpOnly = true;
  /**
   * @see SessionHandler#DEFAULT_COOKIE_SECURE_FLAG
   */
  private boolean secureFlag = false;
  /**
   * @see SessionHandler#DEFAULT_SESSIONID_MIN_LENGTH
   */
  private int sessionIdMinLength = 16;
  @NestedConfigurationProperty
  private LocalStoreProps localStore = null;
  @NestedConfigurationProperty
  private ClusteredStoreProps clusteredStore = null;

  @Lazy
  @ConditionalOnMissingBean(SessionHandler.class)
  @Bean
  public SessionHandler sessionHandler(
    SessionStore sessionStore,
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired(required = false) AuthProvider authProvider
  ) {
    return SessionHandler.create(sessionStore)
      .setAuthProvider(authProvider)
      .setCookieHttpOnlyFlag(httpOnly)
      .setCookieSecureFlag(secureFlag)
      .setMinLength(sessionIdMinLength)
      .setNagHttps(nagHttps)
      .setSessionCookieName(cookieName)
      .setSessionCookiePath(cookiePath)
      .setSessionTimeout(timeout);
  }

  @Bean
  public SessionStore sessionStore(Vertx vertx) {
    final JsonObject config;
    if (vertx.isClustered()) {
      config = clusteredStore == null ? new JsonObject() : JsonObject.mapFrom(clusteredStore);
    } else {
      config = localStore == null ? new JsonObject() : JsonObject.mapFrom(localStore);
    }
    return SessionStore.create(vertx, config);
  }

  @Override
  public String getHandler() {
    return handler;
  }

  public SessionHandlerConfiguration setHandler(String handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public Integer getOrder() {
    return order;
  }

  @Override
  public SessionHandlerConfiguration setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public String getCookieName() {
    return cookieName;
  }

  public SessionHandlerConfiguration setCookieName(String cookieName) {
    this.cookieName = cookieName;
    return this;
  }

  public String getCookiePath() {
    return cookiePath;
  }

  public SessionHandlerConfiguration setCookiePath(String cookiePath) {
    this.cookiePath = cookiePath;
    return this;
  }

  public long getTimeout() {
    return timeout;
  }

  public SessionHandlerConfiguration setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  public boolean isNagHttps() {
    return nagHttps;
  }

  public SessionHandlerConfiguration setNagHttps(boolean nagHttps) {
    this.nagHttps = nagHttps;
    return this;
  }

  public boolean isHttpOnly() {
    return httpOnly;
  }

  public SessionHandlerConfiguration setHttpOnly(boolean httpOnly) {
    this.httpOnly = httpOnly;
    return this;
  }

  public boolean isSecureFlag() {
    return secureFlag;
  }

  public SessionHandlerConfiguration setSecureFlag(boolean secureFlag) {
    this.secureFlag = secureFlag;
    return this;
  }

  public int getSessionIdMinLength() {
    return sessionIdMinLength;
  }

  public SessionHandlerConfiguration setSessionIdMinLength(int sessionIdMinLength) {
    this.sessionIdMinLength = sessionIdMinLength;
    return this;
  }

  public LocalStoreProps getLocalStore() {
    return localStore;
  }

  public SessionHandlerConfiguration setLocalStore(LocalStoreProps localStore) {
    this.localStore = localStore;
    return this;
  }

  public ClusteredStoreProps getClusteredStore() {
    return clusteredStore;
  }

  public SessionHandlerConfiguration setClusteredStore(ClusteredStoreProps clusteredStore) {
    this.clusteredStore = clusteredStore;
    return this;
  }

  @SuppressWarnings("JavadocReference")
  private static class LocalStoreProps {
    /**
     * unit: ms; default value is 1 second
     *
     * @see io.vertx.ext.web.sstore.impl.LocalSessionStoreImpl#DEFAULT_REAPER_INTERVAL
     */
    private long reaperInterval = 1000;
    /**
     * @see io.vertx.ext.web.sstore.impl.LocalSessionStoreImpl#DEFAULT_SESSION_MAP_NAME
     */
    private String sessionMapName = "vertx-web.sessions";
  }

  @SuppressWarnings("JavadocReference")
  private static class ClusteredStoreProps {
    /**
     * unit: ms; default value is 5 seconds
     *
     * @see io.vertx.ext.web.sstore.ClusteredSessionStore#DEFAULT_RETRY_TIMEOUT
     */
    private long timeout = 5000L;
    /**
     * @see io.vertx.ext.web.sstore.impl.ClusteredSessionStoreImpl#DEFAULT_SESSION_MAP_NAME
     */
    private String sessionMapName = "vertx-web.sessions";
  }
}