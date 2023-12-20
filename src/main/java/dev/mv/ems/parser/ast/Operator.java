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
            case GT, LT, GTE, LTE, EQUAL, NEQUAL -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    
    public float apply(float a, float b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> a / b;
            case POWER -> (float) Math.pow(a, b);
            case MOD -> a % b;
            case BAND -> (float) ((int) a & (int) b);
            case BOR -> (float) ((int) a | (int) b);
            case XOR -> (float) ((int) a ^ (int) b);
            case LSHIFT -> (float) ((int) a << (int) b);
            case RSHIFT -> (float) ((int) a >> (int) b);
            case ARSHIFT -> (float) ((int) a >>> (int) b);
            default -> 0.0f;
        };
    }
    
    public boolean applyCond(float a, float b) {
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

    public int apply(int a, int b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> a / b;
            case POWER -> (int) Math.pow(a, b);
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

    public boolean applyCond(int a, int b) {
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
            case LTE, GTE -> true;
            case AND, MOD, MULTIPLY, DIVIDE -> a && b;
            case OR, PLUS -> a || b;
            default -> false;
        };
    }

    public float apply(float val) {
        return switch (this) {
            case NOT, MINUS -> -val;
            default -> val;
        };
    }

    public int apply(int val) {
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
