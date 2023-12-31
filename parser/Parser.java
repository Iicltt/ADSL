package parser;

import ast.*;
import ast.interfaces.Statement;
import lexer.Lexer;
import token.Token;
import token.TokenType;

import java.util.LinkedList;
import java.util.Objects;

public class Parser {
    private Lexer lexer;
    private Token curToken;

    public Parser(Lexer l) {
        lexer = l;
        // 读取第一个Token至curToken
        nextToken();
    }

    private void nextToken() {
        curToken = lexer.nextToken();

        //跳过注释
        while (curToken != null && Objects.equals(curToken.type, TokenType.COMMENT))
            nextToken();
    }

    public boolean isEnd() {
        return lexer.isEnd();
    }

    /**
     * The grammar
     * <p>
     * program ::= <program-block>
     * program-block ::= <block>
     * block ::= {single-statement}*
     * single-statement =[ [<Var-statement> | <callFunction-Statement> |
     * <function-definition>(和callFunction有冲突) | <expression-statement> | <return-statement> | <identifier> ';' (和callFunction可能有冲突)]
     * | [<if-statment>]]
     * <p>
     * Var Statement
     * Var-Statement ::= 'var' <identifier> '=' (<callFunction-Statement> | <expression-statement> | <identifier>)
     * Var-Statement ::= 'var' <identifier> [function-definition]
     * <p>
     * Function Invocation:
     * callFunction-Statement ::= [<identifier> | <function-definition> ] '('<parameters>')'
     * parameters ::= {<expression-statement> | callFunction}*
     * <p>
     * If-else Statement:
     * if-statement ::= 'if' '{' <program-block> '}'  'else' '{' <program-block> '}'
     * <p>
     * Function Definition Statement:
     * function-definition ::= 'fn' '(' <arguments> ')' '{' <function-body> '}'
     * <arguments> ::= [<identifier> | expression-statement | callFunction-statement] {',' [<identifier> | expression-statement | callFunction-statement] }*
     * <function-body> ::= <program-block>
     * <p>
     * Return Statement:
     * <return-statement> := 'return' [function]
     * Expression Statement:
     * <expression-statement> ::= <compare> '==' <expression-statement> | <compare>  '!=' <expression-statement> | <compare>
     * <compare> ::=  <arithmetic> '>' <compare> | <arithmetic> '<' <compare> | <arithmetic>
     * <arithmetic> ::= <priority-arithmetic> '+' <arithmetic> | <priority-arithmetic> '-' <arithmetic> | <priority-arithmetic>
     * <priority-arithmetic> ::= <number> '*' <priority-arithmetic> | <number> '/' <priority-arithmetic> | <number> | <list> | <dict>
     * | identifier | <callFunction-statement>
     *
     * <number> ::= {~}[<integer> | <float> | true | false | (<expression-statement>)] | identifier | callFunction-statement
     *
     * <list> ::= '[' expression-statement (',' expression-statement) * ']'
     *
     * <dict> ::= '{' (<identifier> ':' expressoinStatement) * '}'
     * Number or Identifier:
     * <unsigned integer> ::= <digit> | <unsigned integer> <digit>
     * <integer> ::= +<unsigned integer>  | ~<unsigned integer> | <unsigned integer>
     * <identifier> ::= <letter> | <identifier> < digit> | <identifier> <letter>
     * <float> := <digit>'.'<digit>
     *
     * @return Statement
     */


    public Statement getNextStatement() {
        // 从文件输入源码，直接run Test，sourceCode为Ture
        if (lexer.sourceCode) {
            if (lexer.isEnd())
                return null;
            else
                return getStatement();
        } else {
            int cnt = 0;

            while (checkType(curToken.type, TokenType.ILLEGAL)) {
                nextToken();

                cnt++;

                if (cnt > 10 && checkType(curToken.type, TokenType.ILLEGAL))
                    return null;
            }

            return getStatement();
        }


    }

