package dev.mv.ems.parser.ast;

public class Ident implements Expression {
    public String name;

    public Ident(String name) {
        this.name = name;
    }

    public Type inferType() {
        return Type.UNKNOWN;
    }

    @Override
    public String toString() {
        return name;
    }

}
