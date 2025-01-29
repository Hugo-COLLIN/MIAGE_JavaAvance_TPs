package lib;

public class Json {

	public String write(Object instance) {
//		if (instance instanceof Integer) // instanceof est un mot clé, un opérateur
////		if (instance.getClass().equals(obj:Integer.class))// ou utiliser .getClass
//			return instance.toString();

		if (instance == null)
			return null;

		if (Number.class.isAssignableFrom(instance.getClass()))
			return instance.toString();

		throw new UnsupportedOperationException("Type de données non-géré");
	}

}
