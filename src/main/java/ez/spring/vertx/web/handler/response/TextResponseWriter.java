package ez.spring.vertx.web.handler.response;

import io.vertx.core.buffer.Buffer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TextResponseWriter extends AbstractResponseWriter<Object> {
    private final String charset;
    private final String contentType;

    public TextResponseWriter(Charset charset) {
        this.charset = charset.toString();
        contentType = "text/plain;charset=" + this.charset;
    }

    public TextResponseWriter() {
        this(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public Buffer encodeBody(@Nullable Object o) {
        return o == null ? Buffer.buffer() : Buffer.buffer(o.toString(), charset);
    }
}
