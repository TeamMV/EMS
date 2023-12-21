package dev.mv.ems.parser.ast;

import java.util.Map;

public class Binary implements Expression {
    public Expression a;
    public Operator op;
    public Expression b;

    public Binary(Expression a, Operator op, Expression b) {
        this.a = a;
        this.op = op;
        this.b = b;
    }

    public Type inferType(Map<String, Type> vars) {
        if (op.isComp()) return Type.BOOL;
        Type at = a.inferType(vars);
        Type bt = b.inferType(vars);
        if (op == Operator.DIVIDE) {
            if (at == bt) {
                if (at == Type.INT || at == Type.FLOAT) return Type.FLOAT;
                return at;
            }
            if (at == Type.UNKNOWN) {
                if (a instanceof Ident i) vars.put(i.name, bt);
                if (bt == Type.INT) return Type.FLOAT;
                return bt;
            }
            if (bt == Type.UNKNOWN) {
                if (b instanceof Ident i) vars.put(i.name, at);
                if (at == Type.INT) return Type.FLOAT;
                return at;
            }
            return Type.FLOAT;
        }
        if (at == bt) return at;
        if (at == Type.UNKNOWN) {
            if (a instanceof Ident i) vars.put(i.name, bt);
            return bt;
        }
        if (bt == Type.UNKNOWN) {
            if (b instanceof Ident i) vars.put(i.name, at);
            return at;
        }
        if (at == Type.FLOAT || bt == Type.FLOAT) return Type.FLOAT;
        return Type.INT;
    }


    @Override
    public String toString() {
        return "(" + a.toString() + " " + op.name().toLowerCase() + " " + b.toString() + ")";
    }
}
