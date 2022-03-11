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
	public final StringBuffer postfix;
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
		postfix = converter.postfix;

		if (postfix != null) {
			result = evaluate(postfix);
			System.out.printf("%nResult: %s",
				result != null ? format(result) : "Error: incomplete expression");
		}
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

	private BigDecimal evaluate(StringBuffer postfix) {
		Stack<StringBuffer> postfixStack = new Stack<>();
		pushPostfixToStack(postfixStack, postfix);

		Iterator<StringBuffer> iterator = postfixStack.iterator();

		postfixStack.push(new StringBuffer(")"));

		Stack<BigDecimal> stack = new Stack<>();

		while (iterator.hasNext()) {
			StringBuffer next = iterator.next();
			if (!next.toString().equals(")")) {
				if (next.toString().matches("\\d+(\\.\\d+)?")) {
					stack.push(new BigDecimal(next.toString()));
				} else {
					BigDecimal x = stack.pop();
					if (stack.isEmpty()) {
						if (!next.toString().equals("-")
							&& !next.toString().equals("+")) {
							return null;
						}
					}
					BigDecimal y = !stack.isEmpty() ? stack.pop() : BigDecimal.ZERO;
					stack.push(calculate(y, next.charAt(0), x));
				}
			}
		}

		return stack.pop();
	}

	private void pushPostfixToStack(Stack<StringBuffer> stack, StringBuffer postFix) {
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
