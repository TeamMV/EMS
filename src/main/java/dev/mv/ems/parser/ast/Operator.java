package dev.mv.ems.parser.ast;

public enum Operator {
    PLUS(5),
    MINUS(5),
    MULTIPLY(6),
    DIVIDE(6),
    POWER(7),
    MOD(6),
    EQUAL(2),
    NEQUAL(2),
    LT(3),
    GT(3),
    LTE(3),
    GTE(3),
    AND(0),
    BAND(1),
    OR(0),
    BOR(1),
    XOR(1),
    NOT(8),
    FACT(8),
    LSHIFT(4),
    RSHIFT(4),
    ARSHIFT(4);

    final int precedence;

    Operator(int precedence) {
        this.precedence = precedence;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isUnary() {
        return this == NOT || this == MINUS;
    }

    public boolean isComp() {
        switch (this) {
            case GT, LT, GTE, LTE, EQUAL, NEQUAL, AND, OR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    
    public double apply(double a, double b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> a / b;
            case POWER -> Math.pow(a, b);
            case MOD -> a % b;
            case BAND -> (double) ((long) a & (long) b);
            case BOR -> (double) ((long) a | (long) b);
            case XOR -> (double) ((long) a ^ (long) b);
            case LSHIFT -> (double) ((long) a << (long) b);
            case RSHIFT -> (double) ((long) a >> (long) b);
            case ARSHIFT -> (double) ((long) a >>> (long) b);
            default -> 0.0f;
        };
    }
    
    public boolean applyCond(double a, double b) {
        return switch (this) {
            case EQUAL -> a == b;
            case NEQUAL -> a != b;
            case LT -> a < b;
            case GT -> a > b;
            case LTE -> a <= b;
            case GTE -> a >= b;
            case AND -> a > 0 && b > 0;
            case OR -> a > 0 || b > 0;
            default -> false;
        };
    }

    public long apply(long a, long b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> a / b;
            case POWER -> (long) Math.pow(a, b);
            case MOD -> a % b;
            case BAND -> a & b;
            case BOR -> a | b;
            case XOR -> a ^ b;
            case LSHIFT -> a << b;
            case RSHIFT -> a >> b;
            case ARSHIFT -> a >>> b;
            default -> 0;
        };
    }

    public boolean applyCond(long a, long b) {
        return switch (this) {
            case EQUAL -> a == b;
            case NEQUAL -> a != b;
            case LT -> a < b;
            case GT -> a > b;
            case LTE -> a <= b;
            case GTE -> a >= b;
            case AND -> a > 0 && b > 0;
            case OR -> a > 0 || b > 0;
            default -> false;
        };
    }

    public boolean apply(boolean a, boolean b) {
        return switch (this) {
            case POWER -> a;
            case BAND -> a & b;
            case BOR -> a | b;
            case XOR, MINUS -> a ^ b;
            case EQUAL -> a == b;
            case NEQUAL -> a != b;
            case AND, MOD, MULTIPLY, DIVIDE -> a && b;
            case OR, PLUS -> a || b;
            case GT -> a && !b;
            case LT -> !a && b;
            case GTE -> (a && !b) || (a == b);
            case LTE -> (!a && b) || (a == b);
            default -> false;
        };
    }

    public double apply(double val) {
        return switch (this) {
            case NOT, MINUS -> -val;
            default -> val;
        };
    }

    public long apply(long val) {
        return switch (this) {
            case NOT, MINUS -> -val;
            default -> val;
        };
    }

    public boolean apply(boolean val) {
        return switch (this) {
            case NOT, MINUS -> !val;
            default -> val;
        };
    }
}
