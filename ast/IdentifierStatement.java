package ast;

import ast.interfaces.Statement;
import token.Token;

public class IdentifierStatement implements Statement {
    public Token name;

    public IdentifierStatement(Token t) {
        name = t;
    }

    @Override
    public String getType() {
        return "IdentifierStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s + "Identifier\n");
        sb.append(s + "    name:\n");
        sb.append(s + "        " + name.value + "\n");
        return sb.toString();
    }
}
