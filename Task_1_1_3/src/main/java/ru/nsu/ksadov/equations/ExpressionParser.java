package ru.nsu.ksadov.equations;

/**
 * Простой парсер для строковых выражений вида (3+(2*x)).
 */
public class ExpressionParser {
    /**
     * Парсит строку в объект Expression.
     */
    public static Expression parse(String s) {
        s = s.trim();

        if (s.startsWith("(") && s.endsWith(")") && isBalanced(s.substring(1, s.length() - 1))) {
            s = s.substring(1, s.length() - 1).trim();
        }

        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                String leftStr = s.substring(0, i).trim();
                String rightStr = s.substring(i + 1).trim();
                Expression left = parse(leftStr);
                Expression right = parse(rightStr);
                switch (c) {
                    case '+': return new Add(left, right);
                    case '-': return new Sub(left, right);
                    case '*': return new Mul(left, right);
                    case '/': return new Div(left, right);
                }
            }
        }

        if (s.matches("-?\\d+(\\.\\d+)?")) {
            return new Number(Double.parseDouble(s));
        }

        return new Variable(s);
    }

    private static boolean isBalanced(String s) {
        int depth = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') depth++;
            else if (c == ')') depth--;
            if (depth < 0) return false;
        }
        return depth == 0;
    }
}
