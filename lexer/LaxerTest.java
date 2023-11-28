package lexer;

import token.Token;
import token.TokenType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class LaxerTest {
    public static void main(String[] args) throws IOException {
        String filePath = "test/laxerTest";
        Path path = Paths.get(filePath);
        byte[] data = Files.readAllBytes(path);
        String input = new String(data);
        //System.out.println(input);
        Lexer lexer = new Lexer(input);

        Token t = lexer.nextToken();

        while (!Objects.equals(t.type, TokenType.EXIT))
        {
            System.out.println(t.toString());
            t = lexer.nextToken();
        }
    }
}
