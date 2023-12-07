package ast;

import ast.interfaces.Statement;

import java.util.LinkedList;

public class CallStatement implements Statement {
    public Statement function;
    public LinkedList<LinkedList<Statement>> arguments;

    public CallStatement() {
        arguments = new LinkedList<>();
    }

    @Override
    public String getType() {
        return "CallStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s + "Function Invocation\n");
        sb.append(s + "    function:\n");
        sb.append(function.getString(s + "        "));

        for (int j = 0; j < arguments.size(); j++) {
            int len = arguments.get(j).size();
            sb.append(s + "        Layer" + (j + 1) + "\n");
            for (int i = 0; i < len; i++) {
                sb.append(s + "            Argument" + (i + 1) + "\n");
                sb.append(arguments.get(j).get(i).getString(s + "                "));
            }
        }


        return sb.toString();
    }
}
