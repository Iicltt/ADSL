package ast;

import ast.interfaces.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DictStatement implements Statement {
    public HashMap<StringStatement, Statement> dict;

    public DictStatement() {
        dict = new HashMap<>();
    }

    @Override
    public String getType() {
        return "DictStatement";
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append(s + "Dict Statement\n");

        Iterator<Map.Entry<StringStatement, Statement>> it = dict.entrySet().iterator();
        int cnt = 1;

        while (it.hasNext()) {
            Map.Entry<StringStatement, Statement> entry = it.next();

            sb.append(s + "    Element" + cnt + "\n");
            cnt++;
            sb.append(s + "    " + "    " + "key:\n");
            sb.append(entry.getKey().getString(s + "    " + "    " + "    "));
            sb.append(s + "    " + "    " + "value:\n");
            sb.append(entry.getValue().getString(s + "    " + "    " + "    "));
        }
        return sb.toString();
    }
}
