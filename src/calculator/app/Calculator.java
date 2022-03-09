package calculator.app;

import calculator.util.InfixToPostfix;
import calculator.util.PostfixEvaluator;

public class Calculator {

	public static void main(String[] args) {
		System.out.printf("%n-----------Welcome to HiQ Java Calculator-----------%n"
			+ "Signs%n+ (add)%n- (subtract)%n* (multiply)%n^ (power)%n/ (divide)%n"
			+ "%% (modulus)%n%n");

		InfixToPostfix converter = new InfixToPostfix();

		@SuppressWarnings("unused")
		PostfixEvaluator evaluator = new PostfixEvaluator(converter);
	}
}
