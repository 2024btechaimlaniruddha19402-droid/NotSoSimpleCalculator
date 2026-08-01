import json
from urllib.parse import parse_qs


def evaluate_expression(expression: str) -> str:
    expression = expression.strip()
    if not expression:
        return "0"

    try:
        tokens = tokenize(expression)
        values = []
        ops = []

        for token in tokens:
            if is_number(token):
                values.append(float(token))
            elif token == '(':
                ops.append(token)
            elif token == ')':
                while ops and ops[-1] != '(':
                    apply_operator(values, ops.pop())
                ops.pop()
            else:
                while ops and should_pop_operator(ops[-1], token):
                    apply_operator(values, ops.pop())
                ops.append(token)

        while ops:
            apply_operator(values, ops.pop())

        if not values:
            return "0"

        result = values.pop()
        if abs(result - round(result)) < 1e-9:
            return str(int(round(result)))
        return str(result).rstrip('0').rstrip('.')
    except Exception:
        raise ValueError("Invalid expression")


def is_number(token: str) -> bool:
    try:
        float(token)
        return True
    except ValueError:
        return False


def tokenize(expression: str):
    tokens = []
    current = ''
    for ch in expression:
        if ch.isdigit() or ch == '.':
            current += ch
        else:
            if current:
                tokens.append(current)
                current = ''
            if ch in '()+-*/':
                tokens.append(ch)
    if current:
        tokens.append(current)
    return tokens


def precedence(op: str) -> int:
    if op in ('*', '/'):
        return 2
    if op in ('+', '-'):
        return 1
    return 0


def should_pop_operator(top: str, incoming: str) -> bool:
    if top == '(':
        return False
    return precedence(top) >= precedence(incoming)


def apply_operator(values: list, op: str) -> None:
    if len(values) < 2:
        raise ValueError("Invalid expression")
    right = values.pop()
    left = values.pop()
    if op == '+':
        values.append(left + right)
    elif op == '-':
        values.append(left - right)
    elif op == '*':
        values.append(left * right)
    elif op == '/':
        if right == 0:
            raise ValueError("Division by zero")
        values.append(left / right)
    else:
        raise ValueError("Unknown operator")


def handler(request):
    query = request.get('query', '')
    data = parse_qs(query)
    expression = data.get('expression', [''])[0]

    try:
        result = evaluate_expression(expression)
        return {
            'statusCode': 200,
            'headers': {'Content-Type': 'application/json'},
            'body': json.dumps({'result': result})
        }
    except ValueError as e:
        return {
            'statusCode': 400,
            'headers': {'Content-Type': 'application/json'},
            'body': json.dumps({'error': str(e)})
        }


app = handler
application = handler
