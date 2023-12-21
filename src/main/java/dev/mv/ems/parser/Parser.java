package dev.mv.ems.parser;

import dev.mv.ems.parser.ast.*;
import dev.mv.ems.parser.lexer.Lexer;
import dev.mv.ems.parser.lexer.Token;
import dev.mv.ems.runtime.Program;
import dev.mv.ems.runtime.Variable;
import dev.mv.utils.generic.triplet.Triplet;

import java.util.*;

public class Parser {

    public static final Triplet<String, Type, Long>[] CONSTANTS = new Triplet[] {
            new Triplet<>("PI", Type.FLOAT, Double.doubleToLongBits(Math.PI)),
            new Triplet<>("E", Type.FLOAT, Double.doubleToLongBits(Math.E))
    };

    private Lexer lexer;
    public Map<String, Type> startingVariables = new HashMap<>();
    public Map<String, Type> variables = new HashMap<>();
    private Map<String, Expression> needResolving = new HashMap<>();
    private List<Call> calls = new ArrayList<>();

    public Parser(String input) {
        lexer = new Lexer(input);
    }
    
    public Program parse() {
        List<Statement> stmts = new ArrayList<>();

        Token token = lexer.nextToken();
        while (token != Token.EOF) {
            lexer.revert(token);
            stmts.add(parseStatement());
            token = lexer.nextToken();
        }

        for (String name : needResolving.keySet()) {
            Expression expr = needResolving.get(name);

            Type type = expr.inferType(variables);
            if (type == Type.UNKNOWN) {
                type = Type.INT;
            }

            startingVariables.put(name, type);
            if (variables.get(name) == Type.UNKNOWN) {
                variables.put(name, type);
            }
        }

        Map<String, Variable> vars = new HashMap<>();

        for (String name : variables.keySet()) {
            if (vars.containsKey(name)) continue;
            if (Arrays.stream(CONSTANTS).anyMatch(constant -> constant.a.equals(name))) continue;
            Type start = startingVariables.get(name);
            Type end = variables.get(name);

            Variable var = createVariable(name, start, end);
            variables.put(name, var.type);
            vars.put(name, var);
        }

        for (Call call : calls) {
            call.checkTypes(variables);
        }

        return new Program(stmts, vars);
    }

    private Variable createVariable(String name, Type start, Type end) {
        Type fType = start;

        if (start != end && end != Type.UNKNOWN) {
            if (start == Type.UNKNOWN) fType = end;
            else if (end == Type.FLOAT && start == Type.INT) fType = end;
            else if (end == Type.BOOL ^ start == Type.BOOL) throw new RuntimeException("Type error: bool cannot be directly cast to int or float!");
        }
        else if (start == Type.UNKNOWN) {
            fType = Type.INT;
        }

        Variable var = new Variable(name, fType);
        return var;
    }

