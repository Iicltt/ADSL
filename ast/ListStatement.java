package ast;

import ast.interfaces.Statement;

import java.util.LinkedList;

public class ListStatement implements Statement {
    public LinkedList<Statement> store;

    public ListStatement() {
        store = new LinkedList<>();
    }

    @Override
    public String getType() {
        return "ListStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s + "List\n");

        for (int i = 0; i < store.size(); i++) {
            sb.append(s + "    Element" + (i + 1) + "\n");
            sb.append(store.get(i).getString(s + "    " + "    "));
        }
        return sb.toString();
    }
}
