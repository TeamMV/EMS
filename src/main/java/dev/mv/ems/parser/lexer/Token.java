package dev.mv.ems.parser.lexer;

public enum Token {
    UNKNOWN,
    IDENT,
    ILITERAL,
    FLITERAL,
    TRUE,
    FALSE,

    IF,
    ELSE,
    WHILE,
    FOR,
    FROM,
    TO,

    LPAREN,
    RPAREN,
    COMMA,

    ASSIGN,
    OPERATOR,
    OPERATOR_ASSIGN,

    BREAK,
    CONTINUE,

    DO,
    END,
    EOF;

    private Object value;

    public Object getValue() {
        return value;
    }

    public <T> T getValueAs() {
        return (T) value;
    }

    public Token setValue(Object value) {
        this.value = value;
        return this;
    }

    Token() {
        this.value = null;
    }
}
