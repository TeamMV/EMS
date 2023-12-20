package dev.mv.ems.parser.ast;

public class If implements Statement {

    public Expression cond;
    public Statement body;
    public Statement else_body;

    public If(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    public If(Expression cond, Statement body, Statement else_body) {
        this.cond = cond;
        this.body = body;
        this.else_body = else_body;
    }

    @Override
    public String toString() {
        if (else_body == null) return "if " + cond.toString() + " " + body.toString() + "\n";
        return "if " + cond.toString() + " " + body.toString() + " else " + else_body.toString() + "\n";
    }

}
