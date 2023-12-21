package dev.mv.ems.runtime;

import dev.mv.ems.parser.ast.Statement;

import java.util.List;
import java.util.Map;

public class Program {

    public List<Statement> stmts;
    public Map<String, Variable> vars;

    public Program(List<Statement> stmts, Map<String, Variable> vars) {
        this.stmts = stmts;
        this.vars = vars;
    }
}
