package dev.mv.ems.parser.lexer;

public class UnexpectedCharException extends RuntimeException{
    public UnexpectedCharException(char c, int line, int col) {
        super("Unexpected Character '"+c+"' on line " + line + ":" + col);
    }

    public UnexpectedCharException(String msg) {
        super(msg);
    }
}