    // 语句结束后都要接上一个分号，不包括if语句，函数定义语句属于ExpressionStatement，也要加分号
    private Statement getStatement() {
        Statement s;
        if (checkType(curToken.type, TokenType.VAR)) {
            s = parseVarStatement();
            // 检测分号;
            if (!checkSemicolon())
                s = null;
        } else if (checkType(curToken.type, TokenType.RETURN)) {
            s = parseReturnStatement();
            if (!checkSemicolon())
                s = null;
        } else if (checkType(curToken.type, TokenType.IF)) {
            s = parseIfStatement();
        } else {
            s = parseExpressionStatement();
            if (!checkSemicolon())
                s = null;
        }
        return s;
    }

    private Statement getNextStatementWithoutCheck() {
        if (lexer.isEnd())
            return null;
        else {
            Statement s;
            if (checkType(curToken.type, TokenType.VAR)) {
                s = parseVarStatement();
            } else if (checkType(curToken.type, TokenType.RETURN)) {
                s = parseReturnStatement();
            } else if (checkType(curToken.type, TokenType.IF)) {
                s = parseIfStatement();
            } else {
                s = parseExpressionStatement();
            }

            return s;
        }
    }


    private Statement parseVarStatement() {
        // 类型检查
        if (!checkType(curToken.type, TokenType.VAR)) {
            error(curToken.type, TokenType.VAR, curToken.pos);
            return null;
        }

        VarStatement varStatement = new VarStatement();

        varStatement.token = new Token(curToken);

        nextToken();

        // 检查var后是否为标识符
        if (!checkType(curToken.type, TokenType.IDEN)) {
            error(curToken.type, TokenType.IDEN, curToken.pos);
            return null;
        }

        // 变量名
        varStatement.name = new Token(curToken);

        nextToken();

        // 检查标识符后是否为赋值符号'='或者为'$'，如果为'$'即为函数
        if (checkType(curToken.type, TokenType.FUNCTION)) {
            varStatement.value = parseExpressionStatement();
            return varStatement;
        } else if (!checkType(curToken.type, TokenType.ASSIGN)) {
            error(curToken.type, TokenType.IDEN, curToken.pos);
            return null;
        }

        nextToken();

        varStatement.value = switch (curToken.type) {
            case TokenType.VAR -> parseVarStatement();
            case TokenType.IF -> parseIfStatement();
            case TokenType.RETURN -> parseReturnStatement();
            default -> parseExpressionStatement();
        };

        return varStatement;
    }


    private Statement parseIdentifier() {
        if (!checkType(curToken.type, TokenType.IDEN)) {
            error(curToken.type, TokenType.IDEN, curToken.pos);
            return null;
        }


        IdentifierStatement identifierStatement = new IdentifierStatement(curToken);

        Statement res = identifierStatement;

        nextToken();

        if (checkType(curToken.type, TokenType.LPAREN)) {
            CallStatement callStatement = new CallStatement();
            callStatement.function = identifierStatement;

            while (checkType(curToken.type, TokenType.LPAREN)) {
                callStatement.arguments.add(parseArguments());
            }

            res = callStatement;
        } else if (checkType(curToken.type, TokenType.LSBRACE)) {
            GetElementStatement getElementStatement = new GetElementStatement();
            getElementStatement.listName = identifierStatement;

            nextToken();

            // 得到list所有元素
            getElementStatement.listIndex = parseExpressionStatement();

            if (!checkType(curToken.type, TokenType.RSBRACE)) {
                error(curToken.type, TokenType.RSBRACE, curToken.pos);
                return null;
            }

            nextToken();

            res = getElementStatement;
        }

        return res;
    }