    private Statement parseStatement() {
        Token token = lexer.nextToken();
        switch (token) {
            case DO -> {
                List<Statement> stmts = new ArrayList<>();
                token = lexer.nextToken();
                while (token != Token.END) {
                    lexer.revert(token);
                    stmts.add(parseStatement());
                    token = lexer.nextToken();
                }
                return new Block(stmts);
            }
            case IF -> {
                Expression cond = parseExpression();
                Statement body = parseStatement();
                token = lexer.nextToken();
                if (token == Token.ELSE) {
                    Statement else_body = parseStatement();
                    return new If(cond, body, else_body);
                }
                lexer.revert(token);
                return new If(cond, body);
            }
            case WHILE -> {
                Expression cond = parseExpression();
                Statement body = parseStatement();
                return new While(cond, body);
            }
            case FOR -> {
                token = lexer.nextToken();
                if (token != Token.IDENT) throw new UnexpectedTokenException("Expected identifier after \"for\", got " + token);
                String ident = token.getValueAs();
                token = lexer.nextToken();
                if (token != Token.FROM) throw new UnexpectedTokenException("Expected \"from\", got " + token);
                Expression begin = parseExpression();
                token = lexer.nextToken();
                if (token != Token.TO) throw new UnexpectedTokenException("Expected \"to\", got " + token);
                Expression end = parseExpression();
                Statement body = parseStatement();
                return new For(ident, begin, end, body);
            }
            case CONTINUE -> {
                return new Continue();
            }
            case BREAK -> {
                return new Break();
            }
            case IDENT -> {
                String name = token.getValueAs();
                Token next = lexer.nextToken();
                if (next == Token.OPERATOR_ASSIGN) {
                    if (Arrays.stream(CONSTANTS).anyMatch(constant -> constant.a.equals(name))) throw new RuntimeException(name + " is a constant and cannot be changed!");
                    if (name.equals("index")) throw new RuntimeException("\"index\" is a reserved variable which cannot be modified");
                    Operator op = next.getValueAs();
                    Expression value = parseExpression();
                    Type type = value.inferType(variables);
                    if (!startingVariables.containsKey(name) || startingVariables.get(name) == Type.UNKNOWN) {
                        startingVariables.put(name, type);
                        if (type == Type.UNKNOWN) needResolving.put(name, value);
                        variables.put(name, type);
                    }
                    else if (type != Type.UNKNOWN) {
                        variables.put(name, type);
                    }
                    return new Assignment(name, new Binary(new Ident(name), op, value));
                }
                else if (next == Token.ASSIGN) {
                    if (Arrays.stream(CONSTANTS).anyMatch(constant -> constant.a.equals(name))) throw new RuntimeException(name + " is a constant and cannot be changed!");
                    if (name.equals("index")) throw new RuntimeException("\"index\" is a reserved variable which cannot be modified");
                    Expression value = parseExpression();
                    Type type = value.inferType(variables);
                    if (!startingVariables.containsKey(name)) {
                        startingVariables.put(name, type);
                        if (type == Type.UNKNOWN) needResolving.put(name, value);
                        variables.put(name, type);
                    }
                    else if (type != Type.UNKNOWN) {
                        variables.put(name, type);
                    }
                    return new Assignment(name, value);
                }
                else {
                    lexer.revert(next);
                    lexer.revert(token);
                    Expression expr = parseExpression();
                    return new ExpressionStatement(expr);
                }
            }
            default -> {
                lexer.revert(token);
                Expression expr = parseExpression();
                return new ExpressionStatement(expr);
            }
        }
    }

    private Expression parseExpression() {
        return parseExpressionWithPrecedence(0);
    }

    private Expression parseExpressionWithPrecedence(int minPrecedence) {
        Expression left = parsePrimaryExpression();
        Token token = lexer.nextToken();

        while (token == Token.OPERATOR) {
            Operator op = token.getValueAs();

            int precedence = op.getPrecedence();

            if (precedence < minPrecedence) {
                lexer.revert(token);
                return left;
            }

            Expression right = parsePrimaryExpression();

            Token inner = lexer.nextToken();

            while (inner == Token.OPERATOR) {
                Operator innerOp = inner.getValueAs();
                int innerPrecedence = innerOp.getPrecedence();

                if (innerPrecedence < precedence) {
                    lexer.revert(inner);
                    inner = lexer.nextToken();
                    break;
                }

                Expression extra = parseExpressionWithPrecedence(innerPrecedence);
                right = new Binary(right, innerOp, extra);
                if (right.collapsible()) right = right.collapse();
                inner = lexer.nextToken();
            }
            lexer.revert(inner);

            left = new Binary(left, op, right);
            if (left.collapsible()) left = left.collapse();
            token = lexer.nextToken();
        }
        lexer.revert(token);

        return left;
    }

