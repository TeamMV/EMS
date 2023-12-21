package dev.mv.ems.parser.ast;

import java.util.Map;

public interface Expression {

    Type inferType(Map<String, Type> vars);

    //bool bvalue();
    //int ivalue();
    //float fvalue();

}
