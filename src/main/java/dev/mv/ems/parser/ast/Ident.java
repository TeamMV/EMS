package dev.mv.ems.parser.ast;

import dev.mv.ems.parser.Parser;
import dev.mv.utils.generic.triplet.Triplet;

import java.util.Arrays;
import java.util.Map;

public class Ident implements Expression {
    public String name;

    public Ident(String name) {
        this.name = name;
    }

    public Type inferType(Map<String, Type> vars) {
        if (name.equals("index")) return Type.INT;
        Type t = vars.get(name);
        if (t != null) return t;
        return Type.UNKNOWN;
    }

    public boolean collapsible() {
        return Arrays.stream(Parser.CONSTANTS).anyMatch(constant -> constant.a.equals(name));
    }

    public Literal collapse() {
        Triplet<String, Type, Long> constant = Arrays.stream(Parser.CONSTANTS).filter(c -> c.a.equals(name)).findFirst().orElse(null);
        if (constant == null) return null;
        return new Literal(constant.b, constant.c);
    }

    @Override
    public String toString() {
        return name;
    }

}