    private Expression parsePrimaryExpression() {
        Token token = lexer.nextToken();
        switch (token) {
            case OPERATOR -> {
                Operator op = token.getValueAs();
                if (op.isUnary()) {
                    Expression operand = parseExpression();
                    Expression e = new Unary(operand, op);
                    if (e.collapsible()) return e.collapse();
                    return e;
                }
                throw new UnexpectedTokenException("Binary operator " + token + " without left expression");
            }
            case IDENT -> {
                String name = token.getValueAs();
                token = lexer.nextToken();
                if (token == Token.LPAREN) {
                    List<Expression> args = parseArguments();
                    Call call = new Call(name, args);
                    if (!call.checkTypes(variables)) calls.add(call);
                    if (call.collapsible()) return call.collapse();
                    return call;
                }
                else if (token == Token.OPERATOR) {
                    Operator op = token.getValueAs();
                    if (op == Operator.NOT) {
                        if (!startingVariables.containsKey(name)) {
                            startingVariables.put(name, Type.UNKNOWN);
                            variables.put(name, Type.UNKNOWN);
                        }
                        Expression e = new Unary(new Ident(name), Operator.FACT);
                        if (e.collapsible()) return e.collapse();
                        return e;
                    }
                }
                lexer.revert(token);
                if (!startingVariables.containsKey(name)) {
                    startingVariables.put(name, Type.UNKNOWN);
                    variables.put(name, Type.UNKNOWN);
                }
                Expression e = new Ident(name);
                if (e.collapsible()) return e.collapse();
                return e;
            }
            case LPAREN -> {
                Expression expression = parseExpression();
                token = lexer.nextToken();
                if (token != Token.RPAREN) throw new UnexpectedTokenException("Expected ')', got " + token);
                token = lexer.nextToken();
                if (token == Token.OPERATOR) {
                    Operator op = token.getValueAs();
                    if (op == Operator.NOT) {
                        Expression e = new Unary(expression, Operator.FACT);
                        if (e.collapsible()) return e.collapse();
                        return e;
                    }
                }
                lexer.revert(token);
                return expression;
            }
            case ILITERAL -> {
                Long value = token.getValueAs();
                token = lexer.nextToken();
                if (token == Token.OPERATOR) {
                    Operator op = token.getValueAs();
                    if (op == Operator.NOT) {
                        return new Literal(Type.INT, fact(value));
                    }
                }
                lexer.revert(token);
                return new Literal(Type.INT, value);
            }
            case FLITERAL -> {
                Double value = token.getValueAs();
                token = lexer.nextToken();
                if (token == Token.OPERATOR) {
                    Operator op = token.getValueAs();
                    if (op == Operator.NOT) {
                        return new Literal(Type.FLOAT, Double.doubleToLongBits(fact(value)));
                    }
                }
                lexer.revert(token);
                return new Literal(Type.FLOAT, Double.doubleToLongBits(value));
            }
            case TRUE -> {
                return new Literal(Type.BOOL, 1);
            }
            case FALSE -> {
                return new Literal(Type.BOOL, 0);
            }
            default -> throw new UnexpectedTokenException("Expected unary operator, identifier or literal, got " + token);
        }
    }

    private long fact(long input) {
        long result = 1;
        for (long i = 1; i <= input; i++) {
            result *= i;
        }
        return result;
    }

    private static final double GAMMA_R = 10.900511;

    private static final double[] GAMMA_DK = new double[]{
        2.48574089138753565546e-5,
        1.05142378581721974210,
        -3.45687097222016235469,
        4.51227709466894823700,
        -2.98285225323576655721,
        1.05639711577126713077,
        -1.95428773191645869583e-1,
        1.70970543404441224307e-2,
        -5.71926117404305781283e-4,
        4.63399473359905636708e-6,
        -2.71994908488607703910e-9
    };

    private static final double TWO_SQRT_E_OVER_PI = 1.8603827342052657173362492472666631120594218414085755;

    private double fact(double x) {
        x++;
        double s = GAMMA_DK[0];
        if (x < 0.5) {
            for (int i = 1; i < GAMMA_DK.length; i++) {
                s += GAMMA_DK[i] / ((double) i - x);
            }

            return Math.PI / Math.sin(Math.PI * x) * s * TWO_SQRT_E_OVER_PI * Math.pow((0.5 - x + GAMMA_R) / Math.E, 0.5 - x);
        }
        else {
            for (int i = 1; i < GAMMA_DK.length; i++) {
                s += GAMMA_DK[i] / (x + (double) i - 1);
            }

            return s * TWO_SQRT_E_OVER_PI * Math.pow((x - 0.5 + GAMMA_R) / Math.E, x - 0.5);
        }
    }

    private List<Expression> parseArguments() {
        List<Expression> args = new ArrayList<>();
        Token token = lexer.nextToken();
        for (;;) {
            switch (token) {
                case COMMA -> {}
                case RPAREN -> {
                    return args;
                }
                default -> {
                    lexer.revert(token);
                    args.add(parseExpression());
                }
            }
            token = lexer.nextToken();
        }
    }

}
