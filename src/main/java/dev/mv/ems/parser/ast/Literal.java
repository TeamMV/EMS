package dev.mv.ems.parser.ast;

import java.util.Map;

public class Literal implements Expression {
    public Type type;
    public long value;

    public Literal(Type type, long value) {
        this.type = type;
        this.value = value;
    }

    public Type inferType(Map<String, Type> vars) {
        return type;
    }

    public boolean collapsible() {
        return true;
    }

    public Literal collapse() {
        return this;
    }

    void set(long value) {
        this.value = value;
    }

    void set(double value) {
        this.value = Double.doubleToLongBits(value);
    }

    void set(boolean value) {
        this.value = value ? 1 : 0;
    }

    double getD() {
        return Double.longBitsToDouble(value);
    }

    long getL() {
        return value;
    }

    boolean getB() {
        return value != 0;
    }

    double getAsD() {
        if (type == Type.FLOAT) return Double.longBitsToDouble(value);
        return (double) value;
    }

    @Override
    public String toString() {
        switch (type) {
            case INT -> {
                return value + "";
            }
            case FLOAT -> {
                return Double.longBitsToDouble(value) + "";
            }
            case BOOL -> {
                return (value != 0) + "";
            }
            default -> {
                return "";
            }
        }
    }

}
