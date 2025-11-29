package ru.nsu.ksadov.equations;

/**
 * Операция вычитания (f - g).
 */
public class Sub extends BinaryOperation {
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

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double leftVal = ((Number) simplifiedLeft).evaluate();
            double rightVal = ((Number) simplifiedRight).evaluate();
            return new Number(leftVal - rightVal);
        }

        if (simplifiedLeft.toString().equals(simplifiedRight.toString())) {
            return new Number(0);
        }

        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).evaluate() == 0) {
            return simplifiedLeft;
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }

}
