package ru.nsu.ksadov.equations;

/**
 * Операция сложения (f + g).
 */
public class Add extends BinaryOperation {
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return "+";
    }

    @Override
    protected double applyOperation(double left, double right) {
        return left + right;
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }
}
