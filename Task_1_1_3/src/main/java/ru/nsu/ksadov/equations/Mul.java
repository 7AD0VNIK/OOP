package ru.nsu.ksadov.equations;

/**
 * Операция умножения (f * g).
 */
public class Mul extends BinaryOperation {
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

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double leftVal = ((Number) simplifiedLeft).evaluate();
            double rightVal = ((Number) simplifiedRight).evaluate();
            return new Number(leftVal * rightVal);
        }

        if ((simplifiedLeft instanceof Number && ((Number) simplifiedLeft).evaluate() == 0) ||
                (simplifiedRight instanceof Number && ((Number) simplifiedRight).evaluate() == 0)) {
            return new Number(0);
        }

        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).evaluate() == 1) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).evaluate() == 1) {
            return simplifiedLeft;
        }

        return new Mul(simplifiedLeft, simplifiedRight);
    }
}
