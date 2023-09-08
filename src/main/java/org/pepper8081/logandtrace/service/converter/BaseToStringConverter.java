package org.pepper8081.logandtrace.service.converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseToStringConverter implements ToStringConverter {

    private final Function<Stream<?>, String> streamToString =
            s -> "[" + s.map(this::convert).collect(Collectors.joining(",")) + "]";

    @Override
    public String convert(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return ((String) o);
        }
        if (o instanceof Object[] a) {
            return streamToString.apply(Arrays.stream(a));
        } else if (o instanceof Collection<?> c) {
            return streamToString.apply(c.stream());
        } else {
            return o.toString();
        }
    }

}
