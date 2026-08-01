<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>NotSoSimpleCalculator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7fb;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .calculator {
            background: #1f2a44;
            padding: 20px;
            border-radius: 16px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.25);
            width: 320px;
        }
        .display {
            width: 100%;
            height: 50px;
            font-size: 24px;
            text-align: right;
            border: none;
            border-radius: 8px;
            margin-bottom: 15px;
            padding: 0 10px;
            box-sizing: border-box;
        }
        .buttons {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 10px;
        }
        button {
            padding: 14px;
            font-size: 18px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            background: #e9eef8;
            color: #1d2940;
        }
        button:hover {
            background: #d7e1f4;
        }
        .operator {
            background: #ff9f43;
            color: white;
        }
        .operator:hover {
            background: #f28a00;
        }
        .equal {
            background: #2ecc71;
            color: white;
        }
        .equal:hover {
            background: #27ae60;
        }
        .clear {
            background: #e74c3c;
            color: white;
        }
        .clear:hover {
            background: #c0392b;
        }
    </style>
</head>
<body>
<div class="calculator">
    <form action="${pageContext.request.contextPath}/" method="post">
        <input class="display" type="text" name="expression" value="${display}" readonly>
        <div class="buttons">
            <button type="submit" name="btn" value="7">7</button>
            <button type="submit" name="btn" value="8">8</button>
            <button type="submit" name="btn" value="9">9</button>
            <button type="submit" name="btn" value="/" class="operator">/</button>

            <button type="submit" name="btn" value="4">4</button>
            <button type="submit" name="btn" value="5">5</button>
            <button type="submit" name="btn" value="6">6</button>
            <button type="submit" name="btn" value="*" class="operator">*</button>

            <button type="submit" name="btn" value="1">1</button>
            <button type="submit" name="btn" value="2">2</button>
            <button type="submit" name="btn" value="3">3</button>
            <button type="submit" name="btn" value="-" class="operator">-</button>

            <button type="submit" name="btn" value="0">0</button>
            <button type="submit" name="btn" value="." >.</button>
            <button type="submit" name="btn" value="=" class="equal">=</button>
            <button type="submit" name="btn" value="+" class="operator">+</button>

            <button type="submit" name="btn" value="C" class="clear">C</button>
            <button type="submit" name="btn" value="DEL">DEL</button>
        </div>
    </form>
</div>
</body>
</html>
