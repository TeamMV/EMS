package dev.mv.ems.runtime;

import dev.mv.ems.parser.ast.Type;

public class Variable {

    public String name;
    public Type type;
    public long value;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Variable setValue(long value) {
        this.value = value;
        return this;
    }

    public Variable setValue(double value) {
        this.value = Double.doubleToLongBits(value);
        return this;
    }

    public Variable setValue(boolean value) {
        this.value = value ? 1 : 0;
        return this;
    }

    public long getValueL() {
        return value;
    }

    public double getValueD() {
        return Double.longBitsToDouble(value);
    }

    public boolean getValueB() {
        return value != 0;
    }

    public double getValueAsD() {
        if (type == Type.FLOAT) return getValueD();
        return (double) getValueL();
    }

    @Override
    public String toString() {
        if (type == Type.INT) return name + ": " + type.toString().toLowerCase() + " = " + value;
        if (type == Type.FLOAT) return name + ": " + type.toString().toLowerCase() + " = " + getValueD();
        if (type == Type.BOOL) return name + ": " + type.toString().toLowerCase() + " = " + getValueB();
        return name + ": " + type.toString().toLowerCase() + " = " + value;
    }

    @Override
    public Variable clone() {
        return new Variable(name, type).setValue(value);
    }

}
