package dev.mv.ems.parser.ast;

public class For implements Statement {

    public String ident;
    public Expression begin;
    public Expression end;
    public Statement body;

    public For(String ident, Expression begin, Expression end, Statement body) {
        this.ident = ident;
        this.begin = begin;
        this.end = end;
        this.body = body;
    }

}
