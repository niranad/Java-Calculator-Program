package calculator.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;

/**
 * Evaluates a postfix expression obtained from an {@link InfixToPostfix}
 * object, format and output the result.
 * 
 * @author Adeniran J. Olukanni
 *
 */
public class PostfixEvaluator {
	/**
	 * Result of the evaluation.
	 */
	private BigDecimal result;

	/**
	 * Constructs a {@code PostfixEvaluator} object that obtains a postfix
	 * expression from an {@code InfixToPostfix} object, format and output the
	 * result.
	 * 
	 * @param converter - an {@link InfixToPostfix} object
	 * @exception {@linkplain NullPointerException} if the postfix obtained is null
	 *                        due to an invalid infix expression read from the user.
	 */
	public PostfixEvaluator(InfixToPostfix converter) {
		result = evaluate(converter.postFix);

		System.out.printf("%nResult: %s", format(result));
	}

	private String format(BigDecimal d) {
		NumberFormat formatter = new DecimalFormat("0.0E0");
		if (d.precision() - d.scale() > 15) {
			formatter.setRoundingMode(RoundingMode.HALF_UP);
			formatter.setMinimumFractionDigits(14);
			return formatter.format(d);
		}

		formatter = NumberFormat.getNumberInstance();
		return formatter.format(d);
	}

	private BigDecimal evaluate(StringBuffer postFix) {

		Stack<StringBuffer> postFixStack = new Stack<>();
		populateStack(postFixStack, postFix);

		Iterator<StringBuffer> iterator = postFixStack.iterator();

		postFixStack.push(new StringBuffer(")"));

		Stack<BigDecimal> stack = new Stack<>();
		BigDecimal x = BigDecimal.ZERO, y = BigDecimal.ZERO;

		while (iterator.hasNext()) {
			StringBuffer next = iterator.next();
			if (next.compareTo(new StringBuffer(")")) != 0) {
				if (next.toString().matches("\\d+(\\.\\d+)?")) {
					stack.push(new BigDecimal(next.toString()));
				} else if (InfixToPostfix.isOperator(next.toString())) {
					x = stack.pop();
					if (stack.isEmpty()) {
						return x;
					}
					y = stack.pop();
					stack.push(calculate(y, next.charAt(0), x));
				}
			}
		}

		return stack.pop();
	}

	private void populateStack(Stack<StringBuffer> stack, StringBuffer postFix) {
		String[] digitsAndOps = postFix.toString().split("\\s");

		for (String digitOrOp : digitsAndOps) {
			stack.push(new StringBuffer(digitOrOp));
		}
	}

	private BigDecimal calculate(BigDecimal y, char operator, BigDecimal x) {
		switch (operator) {
		case '+':
			return y.add(x);
		case '-':
			return y.subtract(x);
		case '*':
			return y.multiply(x);
		case '/':
			try {
				return y.divide(x, RoundingMode.UNNECESSARY);
			} catch (ArithmeticException e) {
				BigDecimal result = y.divide(x, RoundingMode.HALF_UP);
				MathContext mc = new MathContext(result.precision() - result.scale() + 11,
					RoundingMode.HALF_UP);
				return y.divide(x, mc);
			}
		case '^':
			try {
				return y.pow((int) x.floatValue(), MathContext.UNLIMITED);
			} catch (ArithmeticException e) {
				System.err.printf("%nError: value too large");
			}
		case '%':
			return y.remainder(x);
		default:
			return BigDecimal.ZERO;
		}
	}
}
