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

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // оба выражения - числа
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double leftVal = ((Number) simplifiedLeft).evaluate();
            double rightVal = ((Number) simplifiedRight).evaluate();
            if (rightVal == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return new Number(leftVal / rightVal);
        }

        // числитель равен 0
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).evaluate() == 0) {
            return new Number(0);
        }

        // знаменатель равен 1
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).evaluate() == 1) {
            return simplifiedLeft;
        }

        // числитель и знаменатель одинаковы
        if (simplifiedLeft.equals(simplifiedRight)) {
            return new Number(1);
        }

        return new Div(simplifiedLeft, simplifiedRight);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }

}