package dev.mv.ems.parser.ast;

import dev.mv.ems.runtime.Visitor;

import java.util.Map;

public interface Expression {

    Type inferType(Map<String, Type> vars);

    boolean collapsible();

    Literal collapse();

    default void visit(Visitor visitor) {
        if (this instanceof Call c) visitor.visitCall(c);
        if (this instanceof Ident i) visitor.visitIdent(i);
        if (this instanceof Literal l) visitor.visitLiteral(l);
        if (this instanceof Unary u) visitor.visitUnary(u);
        if (this instanceof Binary b) visitor.visitBinary(b);
        if (this instanceof ExpressionStatement s) visitor.visitExpressionStatement(s);
    }

}
