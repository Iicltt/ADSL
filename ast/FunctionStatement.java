package ast;

import ast.interfaces.Statement;

import java.util.LinkedList;

public class FunctionStatement implements Statement {
    public LinkedList<Statement> parameters;
    public BlockStatement body;

    public FunctionStatement() {
        parameters = new LinkedList<>();
    }

    @Override
    public String getType() {
        return "FunctionStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s + "Function Definition\n");
        sb.append(s + "    Parameters:\n");
        int len = parameters.size();

        for (int i = 0; i < len; i++) {
            sb.append(s + "        Para" + (i + 1) + "\n");
            sb.append(parameters.get(i).getString(s + "            "));
        }

        sb.append(s + "    Body:\n");
        sb.append(body.getString(s + "        "));

        return sb.toString();
    }
}
