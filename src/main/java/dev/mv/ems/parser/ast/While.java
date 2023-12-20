package dev.mv.ems.parser.ast;

public class While implements Statement {

    public Expression cond;
    public Statement body;

    public While(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

}
