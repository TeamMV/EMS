package dev.mv.ems.runtime;

import dev.mv.ems.parser.ast.*;

import java.util.Map;

public class Visitor {

    Map<String, Variable> vars;
    Variable last = new Variable("", Type.UNKNOWN);
    boolean brk, ctn, loop;

    public Visitor(Map<String, Variable> vars) {
        this.vars = vars;
    }

    public void visitStatement(Statement stmt) {
        stmt.visit(this);
    }

    public void visitAssignment(Assignment a) {
        Variable v = vars.get(a.name);
        a.value.visit(this);
        v.type = last.type;
        v.value = last.value;
    }

    public void visitBock(Block b) {
        for (Statement s : b.stmts) {
            s.visit(this);
            if (brk) break;
            if (ctn) {
                ctn = false;
                break;
            }
        }
    }

    public void visitBreak(Break b) {
        if (!loop) return;
        brk = true;
    }

    public void visitContinue(Continue c) {
        if (!loop) return;
        ctn = true;
    }

    public void visitWhile(While w) {
        loop = true;
        w.cond.visit(this);
        while (last.value != 0) {
            w.body.visit(this);
            if (brk) {
                brk = false;
                break;
            }
            w.cond.visit(this);
        }
        loop = false;
    }

    public void visitFor(For f) {
        loop = true;
        f.begin.visit(this);
        long start = (long) last.getValueAsD();
        f.end.visit(this);
        long end = (long) last.getValueAsD();
        Variable v = new Variable(f.ident, Type.INT);
        vars.put(f.ident, v);
        for (long i = start; i <= end; i++) {
            v.value = i;
            f.body.visit(this);
            if (brk) {
                brk = false;
                break;
            }
        }
        loop = false;
    }

    public void visitIf(If i) {
        i.cond.visit(this);
        if (last.value != 0) {
            i.body.visit(this);
        }
        else if (i.else_body != null) {
            i.else_body.visit(this);
        }
    }

    public void visitExpressionStatement(ExpressionStatement s) {
        s.expr.visit(this);
    }

    public void visitExpression(Expression expr) {
        expr.visit(this);
    }

    public void visitCall(Call c) {
        double[] args = new double[c.args.size()];
        for (int i = 0; i < c.args.size(); i++) {
            c.args.get(i).visit(this);
            args[i] = last.getValueAsD();
        }
        double res = c.function.evaluate(args);
        last.type = c.function.type();
        if (last.type == Type.FLOAT) {
            last.setValue(res);
        }
        else {
            last.value = (long) res;
        }
    }

    public void visitIdent(Ident i) {
        Variable v = vars.get(i.name);
        last.type = v.type;
        last.value = v.value;
    }

    public void visitLiteral(Literal l) {
        last.type = l.type;
        last.value = l.value;
    }

    public void visitUnary(Unary u) {
        u.expr.visit(this);
        switch (last.type) {
            case BOOL -> last.setValue(u.op.apply(last.getValueB()));
            case FLOAT -> last.setValue(u.op.apply(last.getValueD()));
            case INT -> last.setValue(u.op.apply(last.getValueL()));
        }
    }

    public void visitBinary(Binary e) {
        Operator op = e.op;
        e.a.visit(this);
        Variable a = last.clone();
        e.b.visit(this);
        Variable b = last.clone();
        if (op.isComp()) {
            last.type = Type.BOOL;
            last.value = switch (chooseType(a, b)) {
                case FLOAT -> op.applyCond(a.getValueAsD(), b.getValueAsD());
                case INT -> op.applyCond(a.getValueL(), b.getValueL());
                case BOOL -> op.apply(a.getValueB(), b.getValueB());
                default -> false;
            } ? 1 : 0;
        }
        else {
            Type t = chooseType(a, b);
            last.type = t;
            last.value = switch (t) {
                case FLOAT -> Double.doubleToLongBits(op.apply(a.getValueAsD(), b.getValueAsD()));
                case INT -> op.apply(a.getValueL(), b.getValueL());
                case BOOL -> op.apply(a.getValueB(), b.getValueB()) ? 1 : 0;
                default -> 0;
            };
        }
    }

    private Type chooseType(Variable a, Variable b) {
        if (a.type == b.type) return a.type;
        if (a.type == Type.FLOAT || b.type == Type.FLOAT) return Type.FLOAT;
        return Type.INT;
    }
}
