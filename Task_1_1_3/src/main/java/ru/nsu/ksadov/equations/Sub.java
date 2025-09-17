package ru.nsu.ksadov.equations;

/**
 * Операция вычитания (f - g).
 */
public class Sub extends BinaryOperations {
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "-";
    }

    @Override
    protected double applyOperation(double left, double right) {
        return left - right;
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }
}
