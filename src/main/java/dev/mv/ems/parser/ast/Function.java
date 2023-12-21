package dev.mv.ems.parser.ast;

public enum Function {
    SIN(1, Type.FLOAT, Type.FLOAT),
    COS(1, Type.FLOAT, Type.FLOAT),
    TAN(1, Type.FLOAT, Type.FLOAT),
    ARCSIN(1, Type.FLOAT, Type.FLOAT),
    ARCCOS(1, Type.FLOAT, Type.FLOAT),
    ARCTAN(1, Type.FLOAT, Type.FLOAT),
    ARCTAN2(2, Type.FLOAT, Type.FLOAT, Type.FLOAT),
    SINH(1, Type.FLOAT, Type.FLOAT),
    COSH(1, Type.FLOAT, Type.FLOAT),
    TANH(1, Type.FLOAT, Type.FLOAT),
    CSC(1, Type.FLOAT, Type.FLOAT),
    SEC(1, Type.FLOAT, Type.FLOAT),
    COT(1, Type.FLOAT, Type.FLOAT),
    ARCCSC(1, Type.FLOAT, Type.FLOAT),
    ARCSEC(1, Type.FLOAT, Type.FLOAT),
    ARCCOT(1, Type.FLOAT, Type.FLOAT),
    CSCH(1, Type.FLOAT, Type.FLOAT),
    SECH(1, Type.FLOAT, Type.FLOAT),
    COTH(1, Type.FLOAT, Type.FLOAT),
    SQRT(1, Type.FLOAT, Type.FLOAT),
    CBRT(1, Type.FLOAT, Type.FLOAT),
    MIN(2, Type.FLOAT, Type.FLOAT, Type.FLOAT),
    MAX(2, Type.FLOAT, Type.FLOAT, Type.FLOAT),
    RAD(1, Type.FLOAT, Type.FLOAT),
    DEG(1, Type.FLOAT, Type.FLOAT),
    ABS(1, Type.FLOAT, Type.FLOAT),
    EXP(1, Type.FLOAT, Type.FLOAT),
    FLOOR(1, Type.INT, Type.FLOAT),
    CEIL(1, Type.INT, Type.FLOAT),
    ROUND(1, Type.INT, Type.FLOAT),
    LN(1, Type.FLOAT, Type.FLOAT),
    LOG10(1, Type.FLOAT, Type.FLOAT),
    LOG(2, Type.FLOAT, Type.FLOAT, Type.FLOAT),
    RAND(0, Type.FLOAT),
    RANDINT(2, Type.INT, Type.INT, Type.INT);

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
            case ARCTAN2 -> (float) Math.atan2(values[0], values[1]);
            case SINH -> (float) Math.sinh(values[0]);
            case COSH -> (float) Math.cosh(values[0]);
            case TANH -> (float) Math.tanh(values[0]);
            case CSC -> (float) (1.0 / Math.sin(values[0]));
            case SEC -> (float) (1.0 / Math.cos(values[0]));
            case COT -> (float) (1.0 / Math.tan(values[0]));
            case ARCCSC -> (float) Math.asin(1.0 / values[0]);
            case ARCSEC -> (float) Math.acos(1.0 / values[0]);
            case ARCCOT -> (float) Math.atan(1.0 / values[0]);
            case CSCH -> (float) (1.0 / Math.sinh(values[0]));
            case SECH -> (float) (1.0 / Math.cosh(values[0]));
            case COTH -> (float) (1.0 / Math.tanh(values[0]));
            case SQRT -> (float) Math.sqrt(values[0]);
            case CBRT -> (float) Math.cbrt(values[0]);
            case MIN -> Math.min(values[0], values[1]);
            case MAX -> Math.max(values[0], values[1]);
            case RAD -> 0.017453292519943295f * values[0];
            case DEG -> 57.29577951308232f * values[0];
            case ABS -> Math.abs(values[0]);
            case EXP -> (float) Math.exp(values[0]);
            case FLOOR -> (float) (int) Math.floor(values[0]);
            case CEIL -> (float) (int) Math.ceil(values[0]);
            case ROUND -> Math.round(values[0]);
            case LN -> (float) Math.log(values[0]);
            case LOG10 -> (float) Math.log10(values[0]);
            case LOG -> (float) Math.log(values[1]) / (float) Math.log(values[0]);
            case RAND -> (float) Math.random();
            case RANDINT -> (float) (int) (Math.random() * (values[1] - values[0]) + values[0]);
        };
    }

    public static Function parse(String name) {
        try {
            return Function.valueOf(name.toUpperCase());
        } catch (Exception ignore) {
            return switch (name.toLowerCase()) {
                case "asin", "arc_sin" -> ARCSIN;
                case "acos", "arc_cos" -> ARCCOS;
                case "atan", "arc_tan" -> ARCTAN;
                case "atan2" -> ARCTAN2;
                case "hsin", "hyperbolicsin", "hyperoblic_sin" -> SINH;
                case "hcos", "hyperboliccos", "hyperoblic_cos" -> COSH;
                case "htan", "hyperbolictan", "hyperoblic_tan" -> TANH;
                case "acsc", "arc_csc" -> ARCCSC;
                case "asec", "arc_sec" -> ARCSEC;
                case "acot", "arc_cot" -> ARCCOT;
                case "hcsc", "hyperboliccsc", "hyperoblic_csc" -> CSCH;
                case "hsec", "hyperbolicsec", "hyperoblic_sec" -> SECH;
                case "hcot", "hyperboliccot", "hyperoblic_cot" -> COTH;
                case "squareroot", "square_root" -> SQRT;
                case "cuberoot", "cube_root" -> CBRT;
                case "degtorad", "deg_to_rad", "degreestoradians", "degrees_to_radians", "radians" -> RAD;
                case "radtodeg", "rad_to_deg", "radianstodegrees", "radians_to_degrees", "degrees" -> DEG;
                case "absolute" -> ABS;
                case "naturallog", "natural_log" -> LN;
                case "random" -> RAND;
                case "randomint", "random_int" -> RANDINT;
                default -> null;
            };
        }
    }
}
