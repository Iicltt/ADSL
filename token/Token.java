package token;

public class Token {
    public String type;
    public String value;
    public int pos;

    public Token(String t, String v) {
        type = t;
        value = v;
        pos = 0;
    }

    public Token(String t, String v, int p) {
        this(t, v);
        pos = p;
    }

    public Token(Token t) {
        type = t.type;
        value = t.value;
    }

    public String toString() {
        return value + " " + type;
    }
}
