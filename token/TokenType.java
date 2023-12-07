package token;

import java.util.HashMap;
import java.util.HashSet;

public class TokenType {
    // 判断某一字符串是否是关键字或符号
    public static HashMap<String, String> types = new HashMap<>();
    // 判断类似 ||, && 这种两个字符且第一个字符单独存在不能作为一种运算符的运算符
    public static HashSet<String> twoOpr = new HashSet<>();
    // 符号

    // 赋值符号
    public static final String ASSIGN = "ASSIGN";
    // 加号
    public static final String ADD = "ADD";
    // 减号
    public static final String SUB = "SUB";
    // 乘号
    public static final String MUL = "MUL";
    // 除号
    public static final String DIV = "DIV";
    // 大于号
    public static final String GT = "GT";
    // 大于等于
    public static final String GE = "GE";
    // 小于号
    public static final String LT = "LT";
    //小于等于
    public static final String LE = "LE";
    // 相等
    public static final String EQU = "EQU";
    // 不相等
    public static final String NEQU = "NEQU";
    // 相反
    public static final String OPPOSITE = "OPPOSITE";
    // 逻辑或
    public static final String OR = "OR";
    // 逻辑与
    public static final String AND = "AND";
    // 逗号
    public static final String COMMA = "COMMA";
    // 分号
    public static final String SEMICOLON = "SEMICOLON";
    // 冒号
    public static final String COLONS = "COLONS";
    // 左小括号 (
    public static final String LPAREN = "LPAREN";
    // 右小括号 )
    public static final String RPAREN = "RPAREN";
    // 左中括号 [
    public static final String LSBRACE = "LSBRACE";
    // 右中括号 ]
    public static final String RSBRACE = "RSBRACE";
    // 左大括号 {
    public static final String LBRACE = "LBRACE";
    // 右大括号
    public static final String RBRACE = "RBRACE";
    // 注释
    public static final String COMMENT = "COMMENT";
    // 结尾
    public static final String EOF = "EOF";

    //关键字

    // 不合法
    public static final String ILLEGAL = "ILLEGAL";
    // 函数
    public static final String FUNCTION = "FUNCTOIN";
    // 赋值
    public static final String VAR = "VAR";
    // true
    public static final String TRUE = "TRUE";
    // false
    public static final String FALSE = "FALSE";
    // if
    public static final String IF = "IF";
    // else
    public static final String ELSE = "ELSE";
    // return
    public static final String RETURN = "RETURN";
    // 整数
    public static final String INT = "INT";
    // 浮点数
    public static final String FLOAT = "FLOAT";
    // 变量
    public static final String IDEN = "IDEN";
    // 字符串
    public static final String STRING = "STRING";
    // 列表
    public static final String LIST = "LIST";
    // 字典
    public static final String DICT = "DICT";


    // 函数调用
    public static final String CALLFUNC = "CALLFUNC";

    // 内置函数
    public static final String BUILDIN = "BUILDIN";

    // 退出

    public static final String EXIT = "EXIT";

    static {
        types.put("=", ASSIGN);
        types.put("+", ADD);
        types.put("-", SUB);
        types.put("*", MUL);
        types.put("/", DIV);
        types.put(">", GT);
        types.put(">=", GE);
        types.put("<", LT);
        types.put("<=", LE);
        types.put("==", EQU);
        types.put("!=", NEQU);
        types.put("~", OPPOSITE);
        types.put("||", OR);
        types.put("&&", AND);
        types.put(",", COMMA);
        types.put(";", SEMICOLON);
        types.put(":", COLONS);
        types.put("(", LPAREN);
        types.put(")", RPAREN);
        types.put("[", LSBRACE);
        types.put("]", RSBRACE);
        types.put("{", LBRACE);
        types.put("}", RBRACE);
        types.put("fn", FUNCTION);
        types.put("var", VAR);
        types.put("true", TRUE);
        types.put("false", FALSE);
        types.put("if", IF);
        types.put("else", ELSE);
        types.put("return", RETURN);

        types.put("len", BUILDIN);
        types.put("type", BUILDIN);

        types.put("exit", EXIT);

        twoOpr.add("!");
        twoOpr.add("|");
        twoOpr.add("&");
    }
}
