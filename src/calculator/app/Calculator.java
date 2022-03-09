package calculator.app;

import calculator.util.InfixToPostFix;
import calculator.util.PostFixEvaluator;

public class Calculator {

	public static void main(String[] args) {
		System.out.printf("%n-----------Welcome to HiQ Java Calculator-----------%n"
			+ "Signs%n+ (add)%n- (subtract)%n* (multiply)%n^ (power)%n/ (divide)%n"
			+ "%% (modulus)%n%n");

		InfixToPostFix converter = new InfixToPostFix();

		@SuppressWarnings("unused")
		PostFixEvaluator evaluator = new PostFixEvaluator(converter);
	}
}
