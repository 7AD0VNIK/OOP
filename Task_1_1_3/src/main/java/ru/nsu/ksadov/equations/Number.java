package ru.nsu.ksadov.equations;

import java.util.Map;

/**
 * Числовая константа в выражении.
 */
public class Number extends Expression {
    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    @Override
    public String toString() {
        if (value == (int) value) {
            return Integer.toString((int) value);
        }
        return Double.toString(value);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }
}