    private LinkedList<Statement> parseArguments() {
        if (!checkType(curToken.type, TokenType.LPAREN)) {
            error(curToken.type, TokenType.LPAREN, curToken.pos);
            return null;
        }

        LinkedList<Statement> res = new LinkedList<>();

        nextToken();

        while (!checkType(curToken.type, TokenType.RPAREN)) {
            Statement s = parseExpressionStatement();

            if (!(s instanceof IdentifierStatement || s instanceof ExpressionStatement || s instanceof CallStatement)) {
                error(s.getType(), "Identifier, Expression or CallStatement", curToken.pos);
                return null;
            }

            res.add(s);

            if (checkType(curToken.type, TokenType.RPAREN))
                break;

            if (!checkType(curToken.type, TokenType.COMMA)) {
                error(curToken.type, TokenType.COMMA, curToken.pos);
                return null;
            }

            nextToken();

        }

        nextToken();

        return res;
    }


    private Statement parseIfStatement() {
        if (!checkType(curToken.type, TokenType.IF)) {
            error(curToken.type, TokenType.IF, curToken.pos);
            return null;
        }

        IfStatement ifStatement = new IfStatement();

        ifStatement.token = new Token(curToken);

        nextToken();

        if (!checkType(curToken.type, TokenType.LPAREN)) {
            error(curToken.type, TokenType.LPAREN, curToken.pos);
            return null;
        }

        ifStatement.condition = parseExpressionStatement();

        ifStatement.ifCondition = parseBlockStatement();

        if (checkType(curToken.type, TokenType.ELSE)) {
            nextToken();
            ifStatement.elseCondition = parseBlockStatement();
        } else {
            ifStatement.elseCondition = null;
        }

        return ifStatement;

    }


    private BlockStatement parseBlockStatement() {
        if (!checkType(curToken.type, TokenType.LBRACE)) {
            error(curToken.type, TokenType.LBRACE, curToken.pos);
            return null;
        }

        nextToken();

        BlockStatement bls = new BlockStatement();

        while (!checkType(curToken.type, TokenType.RBRACE)) {
            Statement s = getNextStatement();
            bls.statements.add(s);
        }

        // 检查 '}'
        if (!checkType(curToken.type, TokenType.RBRACE)) {
            error(curToken.type, TokenType.RBRACE, curToken.pos);
            return null;
        }

        nextToken();

        return bls;
    }


    private Statement parseFunctionStatement() {
        if (!checkType(curToken.type, TokenType.FUNCTION)) {
            error(curToken.type, TokenType.FUNCTION, curToken.pos);
            return null;
        }

        Statement res;
        FunctionStatement functionStatement = new FunctionStatement();

        nextToken();

        functionStatement.parameters = parseArguments();


        if (!checkType(curToken.type, TokenType.LBRACE)) {
            error(curToken.type, TokenType.LBRACE, curToken.pos);
            return null;
        }

        functionStatement.body = parseBlockStatement();

        res = functionStatement;

        // unknown
        if (checkType(curToken.type, TokenType.LPAREN)) {
            CallStatement callStatement = new CallStatement();
            callStatement.function = functionStatement;

            while (checkType(curToken.type, TokenType.LPAREN)) {
                callStatement.arguments.add(parseArguments());
            }

            res = callStatement;
        }

        return res;
    }


    private Statement parseReturnStatement() {
        if (!checkType(curToken.type, TokenType.RETURN)) {
            error(curToken.type, TokenType.RETURN, curToken.pos);
            return null;
        }

        ReturnStatement returnStatement = new ReturnStatement();
        returnStatement.token = new Token(curToken);
        nextToken();

        returnStatement.returnValue = parseExpressionStatement();

        return returnStatement;
    }

    /**
     * Parse Expressions.
     * In the following patern:
     * A  := B A'
     * A' := == B A' | != BA' | && BA' | || BA' | null
     * B  := C B'
     * B' := > C B' | < C B' | null
     * C  := D C'
     * C' := +DC' | -DC' | null
     * D  := ED'
     * D' := *ED' | /ED' | null
     * E  := (A) | num | iden | ~E
     *
     * @return Statement
     */

    private Statement parseExpressionStatement() {
        return A();
    }

    /**
     * Top layer.
     *
     * @return Statement
     */
    private Statement A() {
        ExpressionStatement root = new ExpressionStatement();
        root.left = B();

        Statement tmp = A_1(root);

        return tmp == null ? root.left : tmp;

    }

