package test;

import lib.Json;
import lib.JsonIgnore;
import lib.JsonProperty;

@SuppressWarnings("unused")
public class RunMeBaby {

	//    _             _   _
	//   | |_____ _____| | / |
	//   | / -_) V / -_) | | |
	//   |_\___|\_/\___|_| |_|
	//

	private static class Level1 {

		private static class FirstSimpleBean {
			public String getProperty() { return "value"; }
		}
		
		private static class SecondSimpleBean {
			public String getHello() { return "world"; }
		}
		
		private static class ComplexBean {
			public float getFloat() { return 1.414f; }
			public Boolean getBoolean() { return Boolean.TRUE; }
			public String getString() { return "xoxo"; }
			public Integer getInteger() { return 42; }
			public Integer getNull() { return null; }
			public Object[] getArrayOfLiterals() { return new Object[] { null, 1337, 3.14d, Boolean.TRUE, "hello" }; }
			public Object[] getArrayOfBeans() { return new Object[] { new FirstSimpleBean(), new SecondSimpleBean() }; }
		}

	}

	//    _             _   ___ 
	//   | |_____ _____| | |_  )
	//   | / -_) V / -_) |  / /
	//   |_\___|\_/\___|_| /___|
	//

	private static class Level2 {

		private static class AnnotatedClass {
			@JsonIgnore
			public String getIgnore() { return "ignore me !"; }
			@JsonProperty("mixedArray")
			public Object[] getArrayOfBeans() { return new Object[] { 42, Boolean.TRUE, "hello"}; }
		}

	}

	//    _             _   ____
	//   | |_____ _____| | |__ /
	//   | / -_) V / -_) |  |_ \
	//   |_\___|\_/\___|_| |___/
	//

	private static class Level3 {

		private static class BaseClass {
			public Object[] getArray() { return new Object[] { Boolean.TRUE, 3.14f }; }
			public Boolean getFalse() { return true; }
		}
	
		private static class SubClass extends BaseClass {
			@Override
			public Boolean getFalse() { return false; }
		}

	}

	//    _             _   _ _
	//   | |_____ _____| | | | |
	//   | / -_) V / -_) | |_  _|
	//   |_\___|\_/\___|_|   |_|
	//  

	private static class Level4 {

		private static class BaseClass {
			public String getEscaped() { return "\\"; }
			public Boolean getFalse() { return false; }
		}
	
		private static class SubClass extends BaseClass {
			@JsonIgnore
			public Boolean getFalse() { return true; }
			public SubClass getSubClass() { return new SubClass(); }
		}

	}

	public static void main(String[] args) {
		// level 1 - gestion de tous les types demandés
		System.out.println(new Json().write(new Level1.ComplexBean()));
		// {"boolean":true,"float":1.414,"integer":42,"string":"xoxo","null":null,"array_of_literals":[null,1337,3.14,true,"hello"],"array_of_beans":[{"property":"value"},{"hello":"world"}]}

		// level 2 - gestion des annotations
		System.out.println(new Json().write(new Level2.AnnotatedClass()));
		// {"mixed_array":[42,true,"hello"]}

		// level 3 - prise en compte de l'héritage
		System.out.println(new Json().write(new Level3.SubClass()));
		// {"false":false,"array":[true,3.14]}

		// Level 4 - 💪
		//System.out.println(new Json().write(new Level4.SubClass()));
		// {"false":"false","escaped":"\\"} ou {"false":"false","escaped":"\\", "sub_class":{}}
	}

}
