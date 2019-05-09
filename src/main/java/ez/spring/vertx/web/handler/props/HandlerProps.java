package ez.spring.vertx.web.handler.props;

public abstract class HandlerProps {
    abstract boolean isEnabled();

    abstract void setEnabled(boolean enabled);

    /**
     * {@link Integer#MIN_VALUE} means ignore it(use auto-incremental sequence)
     */
    private int order = Integer.MIN_VALUE;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
