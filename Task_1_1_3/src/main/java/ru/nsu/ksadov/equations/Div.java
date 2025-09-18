package ru.nsu.ksadov.equations;

/**
 * Операция деления (f / g).
 */
public class Div extends BinaryOperation {
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    protected double applyOperation(double left, double right) {
        if (right == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left / right;
    }

    @Override
    public Expression derivative(String var) {
        // (f/g)' = (f'*g - f*g') / (g*g)
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }
}
