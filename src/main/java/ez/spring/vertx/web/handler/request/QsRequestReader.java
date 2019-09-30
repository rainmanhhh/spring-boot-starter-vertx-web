package ez.spring.vertx.web.handler.request;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * request reader for url querystring. eg: decode "a=1&amp;b=str" to {a:1, b:"str"}
 *
 * @param <Request> request object type
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class QsRequestReader<Request> implements RequestReader<Request> {
    private final Class<Request> requestClass;
    private final Map<Field, ValueSetter> setterMap = new HashMap<>();

    public QsRequestReader(Class<Request> requestClass) {
        this.requestClass = requestClass;
        for (Field field : requestClass.getDeclaredFields()) {
            field.setAccessible(true);
            setterMap.put(field, ValueSetter.of(field));
        }
    }

    @Override
    public Future<Request> readRequest(RoutingContext context) throws Throwable {
        Request request = requestClass.getConstructor().newInstance();
        for (Map.Entry<Field, ValueSetter> entry : setterMap.entrySet()) {
            entry.getValue().set(request, entry.getKey(), context);
        }
        return Future.succeededFuture(request);
    }

    interface ValueSetter {
        static ValueSetter of(Field field) {
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) return new StringSetter();
            else {
                if (List.class.isAssignableFrom(fieldType) || fieldType.isArray())
                    throw new IllegalArgumentException("unsupported List or Array member: " + field.getName());
                return new JsonSetter();
            }
        }

        void set(Object obj, Field field, RoutingContext context) throws Throwable;
    }

    static class StringSetter implements ValueSetter {
        @Override
        public void set(Object obj, Field field, RoutingContext context) throws Throwable {
            field.set(obj, context.queryParams().get(field.getName()));
        }
    }

    static class JsonSetter implements ValueSetter {
        @Override
        public void set(Object obj, Field field, RoutingContext context) throws Throwable {
            String strValue = context.queryParams().get(field.getName());
            if (strValue != null) {
                field.set(obj, Json.decodeValue(strValue, field.getType()));
            }
        }
    }
}
