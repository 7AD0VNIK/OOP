package ru.nsu.ksadov.equations;

import java.util.Map;

/**
 * Переменная в выражении (например, x).
 */
public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable '" + name + "' not found in context");
        }
        return variables.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }
}
