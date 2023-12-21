package dev.mv.ems.runtime;

import dev.mv.ems.parser.ast.Type;

public class Variable {

    public String name;
    public Type type;
    public int value;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Variable setValue(int value) {
        this.value = value;
        return this;
    }

    public Variable setValue(float value) {
        this.value = Float.floatToIntBits(value);
        return this;
    }

    public Variable setValue(boolean value) {
        this.value = value ? 1 : 0;
        return this;
    }

    public int getValueI() {
        return value;
    }

    public float getValueF() {
        return Float.intBitsToFloat(value);
    }

    public boolean getValueB() {
        return value != 0;
    }

    @Override
    public String toString() {
        if (type == Type.INT) return name + ": " + type.toString().toLowerCase() + " = " + value;
        if (type == Type.FLOAT) return name + ": " + type.toString().toLowerCase() + " = " + getValueF();
        if (type == Type.BOOL) return name + ": " + type.toString().toLowerCase() + " = " + getValueB();
        return name + ": " + type.toString().toLowerCase() + " = " + value;
    }

}
