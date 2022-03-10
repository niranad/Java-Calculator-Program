package calculator.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Reads an infix expression from user keyboard input and converts it to a
 * postfix expression. This {@code class} formats the expression for processing
 * and uses regular expression to validate the input infix. If valid, the infix
 * is converted to the corresponding postfix. An error message is generated if
 * the input infix is invalid. A custom {@link Stack} is used for the
 * conversion.
 * 
 * @author Adeniran J. Olukanni
 */
public class InfixToPostfix {
	final StringBuffer postFix;

	/**
	 * Constructs an {@code InfixToPostFix} object that reads an infix expression
	 * from the user at the keyboard and converts it to a postfix expression.
	 */
	public InfixToPostfix() {
		postFix = convertToPostfix(getInfix());
	}

	private static StringBuffer getInfix() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter an expression: ");
		StringBuffer infix = new StringBuffer(
			sc.nextLine().trim()
				.replaceAll("\\s", "")
				.replaceAll("(\\d+)(\\()", "$1*$2")
				.replaceAll("(\\))(\\d+)", "$1*$2"));
		sc.close();

		return infix;
	}

	private static StringBuffer convertToPostfix(StringBuffer infix) {
		Pattern operatorsPat = Pattern
			.compile("\\(|\\)|\\d+(\\.\\d+)?|\\*|\\+|\\/|\\%|\\-|\\^");
		Matcher matcher = operatorsPat.matcher(infix);

		Stack<String> infixStack = new Stack<>();

		while (matcher.find()) {
			infixStack.push(matcher.group());
		}

		if (isValidExpression(infix)) {
			Stack<Character> stack = new Stack<>();
			stack.push('(');

			infixStack.push(")");

			StringBuffer postFix = new StringBuffer();

			Iterator<String> iterator = infixStack.iterator();

			while (!stack.isEmpty()) {
				String next = iterator.next();
				if (next.toString().matches("\\d+(\\.\\d+)?")) {
					postFix.append(next + " ");
				} else if (Character.compare(next.charAt(0), '(') == 0) {
					stack.push(next.charAt(0));
				} else if (isOperator(next)) {
					while (isOperator(String.valueOf(stack.peek()))
						&& precedence(stack.peek()) >= precedence(next.charAt(0))) {
						postFix.append(stack.pop() + " ");
					}
					stack.push(next.charAt(0));
				} else if (Character.compare(next.charAt(0), ')') == 0) {
					while (Character.compare(stack.peek(), '(') != 0) {
						postFix.append(stack.pop() + " ");
					}
					stack.pop();
				}
			}

			postFix.trimToSize();

			return postFix;
		} else {
			System.err.printf("%nInvalid Expression!!!%n");
			return null;
		}
	}

	private static boolean isValidExpression(StringBuffer exp) {
		Pattern wrongExpPat = Pattern.compile(
			"[^\\d\\s\\+\\-\\/\\*\\(\\)\\%\\^\\.]+|(\\+{2})|(\\-{2})|(\\*{2})|(\\/{2})|(\\%{2})|(\\^{2})|(\\.{2})");
		Matcher invalidExpMatcher = wrongExpPat.matcher(exp);
		wrongExpPat = Pattern.compile("\\(|\\)");
		Matcher bracketsMatcher = wrongExpPat.matcher(exp);

		if (invalidExpMatcher.find()) {
			return false;
		} else if (bracketsMatcher.find()) {
			String brackets = exp.toString().replaceAll("[^\\(\\)]", "");
			String[] bracketsArr = brackets.split("");
			Map<String, Long> bracketsCount = Arrays.stream(bracketsArr)
				.collect(Collectors.groupingBy(el -> el, Collectors.counting()));

			if (bracketsCount.get("(") == bracketsCount.get(")")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	static boolean isOperator(String s) {
		return s.matches("[\\+\\-\\*\\/\\%\\^]");
	}

	private static int precedence(char s) {
		switch (s) {
		case '*':
		case '/':
		case '%':
			return 1;
		case '^':
			return 2;
		default:
			return 0;
		}
	}
}
