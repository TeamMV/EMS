package dev.mv.ems.parser;

import dev.mv.ems.parser.ast.*;
import dev.mv.ems.parser.lexer.Lexer;
import dev.mv.ems.parser.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private Lexer lexer;

    public Parser(String input) {
        lexer = new Lexer(input);
    }
    
    public List<Statement> parse() {
        List<Statement> stmts = new ArrayList<>();

        Token token = lexer.nextToken();
        while (token != Token.EOF) {
            lexer.revert(token);
            stmts.add(parseStatement());
            token = lexer.nextToken();
        }

        return stmts;
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
                    Operator op = next.getValueAs();
                    Expression value = parseExpression();
                    return new Assignment(name, new Binary(new Ident(name), op, value));
                }
                else if (next == Token.ASSIGN) {
                    Expression value = parseExpression();
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
                inner = lexer.nextToken();
            }
            lexer.revert(inner);

            left = new Binary(left, op, right);
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
                    return new Unary(operand, op);
                }
                throw new UnexpectedTokenException("Binary operator " + token + " without left expression");
            }
            case IDENT -> {
                String name = token.getValueAs();
                token = lexer.nextToken();
                if (token == Token.LPAREN) {
                    List<Expression> args = parseArguments();
                    return new Call(name, args);
                }
                else {
                    lexer.revert(token);
                    return new Ident(name);
                }
            }
            case LPAREN -> {
                Expression expression = parseExpression();
                token = lexer.nextToken();
                if (token != Token.RPAREN) throw new UnexpectedTokenException("Expected ')', got " + token);
                return expression;
            }
            case ILITERAL -> {
                Integer value = token.getValueAs();
                return new Literal(Type.INT, value);
            }
            case FLITERAL -> {
                Float value = token.getValueAs();
                return new Literal(Type.FLOAT, Float.floatToIntBits(value));
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
