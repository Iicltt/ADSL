package ast;

import ast.interfaces.Statement;
import token.Token;

public class VarStatement implements Statement {
    public Token token;
    public Token name;
    public Statement value;

    @Override
    public String getType() {
        return "VarStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s + "Var Statement\n");
        sb.append(s + "    name:\n");
        sb.append(s + "        " + name.value + "\n");
        sb.append(s + "    value:\n");
        sb.append(value.getString(s + "        ") + "\n");
        return sb.toString();
    }
}
