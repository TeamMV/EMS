package dev.mv.ems.parser.ast;

public class Unary implements Expression {
    public Expression expr;
    public Operator op;

    public Unary(Expression expr, Operator op) {
        this.expr = expr;
        this.op = op;
    }

    public Type inferType() {
        return expr.inferType();
    }

    @Override
    public String toString() {
        return "(" + op.name().toLowerCase() + " " + expr.toString() + ")";
    }

}
