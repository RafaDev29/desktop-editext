package editor.lexer;

import editor.tokens.Token;
import editor.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private static final String[] KEYWORDS = {
            "int", "if", "else", "while", "return", "void"
    };

    public List<Token> scan(String input) {
        List<Token> tokens = new ArrayList<>();
        int line = 1;
        int column = 1;
        int i = 0;

        while (i < input.length()) {
            char current = input.charAt(i);

            // Ignorar espacios y saltos
            if (Character.isWhitespace(current)) {
                if (current == '\n') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }
                i++;
                continue;
            }

            // Identificadores o palabras clave
            if (Character.isLetter(current)) {
                int start = i;
                while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                    i++;
                }
                String word = input.substring(start, i);
                TokenType type = isKeyword(word) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
                tokens.add(new Token(type, word, line, column));
                column += word.length();
                continue;
            }

            // Números
            if (Character.isDigit(current)) {
                int start = i;
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    i++;
                }
                String number = input.substring(start, i);
                tokens.add(new Token(TokenType.NUMBER, number, line, column));
                column += number.length();
                continue;
            }

            // Operadores y símbolos simples
            if ("+-*/=;{}()<>".indexOf(current) >= 0) {
                tokens.add(new Token(TokenType.OPERATOR, Character.toString(current), line, column));
                i++;
                column++;
                continue;
            }

            // Si no se reconoce
            tokens.add(new Token(TokenType.ERROR, Character.toString(current), line, column));
            i++;
            column++;
        }

        return tokens;
    }

    private boolean isKeyword(String word) {
        for (String keyword : KEYWORDS) {
            if (keyword.equals(word)) {
                return true;
            }
        }
        return false;
    }
}
