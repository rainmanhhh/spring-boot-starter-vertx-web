package ez.spring.vertx.web.verticle;

import ez.spring.vertx.deploy.VerticleDeploy;

public class HttpServerVerticleDeploy extends VerticleDeploy {
    private boolean enabled = false;
    /**
     * use vertx event loop size as default value
     */
    private int instances;
    /**
     * use vertx worker pool size as default value
     */
    private int workerPoolSize;
    /**
     * use vertx maxWorkerExecuteTime(and unit) as default value
     */
    private long maxWorkerExecuteTime;

    @Override
    public long getMaxWorkerExecuteTime() {
        return maxWorkerExecuteTime;
    }

    @Override
    public HttpServerVerticleDeploy setMaxWorkerExecuteTime(long maxWorkerExecuteTime) {
        this.maxWorkerExecuteTime = maxWorkerExecuteTime;
        return this;
    }

    @Override
    public int getInstances() {
        return instances;
    }

    @Override
    public HttpServerVerticleDeploy setInstances(int instances) {
        this.instances = instances;
        return this;
    }

    @Override
    public int getWorkerPoolSize() {
        return workerPoolSize;
    }

    @Override
    public HttpServerVerticleDeploy setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public HttpServerVerticleDeploy setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
