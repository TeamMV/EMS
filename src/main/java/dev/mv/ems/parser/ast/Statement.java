package dev.mv.ems.parser.ast;

import dev.mv.ems.runtime.Visitor;

public interface Statement {

    default void visit(Visitor visitor) {
        if (this instanceof Assignment a) visitor.visitAssignment(a);
        if (this instanceof Block b) visitor.visitBock(b);
        if (this instanceof Break b) visitor.visitBreak(b);
        if (this instanceof Continue c) visitor.visitContinue(c);
        if (this instanceof While w) visitor.visitWhile(w);
        if (this instanceof If i) visitor.visitIf(i);
        if (this instanceof For f) visitor.visitFor(f);
    }

}
