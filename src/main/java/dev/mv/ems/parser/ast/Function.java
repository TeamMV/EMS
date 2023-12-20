package dev.mv.ems.parser.ast;

import dev.mv.utils.Utils;

public enum Function {
    SIN(1, Type.FLOAT),
    COS(1, Type.FLOAT),
    TAN(1, Type.FLOAT),
    ARCSIN(1, Type.FLOAT),
    ARCCOS(1, Type.FLOAT),
    ARCTAN(1, Type.FLOAT),
    SINH(1, Type.FLOAT),
    COSH(1, Type.FLOAT),
    TANH(1, Type.FLOAT),
    SQRT(1, Type.FLOAT),
    CBRT(1, Type.FLOAT),
    MIN(2, Type.FLOAT),
    MAX(2, Type.FLOAT),
    RAD(1, Type.FLOAT),
    DEG(1, Type.FLOAT),
    ABS(1, Type.FLOAT),
    EXP(1, Type.FLOAT),
    FLOOR(1, Type.FLOAT),
    CEIL(1, Type.FLOAT),
    ROUND(1, Type.FLOAT),
    LN(1, Type.FLOAT),
    LOG10(1, Type.FLOAT),
    LOG(2, Type.FLOAT),
    RAND(0, Type.FLOAT),
    RANDINT(2, Type.FLOAT);

    final int argCount;
    final Type type;
    final Type[] argTypes;

    Function(int argCount, Type type, Type... argTypes) {
        this.argCount = argCount;
        this.type = type;
        this.argTypes = argTypes;
        assert argCount == argTypes.length;
    }

    public int getArgCount() {
        return argCount;
    }

    public Type type() {
        return type;
    }

    public Type[] getArgTypes() {
        return argTypes;
    }

    //TODO
    public float evaluate(float... values) {
        return switch (this) {
            case SIN -> (float) Math.sin(values[0]);
            case COS -> (float) Math.cos(values[0]);
            case TAN -> (float) Math.tan(values[0]);
            case ARCSIN -> (float) Math.asin(values[0]);
            case ARCCOS -> (float) Math.acos(values[0]);
            case ARCTAN -> (float) Math.atan(values[0]);
            case SINH -> (float) Math.sinh(values[0]);
            case COSH -> (float) Math.cosh(values[0]);
            case TANH -> (float) Math.tanh(values[0]);
            case SQRT -> (float) Math.sqrt(values[0]);
            case CBRT -> (float) Math.cbrt(values[0]);
            case MIN -> Math.min(values[0], values[1]);
            case MAX -> Math.max(values[0], values[1]);
            case RAD -> 0.017453292519943295f * values[0];
            case DEG -> 57.29577951308232f * values[0];
            case ABS -> Math.abs(values[0]);
            case EXP -> (float) Math.exp(values[0]);
            case FLOOR -> (float) Math.floor(values[0]);
            case CEIL -> (float) Math.ceil(values[0]);
            case ROUND -> Math.round(values[0]);
            case LN -> (float) Math.log(values[0]);
            case LOG10 -> (float) Math.log10(values[0]);
            case LOG -> (float) Math.log(values[1]) / (float) Math.log(values[0]);
            case RAND -> (float) Math.random();
            case RANDINT -> (float) Math.random() * (values[1] - values[0] + values[0]);
        };
    }

    public static Function parse(String name) {
        return Function.valueOf(name.toUpperCase());
    }
}
