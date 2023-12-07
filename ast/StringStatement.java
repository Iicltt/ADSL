package ast;

import ast.interfaces.Statement;

public class StringStatement implements Statement {
    public String content;

    public StringStatement(String s) {
        content = s;
    }

    @Override
    public String getType() {
        return "StringStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s + "String Statement\n");
        sb.append(s + "    value:\n");
        sb.append(s + "    " + "    " + content + "\n");
        return sb.toString();
    }
}
