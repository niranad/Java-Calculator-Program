package calculator.app;

import calculator.util.InfixToPostfix;
import calculator.util.PostfixEvaluator;

public class Calculator {

	public static void main(String[] args) {
		System.out.printf("%n-----------Welcome to HiQ Basic Java Calculator-----------%n"
			+ "Signs%n+ (add)%n- (subtract)%n* (multiply)%n^ (power)%n/ (divide)%n"
			+ "%% (modulus)%n");

		InfixToPostfix converter = new InfixToPostfix();

		PostfixEvaluator calculator = new PostfixEvaluator(converter);
		
		boolean quitCalculator = calculator.postfix == null;
		
		while (!quitCalculator) {
			converter = new InfixToPostfix();
			calculator = new PostfixEvaluator(converter);
			quitCalculator = calculator.postfix == null;
		}
		
		System.out.printf("Good bye");
	}
}
