package ru.nsu.ksadov.equations;

import java.util.ArrayList;
import java.util.List;

/**
 * Улучшенный парсер для строковых выражений, работающий без обязательных скобок.
 * Поддерживает приоритеты операторов: * / имеют высший приоритет, затем + -
 */
public class AdvancedExpressionParser {

    /**
     * Парсит строку в объект Expression с учетом приоритетов операторов.
     */
    public static Expression parse(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        s = s.trim();

        if (!isBalanced(s)) {
            throw new IllegalArgumentException("Unbalanced parentheses in expression: " + s);
        }

        List<String> tokens = tokenize(s);
        return parseExpression(tokens);
    }

    /**
     * Разбивает строку на токены.
     */
    private static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == ' ') {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }

            if (c == '(') {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add("(");
                depth++;
            } else if (c == ')') {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(")");
                depth--;
            } else if (depth == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens;
    }

    /**
     * Парсит выражение с учетом приоритетов операторов.
     * Обрабатывает сложение и вычитание (низший приоритет).
     */
    private static Expression parseExpression(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        Expression left = parseTerm(tokens);

        // если остались токены и это + или -, продолжаем парсить
        while (!tokens.isEmpty()) {
            String operator = tokens.get(0);
            if (operator.equals("+") || operator.equals("-")) {
                tokens.remove(0); // Убираем оператор
                Expression right = parseTerm(tokens);
                if (operator.equals("+")) {
                    left = new Add(left, right);
                } else {
                    left = new Sub(left, right);
                }
            } else {
                break;
            }
        }

        return left;
    }

    /**
     * Парсит термы (умножение и деление) - высший приоритет.
     */
    private static Expression parseTerm(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Unexpected end of expression");
        }

        Expression left = parseFactor(tokens);

        // если остались токены и это * или /, продолжаем парсить
        while (!tokens.isEmpty()) {
            String operator = tokens.get(0);
            if (operator.equals("*") || operator.equals("/")) {
                tokens.remove(0); // Убираем оператор
                Expression right = parseFactor(tokens);
                if (operator.equals("*")) {
                    left = new Mul(left, right);
                } else {
                    left = new Div(left, right);
                }
            } else {
                break;
            }
        }

        return left;
    }

    /**
     * Парсит факторы (числа, переменные, выражения в скобках).
     */
    private static Expression parseFactor(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Unexpected end of expression");
        }

        String token = tokens.remove(0);

        if (token.equals("(")) {

            int depth = 1;
            int endIndex = 0;
            List<String> subTokens = new ArrayList<>();

            while (endIndex < tokens.size() && depth > 0) {
                String subToken = tokens.get(endIndex);
                if (subToken.equals("(")) depth++;
                if (subToken.equals(")")) depth--;

                if (depth > 0) {
                    subTokens.add(subToken);
                }
                endIndex++;
            }

            if (depth != 0) {
                throw new IllegalArgumentException("Unbalanced parentheses");
            }

            for (int i = 0; i < endIndex; i++) {
                tokens.remove(0);
            }

            Expression result = parseExpression(subTokens);
            return result;
        }

        if (token.matches("-?\\d+(\\.\\d+)?")) {
            try {
                return new Number(Double.parseDouble(token));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + token);
            }
        }

        if (token.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
            return new Variable(token);
        }

        throw new IllegalArgumentException("Invalid factor: " + token);
    }

    /**
     * Проверяет баланс скобок в выражении.
     */
    private static boolean isBalanced(String s) {
        int depth = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            }
            if (depth < 0) {
                return false;
            }
        }
        return depth == 0;
    }
}