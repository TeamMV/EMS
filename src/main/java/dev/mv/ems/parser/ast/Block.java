package dev.mv.ems.parser.ast;

import java.util.List;

public class Block implements Statement {

    public List<Statement> stmts;

    public Block(List<Statement> stmts) {
        this.stmts = stmts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        for (Statement stmt : stmts) {
            builder.append(stmt);
        }
        builder.append("}");
        return builder.toString();
    }

}
