package dev.mv.ems.parser.ast;

public interface Expression {

    Type inferType();

    //Type calcType(variables);
    //bool bvalue();
    //int ivalue();
    //float fvalue();

}
