package ru.nsu.ksadov.equations;

/**
 * Операция умножения (f * g).
 */
public class Mul extends BinaryOperations {
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "*";
    }

    @Override
    protected double applyOperation(double left, double right) {
        return left * right;
    }

    @Override
    public Expression derivative(String var) {
        // (f*g)' = f'*g + f*g'
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }
}
