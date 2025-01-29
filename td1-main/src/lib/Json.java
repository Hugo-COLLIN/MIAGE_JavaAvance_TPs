package lib;

import java.util.Arrays;

public class Json {

	public String write(Object instance) {
//		if (instance instanceof Integer) // instanceof est un mot clé, un opérateur
////		if (instance.getClass().equals(obj:Integer.class))// ou utiliser .getClass
//			return instance.toString();

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
						+ Arrays.stream(objects)
								.map(this::write)
								.reduce((a, b) -> a + ", " + b)
								.orElse("")
						+ "]";
            }
            default -> {
            }
        }

        throw new UnsupportedOperationException("Type de données non-géré");
	}

}
