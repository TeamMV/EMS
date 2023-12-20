package dev.mv.ems.parser.ast;

public class ExpressionStatement implements Statement {

    public Expression expr;

    public ExpressionStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return expr.toString() + "\n";
    }

}
