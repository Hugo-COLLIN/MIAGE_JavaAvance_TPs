package lib;

//import helpers.CaseUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Json {

	public String write(Object instance) {
		if (instance == null)
			return null;

		if (Number.class.isAssignableFrom(instance.getClass()))
			return instance.toString();

        switch (instance) {
            case String s -> {
                return "\"" + instance + "\"";
            }
            case Boolean b -> {
                return instance.toString();
            }
            case Object[] objects -> {
				return "["
//						+ Arrays.stream(objects)
//								.map(this::write)
//								.reduce((a, b) -> a + ", " + b)
//								.orElse("")
                        + Arrays.stream(objects)
                            .map(this::write)
                            .collect(Collectors.joining(", "))
						+ "]";
            }
            case Object o -> {
                final Method[] methods = o.getClass().getDeclaredMethods();
                return "{"
                        + Arrays.stream(methods)
                        // on ne prend que les gettes ie getxxx sans param
                                .filter(method -> method.getName().startsWith("get"))
                                .filter(method -> method.getParameterCount() == 0)
                                .map(method -> {
                                    try {
                                        return "\"" + method.getName().substring(3).toLowerCase() + "\": " + write(method.invoke(o));
//                                        method.setAccessible(true);
//                                        final Object value = method.invoke(o);
//                                        return "\"" + CaseUtils.camelCaseToSnakeCase(method.getName().substring(3)) + "\": " + write(value);
                                    } catch (Exception e) {
                                        return "";
                                    }
                                })
                                .collect(Collectors.joining(", "))
                        + "}";
            }
        }

//        throw new UnsupportedOperationException("Type de données non-géré");
	}

}
