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

    public boolean collapsible() {
        return a.collapsible() && b.collapsible();
    }

    public Literal collapse() {
        Literal a = this.a.collapse();
        Literal b = this.b.collapse();
        if (op.isComp()) {
            switch (chooseType(a, b)) {
                case FLOAT -> {
                    if (a.type != Type.FLOAT) {
                        return new Literal(Type.BOOL, op.applyCond((double) a.getL(), b.getD()) ? 1 : 0);
                    }
                    else if (b.type != Type.FLOAT) {
                        return new Literal(Type.BOOL, op.applyCond(a.getD(), (double) b.getL()) ? 1 : 0);
                    }
                    return new Literal(Type.BOOL, op.applyCond(a.getD(), b.getD()) ? 1 : 0);
                }
                case INT -> {
                    return new Literal(Type.BOOL, op.applyCond(a.getL(), b.getL()) ? 1 : 0);
                }
                case BOOL -> {
                    return new Literal(Type.BOOL, op.apply(a.getB(), b.getB()) ? 1 : 0);
                }
            }
        }
        switch (chooseType(a, b)) {
            case FLOAT -> {
                if (a.type != Type.FLOAT) {
                    return new Literal(Type.FLOAT, Double.doubleToLongBits(op.apply((double) a.getL(), b.getD())));
                }
                else if (b.type != Type.FLOAT) {
                    return new Literal(Type.FLOAT, Double.doubleToLongBits(op.apply(a.getD(), (double) b.getL())));
                }
                return new Literal(Type.FLOAT, Double.doubleToLongBits(op.apply(a.getD(), b.getD())));
            }
            case INT -> {
                return new Literal(Type.INT, op.apply(a.getL(), b.getL()));
            }
            case BOOL -> {
                return new Literal(Type.BOOL, op.apply(a.getB(), b.getB()) ? 1 : 0);
            }
        }
        return null;
    }

    private Type chooseType(Literal a, Literal b) {
        if (a.type == b.type) return a.type;
        if (a.type == Type.FLOAT || b.type == Type.FLOAT) return Type.FLOAT;
        return Type.INT;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " " + op.name().toLowerCase() + " " + b.toString() + ")";
    }
}
