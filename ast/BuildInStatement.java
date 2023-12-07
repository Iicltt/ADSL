package ast;

import ast.interfaces.BuildIn;
import ast.interfaces.Statement;
import token.Token;

import java.util.Objects;

public class BuildInStatement implements BuildIn {
    public Token fun;
    public Statement lenObj;

    @Override
    public String buildIn() {
        return fun.value;
    }

    @Override
    public String getString(String s) {
        StringBuilder sb = new StringBuilder();
        if (Objects.equals(buildIn(), "len"))
            sb.append(s + "BuildIn function: len()\n");
        else if (Objects.equals(buildIn(), "type"))
            sb.append(s + "BuildIn function: type()\n");
        sb.append(lenObj.getString(s + "    "));
        return sb.toString();
    }

    @Override
    public String getType() {
        return "BuildInStatement";
    }
}
