package dev.mv.ems.parser.ast;

import java.util.Map;

public class Unary implements Expression {
    public Expression expr;
    public Operator op;

    public Unary(Expression expr, Operator op) {
        this.expr = expr;
        this.op = op;
    }

    public Type inferType(Map<String, Type> vars) {
        return expr.inferType(vars);
    }

    public boolean collapsible() {
        return expr.collapsible();
    }

    public Literal collapse() {
        Literal inner = expr.collapse();
        switch (inner.type) {
            case BOOL -> inner.set(op.apply(inner.getB()));
            case FLOAT -> inner.set(op.apply(inner.getD()));
            case INT -> inner.set(op.apply(inner.getL()));
        }
        return inner;
    }

    @Override
    public String toString() {
        return "(" + op.name().toLowerCase() + " " + expr.toString() + ")";
    }
}