    /**
     * Second top.
     *
     * @param root ExpressionStatement
     * @return Statement
     */
    private Statement A_1(ExpressionStatement root) {
        if (checkType(curToken.type, TokenType.EQU) || checkType(curToken.type, TokenType.NEQU) ||
                checkType(curToken.type, TokenType.AND) || checkType(curToken.type, TokenType.OR)) {
            root.opr = new Token(curToken);
            nextToken();
            root.right = B();
            ExpressionStatement root1 = new ExpressionStatement();
            root1.left = root;
            Statement tmp = A_1(root1);

            return tmp == null ? root : tmp;
        } else
            return null;
    }

    private Statement B() {
        ExpressionStatement root = new ExpressionStatement();
        root.left = C();
        Statement tmp = B_1(root);

        return tmp == null ? root.left : tmp;

    }

    private Statement B_1(ExpressionStatement root) {
        if (checkType(curToken.type, TokenType.GT) || checkType(curToken.type, TokenType.LT)) {
            root.opr = new Token(curToken);
            nextToken();
            root.right = C();
            ExpressionStatement root1 = new ExpressionStatement();
            root1.left = root;
            Statement tmp = B_1(root1);

            return tmp == null ? root : tmp;

        } else
            return null;
    }

    private Statement C() {
        ExpressionStatement root = new ExpressionStatement();
        root.left = D();
        Statement tmp = C_1(root);

        return tmp == null ? root.left : tmp;
    }

    private Statement C_1(ExpressionStatement root) {
        if (checkType(curToken.type, TokenType.ADD) || checkType(curToken.type, TokenType.SUB)) {
            root.opr = new Token(curToken);
            nextToken();
            root.right = D();
            ExpressionStatement root1 = new ExpressionStatement();
            root1.left = root;
            Statement tmp = C_1(root1);

            return tmp == null ? root : tmp;
        } else
            return null;
    }

    private Statement D() {
        ExpressionStatement root = new ExpressionStatement();
        root.left = E();
        Statement tmp = D_1(root);

        return tmp == null ? root.left : tmp;
    }

    private Statement D_1(ExpressionStatement root) {
        if (checkType(curToken.type, TokenType.MUL) || checkType(curToken.type, TokenType.DIV)) {
            root.opr = new Token(curToken);
            nextToken();
            root.right = E();
            ExpressionStatement root1 = new ExpressionStatement();
            root1.left = root;
            Statement tmp = D_1(root1);

            return tmp == null ? root : tmp;
        } else
            return null;
    }


    private Statement E() {
        Statement res = null;

        if (checkType(curToken.type, TokenType.OPPOSITE)) {
            ExpressionStatement ops = new ExpressionStatement();
            ops.opr = new Token(curToken);
            nextToken();
            ops.left = E();
            ops.right = null;
            res = ops;
        } else {
            if (checkType(curToken.type, TokenType.INT) || checkType(curToken.type, TokenType.FLOAT) ||
                    checkType(curToken.type, TokenType.TRUE) || checkType(curToken.type, TokenType.FALSE)) {
                ExpressionStatement expressionStatement = new ExpressionStatement();
                expressionStatement.opr = new Token(curToken);
                nextToken();
                expressionStatement.left = expressionStatement.right = null;
                expressionStatement.isLeaf = true;
                res = expressionStatement;
            } else if (checkType(curToken.type, TokenType.IDEN)) {
                res = parseIdentifier();
            } else if (checkType(curToken.type, TokenType.FUNCTION)) {
                res = parseFunctionStatement();
            } else if (checkType(curToken.type, TokenType.LPAREN)) {
                nextToken();

                res = A();

                if (!checkType(curToken.type, TokenType.RPAREN)) {
                    error(curToken.type, TokenType.RPAREN, curToken.pos);
                    res = null;
                }

                nextToken();
            } else if (checkType(curToken.type, TokenType.LSBRACE)) {
                res = perseListStatement();
            } else if (checkType(curToken.type, TokenType.STRING)) {
                res = new StringStatement(curToken.value);
                nextToken();
            } else if (checkType(curToken.type, TokenType.LBRACE)) {
                res = parseDictStatement();
            } else if (checkType(curToken.type, TokenType.BUILDIN)) {
                res = parseBuildIn();
            } else {
                error(curToken.type, "INT, Identifier or '('", curToken.pos);
                nextToken();
            }

        }


        return res;
    }


