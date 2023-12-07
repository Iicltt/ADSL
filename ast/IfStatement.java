package ast;

import ast.interfaces.Statement;
import token.Token;

public class IfStatement implements Statement {
    public Token token;
    public Statement condition;
    public BlockStatement ifCondition;
    public BlockStatement elseCondition;

    @Override
    public String getType() {
        return "IfStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s + "IF Statement\n");
        sb.append(s + "    condition:\n");
        sb.append(condition.getString(s + "        "));
        sb.append(s + "    IFBlock:\n");
        sb.append(ifCondition.getString(s + "        "));
        sb.append(s + "    ElseBlock:\n");
        sb.append(elseCondition.getString(s + "        "));

        return sb.toString();
    }
}
