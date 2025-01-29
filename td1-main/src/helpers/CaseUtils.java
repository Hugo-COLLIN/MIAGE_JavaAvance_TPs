package helpers;

import java.util.stream.IntStream;

public class CaseUtils {

    /**
     * Convertit une chaîne <em>camel case</em> en <em>snake case</em>.
     * @param str La chaîne, peut être vide ou nulle.
     * @return La chaîne convertie.
     */
    public static String camelCaseToSnakeCase(String str) {
        if(str == null) return str;
        if(str.length() == 0) return "";

        final StringBuilder result = new StringBuilder();
        IntStream.range(0, str.length()).forEach(index -> {
            if (Character.isUpperCase(str.charAt(index)) && index > 0) {
                result.append("_");
            }
            result.append(Character.toLowerCase(str.charAt(index)));
        });
        return result.toString();
    }

    /*
    public static void main(String[] args) {
		System.out.println(CaseUtils.camelCaseToSnakeCase(null));
		System.out.println(CaseUtils.camelCaseToSnakeCase(""));
		System.out.println(CaseUtils.camelCaseToSnakeCase("camel"));
		System.out.println(CaseUtils.camelCaseToSnakeCase("camelCase"));
		System.out.println(CaseUtils.camelCaseToSnakeCase("CamelCaseIsAwesome"));
	}
    */

}
