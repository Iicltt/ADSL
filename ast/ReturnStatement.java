package ast;

import ast.interfaces.Statement;
import token.Token;

public class ReturnStatement implements Statement {
    public Token token;
    public Statement returnValue;

    @Override
    public String getType() {
        return "ReturnStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s + "Return Statement\n");
        sb.append(s + "    name:\n");
        sb.append(s + "        " + token.value + "\n");
        sb.append(s + "    value:\n");
        sb.append(returnValue.getString(s + "        "));
        return sb.toString();
    }
}
