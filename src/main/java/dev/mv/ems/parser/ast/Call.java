package dev.mv.ems.parser.ast;

import java.util.List;

public class Call implements Expression {
    public Function function;
    public List<Expression> args;

    public Call(String name, List<Expression> args) {
        function = Function.parse(name);
        this.args = args;
        if (args.size() != function.argCount) throw new IllegalArgumentException("Function " + name + " takes in " + function.argCount + " arguments. Provided: " + args.size());
        for (int i = 0; i < args.size(); i++) {
            Type type = args.get(i).inferType();
            if (type == null || type == Type.UNKNOWN) continue;
            if (type != function.argTypes[i]) throw new IllegalArgumentException("Function " + name + " takes in a " + function.argTypes[i] + " as the " + (i + 1) + " argument. Provided: " + type);
        }
    }

    public Type inferType() {
        return function.type();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(function.name().toLowerCase() + "(");
        for (Expression expr : args) {
            builder.append(expr.toString() + ", ");
        }
        builder.append(")\n");
        return builder.toString();
    }

}
