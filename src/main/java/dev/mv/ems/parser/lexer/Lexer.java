package dev.mv.ems.parser.lexer;

import dev.mv.ems.parser.ast.Operator;
import dev.mv.engine.exceptions.Exceptions;

import java.util.*;

public class Lexer {
    private List<Token> reverted = new ArrayList<>();
    private char[] chars;
    private int i;

    public Lexer(String input) {
        chars = input.toCharArray();
    }

    private char next() {
        if (i >= chars.length) return '\0';
        return chars[i++];
    }

    private char peek() {
        if (i >= chars.length) return '\0';
        return chars[i];
    }

    private boolean hasNext() {
        return i < chars.length;
    }

    public void revert(Token token) {
        reverted.add(token);
    }

    public Token nextToken() {
        if (!reverted.isEmpty()) {
            return reverted.removeLast();
        }
        while (hasNext()) {
            char c = next();
            switch (c) {
                case '/' -> {
                    switch (peek()) {
                        case '/' -> {
                            while (hasNext()) {
                                if (next() == '\n') {
                                    break;
                                }
                            }
                        }
                        case '*' -> {
                            while (hasNext()) {
                                if (next() == '*') {
                                    if (peek() == '/') {
                                        next();
                                        break;
                                    }
                                }
                            }
                        }
                        case '=' -> {
                            next();
                            return Token.OPERATOR_ASSIGN.setValue(Operator.DIVIDE);
                        }
                        default -> {
                            return Token.OPERATOR.setValue(Operator.DIVIDE);
                        }
                    };
                }
                case '(' -> {
                    return Token.LPAREN;
                }
                case ')' -> {
                    return Token.RPAREN;
                }
                case '{' -> {
                    return Token.DO;
                }
                case '}' -> {
                    return Token.END;
                }
                case ',' -> {
                    return Token.COMMA;
                }
                case '+' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.PLUS);
                    }
                    return Token.OPERATOR.setValue(Operator.PLUS);
                }
                case '-' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.MINUS);
                    }
                    return Token.OPERATOR.setValue(Operator.MINUS);
                }
                case '*' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.MULTIPLY);
                    }
                    if (peek() == '*') {
                        next();
                        if (peek() == '=') {
                            next();
                            return Token.OPERATOR_ASSIGN.setValue(Operator.POWER);
                        }
                        return Token.OPERATOR.setValue(Operator.POWER);
                    }
                    return Token.OPERATOR.setValue(Operator.MULTIPLY);
                }
                case '=' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR.setValue(Operator.EQUAL);
                    }
                    return Token.ASSIGN;
                }
                case '!' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR.setValue(Operator.NEQUAL);
                    }
                    return Token.OPERATOR.setValue(Operator.NOT);
                }
                case '&' -> {
                    if (peek() == '&') {
                        next();
                        return Token.OPERATOR.setValue(Operator.AND);
                    }
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.BAND);
                    }
                    return Token.OPERATOR.setValue(Operator.BAND);
                }
                case '|' -> {
                    if (peek() == '|') {
                        next();
                        return Token.OPERATOR.setValue(Operator.OR);
                    }
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.BOR);
                    }
                    return Token.OPERATOR.setValue(Operator.BOR);
                }
                case '^' -> {
                    if (peek() == '=') {
                        next();
                        return Token.OPERATOR_ASSIGN.setValue(Operator.POWER);
                    }
                    return Token.OPERATOR.setValue(Operator.POWER);
                }
                case '<' -> {
                    if (peek() == '=') {
                        next();
                        Token.OPERATOR.setValue(Operator.LTE);
                    }
                    if (peek() == '<') {
                        next();
                        if (peek() == '=') {
                            next();
                            return Token.OPERATOR_ASSIGN.setValue(Operator.LSHIFT);
                        }
                        return Token.OPERATOR.setValue(Operator.LSHIFT);
                    }
                    if (peek() == '>') {
                        next();
                        return Token.OPERATOR.setValue(Operator.NEQUAL);
                    }
                    return Token.OPERATOR.setValue(Operator.LT);
                }
                case '>' -> {
                    if (peek() == '=') {
                        next();
                        Token.OPERATOR.setValue(Operator.GTE);
                    }
                    if (peek() == '>') {
                        next();
                        if (peek() == '=') {
                            next();
                            return Token.OPERATOR_ASSIGN.setValue(Operator.RSHIFT);
                        }
                        if (peek() == '>') {
                            next();
                            if (peek() == '=') {
                                next();
                                return Token.OPERATOR_ASSIGN.setValue(Operator.ARSHIFT);
                            }
                            return Token.OPERATOR.setValue(Operator.ARSHIFT);
                        }
                        return Token.OPERATOR.setValue(Operator.RSHIFT);
                    }
                    return Token.OPERATOR.setValue(Operator.GT);
                }
                default -> {
                    if (Character.isAlphabetic(c)) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(c);
                        while (hasNext()) {
                            char p = peek();
                            if (Character.isAlphabetic(p) || Character.isDigit(p) || p == '_') {
                                buffer.append(next());
                            } else {
                                break;
                            }
                        }
                        String str = buffer.toString();
                        switch (str) {
                            default -> { return Token.IDENT.setValue(str.toLowerCase(Locale.ROOT)); }
                            case "true" -> { return Token.TRUE; }
                            case "false" -> { return Token.FALSE; }
                            case "if" -> { return Token.IF; }
                            case "else" -> { return Token.ELSE; }
                            case "elif" -> {
                                reverted.add(Token.IF);
                                return Token.ELSE;
                            }
                            case "while" -> { return Token.WHILE; }
                            case "for" -> { return Token.FOR; }
                            case "from" -> { return Token.FROM; }
                            case "to" -> { return Token.TO; }
                            case "break" -> { return Token.BREAK; }
                            case "continue" -> { return Token.CONTINUE; }
                            case "do" -> { return Token.DO; }
                            case "end" -> { return Token.END; }
                            case "and" -> { return Token.OPERATOR.setValue(Operator.AND); }
                            case "or" -> { return Token.OPERATOR.setValue(Operator.OR); }
                            case "not" -> { return Token.OPERATOR.setValue(Operator.NOT); }
                            case "xor" -> { return Token.OPERATOR.setValue(Operator.XOR); }
                        }
                    }
                    if (Character.isDigit(c)) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(c);
                        while (hasNext()) {
                            char p = peek();
                            if (Character.isDigit(p) || p == '.') {
                                buffer.append(p);
                                next();
                            } else {
                                next();
                                break;
                            }
                        }
                        String str = buffer.toString();
                        if (str.contains(".")) {
                            return Token.FLITERAL.setValue(Float.parseFloat(str));
                        } else {
                            return Token.ILITERAL.setValue(Integer.parseInt(str));
                        }
                    }
                }
            }
        }
        
        return Token.EOF;
    }
}
