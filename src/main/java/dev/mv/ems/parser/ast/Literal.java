package dev.mv.ems.parser.ast;

import java.util.Map;

public class Literal implements Expression {
    Type type;
    int value;

    public Literal(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public Type inferType(Map<String, Type> vars) {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case INT -> {
                return value + "";
            }
            case FLOAT -> {
                return Float.intBitsToFloat(value) + "";
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
