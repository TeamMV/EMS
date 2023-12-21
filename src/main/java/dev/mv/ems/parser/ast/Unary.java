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

    @Override
    public String toString() {
        return "(" + op.name().toLowerCase() + " " + expr.toString() + ")";
    }

}
