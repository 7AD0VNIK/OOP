package ru.nsu.ksadov.equations;

import java.util.Map;

/**
 * Абстрактный класс для бинарных операций (с двумя аргументами).
 */
public abstract class BinaryOperation extends Expression {
    protected final Expression left;
    protected final Expression right;

    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает символ операции (+, -, *, /).
     */
    protected abstract String getOperator();

    /**
     * Выполняет операцию над двумя числами.
     */
    protected abstract double applyOperation(double left, double right);

    @Override
    public double evaluate(Map<String, Double> variables) {
        return applyOperation(left.evaluate(variables), right.evaluate(variables));
    }

    @Override
    public String toString() {
        return "(" + left.toString() + getOperator() + right.toString() + ")";
    }

    public abstract Expression simplify();
}