package lib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import helpers.CaseUtils;

public class Json {

	public String write(Object instance) {

		if(instance == null) {
			return null;
		}

		/*
		// pattern matching java 17+
		switch(instance) {
			case Number n -> n.toString();
			case Boolean b -> b.toString();
			// ...
			default -> throw new UnsupportedOperationException("type non géré");
		}
		*/

		if(Number.class.isAssignableFrom(instance.getClass()) ||
				Boolean.class.isAssignableFrom(instance.getClass())) {
			return instance.toString();
		}
		// alternative moins verbeuse
		//if(instance instanceof Number || instance instanceof Boolean) {
		//	return instance.toString();
		//}
		if(instance instanceof String) {
			// attention, bug si la chaine contient un '"'
			// IRL il faudrait faire un _escaping_
			return "\"" + instance.toString() + "\"";
		}
		
		// tableaux
		if(instance.getClass().getComponentType() != null) {
			
			final StringBuilder buffer = new StringBuilder();
			final Object[] array = (Object[])instance;
			for(int k=0; k<array.length; k++) {
				buffer.append( write(array[k]) );
				buffer.append(',');
			}
			// supprime la dernière virgule
			if(buffer.length() > 0) buffer.setLength(buffer.length() - 1);
			return "[" + buffer.toString() + "]";
			
			// remarque : en utilisant stream() 👇 on peut sérialiser
			// les List<> de la même manière que les tableaux
			// ((List<?>)instance).stream()

			//return "[" + Arrays.stream((Object[])instance).map(e -> write(e)).collect(Collectors.joining(",")) + "]";
		}
		// pour les objets, on remonte toute la chaîne
		// d'héritage, mais on s'arrête avant la classe 
		// racine (i.e. java.lang.Object) pour éviter
		// une boucle infinie à cause de Object::getClass()
		final List<Method> getters = new ArrayList<Method>();
		Class<?> clazz = instance.getClass();
		// collecte de **tous** les getters, hérités ou non
		while(!clazz.equals(Object.class)) {
			getters.addAll(
				Arrays.stream(clazz.getDeclaredMethods())
				// on ne prend en compte que les getters
				// (i.e. les méthodes getXXX() sans argument)
				.filter(m -> m.getName().startsWith("get"))
				.filter(m -> m.getParameterCount() == 0)
				// gestion de l'absence d'annotation @JsonIgnore
				.filter(m -> m.getAnnotation(JsonIgnore.class) == null)
				// attention au doublons introduits par l'héritage
				// on ignore tout getter qui a déjà été traité
				// dans une sous-classe pour éviter ça 👇
				//  {"Prop":0.5772,"Prop":0.1.414, ...}
				// mais pas possible de filtrer sur l'annotation @Override
				// puisqu'elle a une rétention SOURCE uniquement :'(
				.filter(m -> getters.stream().filter(g -> m.getName().equals(g.getName())).findAny().isEmpty())
				.collect(Collectors.toList())
			);
			clazz = clazz.getSuperclass();
		}

		// conversion tableau de getters -> json
		return "{" + getters.stream().map(m -> {

				final JsonProperty anotation = (JsonProperty)m.getAnnotation(JsonProperty.class);
				final String jsonPropName = CaseUtils.camelCaseToSnakeCase(anotation != null ? anotation.value() : m.getName().substring(3));
				try {
					// requis pour introspection
					m.setAccessible(true);
					// appel de la méthode sur l'instance
					final Object value = m.invoke(instance);
					return "\"" + jsonPropName + "\":" + write(value);
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.joining(",")) + "}";
	}

}
