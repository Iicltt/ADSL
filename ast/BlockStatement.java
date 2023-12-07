package ast;

import ast.interfaces.Statement;

import java.util.LinkedList;

public class BlockStatement implements Statement {
    public LinkedList<Statement> statements;

    public BlockStatement() {
        statements = new LinkedList<>();
    }

    @Override
    public String getType() {
        return "BlockStatement";
    }

    @Override
    public String getString(String s) {

        StringBuilder sb = new StringBuilder();

        sb.append(s + "BlockStatement\n");
        int len = statements.size();

        for (int i = 0; i < len; i++) {
            sb.append(s + "    Statement" + (i + 1) + "\n");
            sb.append(statements.get(i).getString(s + "        "));
        }
        return sb.toString();
    }
}
