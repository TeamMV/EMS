package dev.mv.ems.parser.ast;

import java.util.List;
import java.util.Map;

public class Call implements Expression {
    public Function function;
    public List<Expression> args;

    public Call(String name, List<Expression> args) {
        function = Function.parse(name);
        this.args = args;
    }

    public Type inferType(Map<String, Type> vars) {
        return function.type();
    }

    public boolean checkTypes(Map<String, Type> vars) {
        if (function.argCount == 0) return true;
        if (function.argCount < 0) {
            if (function.argTypes.length == 0) return true;
            Type t = function.argTypes[0];
            boolean b = t == Type.BOOL;
            for (Expression e : args) {
                Type type = e.inferType(vars);
                if (type == null || type == Type.UNKNOWN) continue;
                if (type == Type.BOOL ^ b) throw new IllegalArgumentException("Function \"" + function.name().toLowerCase() + "\" takes in a variable number of " + function.argTypes[0].name().toLowerCase() + "s. Provided: " + type.name().toLowerCase());
            }
            return true;
        }
        if (args.size() != function.argCount) throw new IllegalArgumentException("Function " + function.name().toLowerCase() + " takes in " + function.argCount + " arguments. Provided: " + args.size());
        boolean checks = false;
        for (int i = 0; i < args.size(); i++) {
            Type type = args.get(i).inferType(vars);
            if (type == null || type == Type.UNKNOWN) continue;
            checks = true;
            if (type == Type.BOOL ^ function.argTypes[i] == Type.BOOL) throw new IllegalArgumentException("Function \"" + function.name().toLowerCase() + "\" takes in a " + function.argTypes[i].name().toLowerCase() + " as the " + fmt(i + 1) + " argument. Provided: " + type.name().toLowerCase());
        }
        return checks;
    }

    public boolean collapsible() {
        for (Expression e : args) {
            if (!e.collapsible()) return false;
        }
        return true;
    }

    public Literal collapse() {
        double[] values = new double[args.size()];
        for (int i = 0; i < args.size(); i++) {
            values[i] = args.get(i).collapse().getAsD();
        }
        double result = function.evaluate(values);
        return switch (function.type) {
            case FLOAT -> new Literal(Type.FLOAT, Double.doubleToLongBits(result));
            case INT -> new Literal(Type.INT, (long) result);
            case BOOL -> new Literal(Type.BOOL, (long) result);
            default -> null;
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(function.name().toLowerCase() + "(");
        for (Expression expr : args) {
            builder.append(expr.toString() + ", ");
        }
        builder.append(")");
        return builder.toString();
    }

    private String fmt(int n) {
        switch (n % 10) {
            case 1 -> { return n + "st"; }
            case 2 -> { return n + "nd"; }
            case 3 -> { return n + "rd"; }
            default -> { return n + "th"; }
        }
    }

}
