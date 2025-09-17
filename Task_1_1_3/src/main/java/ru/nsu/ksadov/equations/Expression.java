package ru.nsu.ksadov.equations;

import java.util.Map;
import java.util.HashMap;

/**
 * Абстрактный класс для математического выражения.
 * Поддерживает вычисление, дифференцирование и печать.
 */
public abstract class Expression {
    /**
     * Вычисляет значение выражения при заданных переменных.
     *
     * @param variables отображение {переменная → значение}
     * @return результат вычисления
     */
    public abstract double evaluate(Map<String, Double> variables);

    /**
     * Находит производную выражения по указанной переменной.
     *
     * @param var имя переменной
     * @return выражение — производная
     */
    public abstract Expression derivative(String var);

    /**
     * Представление выражения в виде строки.
     *
     * @return строка выражения
     */
    public abstract String toString();

    /**
     * Вычисляет значение выражения без переменных (константный случай).
     */
    public double evaluate() {
        return evaluate(Map.of());
    }

    /**
     * Вычисляет значение выражения по строке присваивания переменных.
     * Пример: "x=10; y=5"
     *
     * @param assignments строка с присваиваниями
     * @return результат вычисления
     */
    public double evaluate(String assignments) {
        Map<String, Double> vars = new HashMap<>();
        String[] parts = assignments.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] kv = part.split("=");
            if (kv.length != 2) {
                throw new IllegalArgumentException("Invalid assignment: " + part);
            }
            String var = kv[0].trim();
            double value = Double.parseDouble(kv[1].trim());
            vars.put(var, value);
        }
        return evaluate(vars);
    }
}
