package dev.mv.ems.parser.ast;

public class Assignment implements Statement {

    public String name;
    public Expression value;

    public Assignment(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " = " + value.toString() + "\n";
    }

}
