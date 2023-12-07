package ast;

import ast.interfaces.Statement;
import token.Token;
import token.TokenType;

public class ExpressionStatement implements Statement {
    public Token opr;
    public Statement left;
    public Statement right;
    public boolean isLeaf = false;

    @Override
    public String getType() {
        return "ExpressionStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        if (isLeaf) {
            sb.append(s + "Experssion\n");
            sb.append(s + "    value:\n");
            sb.append(s + "        " + opr.value + "\n");
        } else if (opr.type.equals(TokenType.OPPOSITE)) {
            sb.append(s + "Oppsite\n");
            sb.append(left.getString(s + "\t"));
        } else {
            sb.append(s + "Experssion\n");
            sb.append(s + "    Operatioin:\n");
            sb.append(s + "        " + opr.type + "\n");
            sb.append(s + "    Left:\n");
            sb.append(left.getString(s + "        "));
            sb.append(s + "    Right:\n");
            sb.append(right.getString(s + "        "));
        }

        return sb.toString();

    }
}
