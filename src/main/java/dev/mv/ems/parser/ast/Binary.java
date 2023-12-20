package dev.mv.ems.parser.ast;

public class Binary implements Expression {
    public Expression a;
    public Operator op;
    public Expression b;

    public Binary(Expression a, Operator op, Expression b) {
        this.a = a;
        this.op = op;
        this.b = b;
    }

    public Type inferType() {
        if (op.isComp()) return Type.BOOL;
        Type at = a.inferType();
        Type bt = b.inferType();
        if (at == Type.UNKNOWN) return bt;
        if (bt == Type.UNKNOWN) return at;
        if (at == bt) return at;
        if (at == Type.FLOAT || bt == Type.FLOAT) return Type.FLOAT;
        return Type.INT;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " " + op.name().toLowerCase() + " " + b.toString() + ")";
    }

}
