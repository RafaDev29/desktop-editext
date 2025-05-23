package editor.utils;

import editor.tokens.Token;
import editor.tokens.TokenType;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class Highlighter {

    public static void applyHighlighting(JTextPane textPane, List<Token> tokens) {
        StyledDocument doc = textPane.getStyledDocument();
        StyleContext context = StyleContext.getDefaultStyleContext();

        // Limpia el estilo previo
        doc.setCharacterAttributes(0, doc.getLength(), context.getEmptySet(), true);

        for (Token token : tokens) {
            Style style = doc.addStyle("style", null);

            switch (token.getType()) {
                case KEYWORD:
                    StyleConstants.setForeground(style, new Color(0, 0, 200)); // azul
                    StyleConstants.setBold(style, true);
                    break;
                case IDENTIFIER:
                    StyleConstants.setForeground(style, Color.BLACK);
                    break;
                case NUMBER:
                    StyleConstants.setForeground(style, new Color(0, 128, 0)); // verde
                    break;
                case OPERATOR:
                    StyleConstants.setForeground(style, Color.RED);
                    break;
                case ERROR:
                    StyleConstants.setForeground(style, Color.WHITE);
                    StyleConstants.setBackground(style, Color.RED);
                    break;
                default:
                    StyleConstants.setForeground(style, Color.GRAY);
                    break;
            }

            int startOffset = getOffset(textPane, token.getLine(), token.getColumn());
            int length = token.getValue().length();

            if (startOffset >= 0 && startOffset + length <= doc.getLength()) {
                doc.setCharacterAttributes(startOffset, length, style, true);
            }
        }
    }

    // Convierte línea/columna a posición absoluta del JTextPane
    private static int getOffset(JTextPane textPane, int line, int column) {
        String[] lines = textPane.getText().split("\n");
        if (line < 1 || line > lines.length) return -1;

        int offset = 0;
        for (int i = 0; i < line - 1; i++) {
            offset += lines[i].length() + 1; // +1 por el '\n'
        }

        return offset + (column - 1);
    }
}
