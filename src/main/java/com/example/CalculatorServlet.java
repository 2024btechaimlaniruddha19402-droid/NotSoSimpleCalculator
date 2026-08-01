package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@WebServlet("/")
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String expression = (String) session.getAttribute("expression");
        if (expression == null) {
            expression = "";
        }
        request.setAttribute("display", expression);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String button = request.getParameter("btn");
        String expression = (String) session.getAttribute("expression");
        if (expression == null) {
            expression = "";
        }

        if (button == null) {
            request.setAttribute("display", expression);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        switch (button) {
            case "C" -> {
                expression = "";
                session.setAttribute("expression", expression);
            }
            case "DEL" -> {
                if (!expression.isEmpty()) {
                    expression = expression.substring(0, expression.length() - 1);
                }
                session.setAttribute("expression", expression);
            }
            case "=" -> {
                String result = evaluateExpression(expression);
                session.setAttribute("expression", result);
            }
            default -> {
                if (isOperator(button) && expression.isEmpty()) {
                    expression = "";
                } else if (isOperator(button) && isOperator(lastChar(expression))) {
                    expression = expression.substring(0, expression.length() - 1) + button;
                } else {
                    expression += button;
                }
                session.setAttribute("expression", expression);
            }
        }

        request.setAttribute("display", session.getAttribute("expression"));
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    private Character lastChar(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.charAt(value.length() - 1);
    }

    private String evaluateExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            return "0";
        }

        List<String> tokens = tokenize(expression);
        Deque<Double> values = new ArrayDeque<>();
        Deque<String> operators = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                values.push(Double.parseDouble(token));
            } else if ("(".equals(token)) {
                operators.push(token);
            } else if (")".equals(token)) {
                while (!operators.isEmpty() && !"(".equals(operators.peek())) {
                    applyOperator(values, operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop();
                }
            } else {
                while (!operators.isEmpty() && shouldPopOperator(operators.peek(), token)) {
                    applyOperator(values, operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            applyOperator(values, operators.pop());
        }

        if (values.isEmpty()) {
            return "0";
        }

        double result = values.pop();
        if (Math.abs(result - Math.rint(result)) < 1e-9) {
            return String.valueOf((long) result);
        }
        return String.format(java.util.Locale.US, "%.10f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                current.append(ch);
            } else {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                if (ch == '(' || ch == ')') {
                    tokens.add(String.valueOf(ch));
                } else if (isOperator(String.valueOf(ch))) {
                    tokens.add(String.valueOf(ch));
                }
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }
        return tokens;
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean shouldPopOperator(String top, String incoming) {
        if (top.equals("(")) {
            return false;
        }
        int topPrecedence = precedence(top);
        int incomingPrecedence = precedence(incoming);
        return topPrecedence >= incomingPrecedence;
    }

    private int precedence(String operator) {
        return switch (operator) {
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            default -> 0;
        };
    }

    private void applyOperator(Deque<Double> values, String operator) {
        if (values.size() < 2) {
            return;
        }
        double right = values.pop();
        double left = values.pop();
        double result;
        switch (operator) {
            case "+" -> result = left + right;
            case "-" -> result = left - right;
            case "*" -> result = left * right;
            case "/" -> {
                if (right == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                result = left / right;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        }
        values.push(result);
    }
}
