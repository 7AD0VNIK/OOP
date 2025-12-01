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
        if (value == (long) value) {
            return String.format("%d", (long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Number other = (Number) obj;
        return Math.abs(value - other.value) < 1e-12; // точность для double
    }
}