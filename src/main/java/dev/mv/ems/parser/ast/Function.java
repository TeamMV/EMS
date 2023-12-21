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
    LCM(-1, Type.INT, Type.INT),
    GCD(-1, Type.INT, Type.INT),
    SIGN(1, Type.INT, Type.FLOAT),
    ROOT(2, Type.FLOAT, Type.FLOAT, Type.FLOAT),
    NPR(2, Type.INT, Type.INT, Type.INT),
    NCR(2, Type.INT, Type.INT, Type.INT),
    LN(1, Type.FLOAT, Type.FLOAT),
    LG(1, Type.FLOAT, Type.FLOAT),
    LB(1, Type.FLOAT, Type.FLOAT),
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
    public double evaluate(double... values) {
        return switch (this) {
            case SIN -> Math.sin(values[0]);
            case COS -> Math.cos(values[0]);
            case TAN -> Math.tan(values[0]);
            case ARCSIN -> Math.asin(values[0]);
            case ARCCOS -> Math.acos(values[0]);
            case ARCTAN -> Math.atan(values[0]);
            case ARCTAN2 -> Math.atan2(values[0], values[1]);
            case SINH -> Math.sinh(values[0]);
            case COSH -> Math.cosh(values[0]);
            case TANH -> Math.tanh(values[0]);
            case CSC -> 1.0 / Math.sin(values[0]);
            case SEC -> 1.0 / Math.cos(values[0]);
            case COT -> 1.0 / Math.tan(values[0]);
            case ARCCSC -> Math.asin(1.0 / values[0]);
            case ARCSEC -> Math.acos(1.0 / values[0]);
            case ARCCOT -> Math.atan(1.0 / values[0]);
            case CSCH -> 1.0 / Math.sinh(values[0]);
            case SECH -> 1.0 / Math.cosh(values[0]);
            case COTH -> 1.0 / Math.tanh(values[0]);
            case SQRT -> Math.sqrt(values[0]);
            case CBRT -> Math.cbrt(values[0]);
            case MIN -> Math.min(values[0], values[1]);
            case MAX -> Math.max(values[0], values[1]);
            case RAD -> 0.017453292519943295 * values[0];
            case DEG -> 57.29577951308232 * values[0];
            case ABS -> Math.abs(values[0]);
            case EXP -> Math.exp(values[0]);
            case FLOOR -> (double) (int) Math.floor(values[0]);
            case CEIL -> (double) (int) Math.ceil(values[0]);
            case ROUND -> Math.round(values[0]);
            case LCM -> {
                if (values.length == 0) yield 0.0f;
                if (values.length == 1) yield (int) values[0];
                int result = 1;
                for (double value : values) {
                    result = lcm(result, (int) value);
                }
                yield 0.0F;
            }
            case GCD -> {
                if (values.length == 0) yield 0.0f;
                if (values.length == 1) yield (int) values[0];
                int result = gcd((int) values[0], (int) values[1]);
                for (int i = 2; i < values.length; i++) {
                    result = gcd(result, (int) values[i]);
                }
                yield result;
            }
            case SIGN -> values[0] >= 0 ? 1 : -1;
            case ROOT -> (float) Math.pow(values[1], 1.0 / values[0]);
            case NPR -> {
                int n = (int) values[0];
                int r = (int) values[0];
                if (r > n) yield 0.0f;
                int result = 1;
                for (int i = n - r + 1; i <= n; i++) result *= i;
                yield result;
            }
            case NCR -> {
                int n = (int) values[0];
                int r = (int) values[1];
                r = Math.min(r, n - r);
                if (r == 0) yield 1.0f;
                int result = 1;
                for (int i = 1; i <= r; i++) result *= (int) ((double) (n - r + i) / i);
                yield result;
            }
            case LN -> Math.log(values[0]);
            case LG -> Math.log10(values[0]);
            case LB -> Math.log(values[1]) / (float) Math.log(2);
            case LOG -> Math.log(values[1]) / (float) Math.log(values[0]);
            case RAND -> Math.random();
            case RANDINT -> (double) (int) (Math.random() * (values[1] - values[0]) + values[0]);
        };
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
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
                case "lowestcommonmultiple", "lowest_common_multiple" -> LCM;
                case "greatestcommondenominator", "greatest_common_denominator" -> GCD;
                case "naturallog", "natural_log" -> LN;
                case "random" -> RAND;
                case "randomint", "random_int" -> RANDINT;
                default -> null;
            };
        }
    }
}
