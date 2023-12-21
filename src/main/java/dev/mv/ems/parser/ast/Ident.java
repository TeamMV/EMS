package dev.mv.ems.parser.ast;

import java.util.Map;

public class Ident implements Expression {
    public String name;

    public Ident(String name) {
        this.name = name;
    }

    public Type inferType(Map<String, Type> vars) {
        Type t = vars.get(name);
        if (t != null) return t;
        return Type.UNKNOWN;
    }

    @Override
    public String toString() {
        return name;
    }

}
