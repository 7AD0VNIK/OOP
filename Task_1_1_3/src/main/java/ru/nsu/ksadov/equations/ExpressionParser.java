package ru.nsu.ksadov.equations;

/**
 * Простой парсер для строковых выражений вида (3+(2*x)).
 */
public class ExpressionParser {
    /**
     * Парсит строку в объект Expression.
     *
     * @throws IllegalArgumentException если выражение некорректно.
     */
    public static Expression parse(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        s = s.trim();

        if (!isBalanced(s)){
            throw new IllegalArgumentException("Unbalanced parentheses in expression: " + s);
        }

        if (s.startsWith("(") && s.endsWith(")") && isBalanced(s.substring(1, s.length() - 1))) {
            s = s.substring(1, s.length() - 1).trim();
        }

        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (depth == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                String leftStr = s.substring(0, i).trim();
                String rightStr = s.substring(i + 1).trim();

                if (leftStr.isEmpty() || rightStr.isEmpty()) {
                    throw new IllegalArgumentException("Missing operands for operator: " + c);
                }

                Expression left = parse(leftStr);
                Expression right = parse(rightStr);
                switch (c) {
                    case '+': return new Add(left, right);
                    case '-': return new Sub(left, right);
                    case '*': return new Mul(left, right);
                    case '/': return new Div(left, right);
                    default: throw new IllegalArgumentException("Unexpected operator: " + c);
                }
            }
        }

        if (s.matches("-?\\d+(\\.\\d+)?")) {
            try {
                return new Number(Double.parseDouble(s));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + s);
            }
        }
        // Проверяем, что имя переменной корректно
        if (!s.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
            throw new IllegalArgumentException("Invalid variable name: " + s);
        }

        return new Variable(s);
    }

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