    private Statement perseListStatement() {
        ListStatement res = new ListStatement();

        // 检查当前令牌是否是'['
        if (!checkType(curToken.type, TokenType.LSBRACE)) {
            error(curToken.type, TokenType.LSBRACE, curToken.pos);
            return null;
        }

        nextToken();

        while (!checkType(curToken.type, TokenType.RSBRACE)) {
            Statement element = parseExpressionStatement();

            res.store.add(element);

            if (checkType(curToken.type, TokenType.RSBRACE))
                break;

            // 检查当前令牌是否是','
            if (!checkType(curToken.type, TokenType.COMMA)) {

                error(curToken.type, TokenType.COMMA, curToken.pos);
                return null;
            }

            nextToken();
        }


        // 检查当前令牌是否是']'
        if (!checkType(curToken.type, TokenType.RSBRACE)) {
            error(curToken.type, TokenType.COMMA, curToken.pos);
            return null;
        }

        nextToken();

        return res;
    }


    private Statement parseDictStatement() {
        DictStatement res;

        if (!checkType(curToken.type, TokenType.LBRACE)) {
            error(curToken.type, TokenType.LBRACE, curToken.pos);
            return null;
        }

        nextToken();

        res = new DictStatement();

        while (!checkType(curToken.type, TokenType.RBRACE)) {
            if (!checkType(curToken.type, TokenType.STRING)) {
                error(curToken.type, "Dict's key should be STRING", curToken.pos);
                return res;
            }

            StringStatement key = (StringStatement) parseExpressionStatement();

            if (!checkType(curToken.type, TokenType.COLONS)) {
                error(curToken.type, TokenType.COLONS, curToken.pos);
                return null;
            }

            nextToken();

            Statement value = parseExpressionStatement();

            res.dict.put(key, value);

            if (checkType(curToken.type, TokenType.RBRACE))
                break;

            if (!checkType(curToken.type, TokenType.COMMA)) {
                error(curToken.type, TokenType.COMMA, curToken.pos);
                return null;
            }

            nextToken();
        }

        if (!checkType(curToken.type, TokenType.RBRACE)) {
            error(curToken.type, TokenType.RBRACE, curToken.pos);
            return null;
        }

        nextToken();

        return res;
    }

    private Statement parseBuildIn() {
        Statement res;


        if (!checkType(curToken.type, TokenType.BUILDIN)) {
            error(curToken.type, TokenType.BUILDIN, curToken.pos);
            return null;
        }

        BuildInStatement len = new BuildInStatement();
        len.fun = new Token(curToken);

        nextToken();

        if (!checkType(curToken.type, TokenType.LPAREN)) {
            error(curToken.type, TokenType.LPAREN, curToken.pos);
            return null;
        }

        nextToken();

        len.lenObj = getNextStatementWithoutCheck();


        if (!checkType(curToken.type, TokenType.RPAREN)) {
            error(curToken.type, TokenType.RPAREN, curToken.pos);
            return null;
        }

        nextToken();

        res = len;

        return res;
    }


    // 类型检查
    private boolean checkType(String typeA, String typeB) {
        return Objects.equals(typeA, typeB);
    }

    //错误信息提示
    private void error(String curType, String expectType, int pos) {

        System.out.println("[ERROR] Line " + pos + ": " + "Current type:" + curType + " Expect type:" + expectType);
    }

    // 分号检查
    private boolean checkSemicolon() {
        if (!checkType(curToken.type, TokenType.SEMICOLON)) {
            error(curToken.type, TokenType.SEMICOLON, curToken.pos);
            return false;
        }
        nextToken();
        return true;
    }

}


