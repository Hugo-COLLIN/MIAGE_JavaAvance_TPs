package test;

import lib.Json;

public class RunMeBaby {

	private static class TestCase {
		public String  getString() { return "xoxo"; }
		public Integer getInteger() { return 42; }
		public Integer getNull() { return null; }
	}

	public static void main(String[] args) {
		System.out.println(new Json().write(new TestCase()));
		// {"string":"xoxo","integer":42,"null":null}

		System.out.println(new Json().write(new TestCase()));

	}

}
