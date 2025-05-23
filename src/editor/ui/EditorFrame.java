package editor.ui;

import editor.lexer.Lexer;
import editor.tokens.Symbol;
import editor.tokens.Token;
import editor.tokens.TokenType;
import editor.utils.Highlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorFrame extends JFrame {

    private final JTextPane textPane;
    private final JTextArea outputArea;
    private final JTextArea errorArea;
    private final JTextArea symbolArea;
    private final JButton analyzeButton;

    public EditorFrame() {
        setTitle("Editor de Texto - Proyecto Compilador");
        setSize(950, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Editor principal
        textPane = new JTextPane();
        JScrollPane textScroll = new JScrollPane(textPane);

        // Botón de análisis
        analyzeButton = new JButton("Analizar código");
        analyzeButton.addActionListener(this::onAnalyzeClick);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(analyzeButton);

        // Tokens
        outputArea = new JTextArea(8, 80);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBackground(new Color(245, 245, 245));

        // Errores
        errorArea = new JTextArea(6, 80);
        errorArea.setEditable(false);
        errorArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        errorArea.setForeground(Color.RED);
        errorArea.setBackground(new Color(255, 240, 240));

        // Símbolos (identificadores)
        symbolArea = new JTextArea(6, 80);
        symbolArea.setEditable(false);
        symbolArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        symbolArea.setForeground(new Color(0, 102, 153));
        symbolArea.setBackground(new Color(230, 245, 255));

        // Panel de resultados
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.add(new JLabel("Tokens encontrados:"));
        resultPanel.add(new JScrollPane(outputArea));
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(new JLabel("Errores léxicos:"));
        resultPanel.add(new JScrollPane(errorArea));
        resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        resultPanel.add(new JLabel("Tabla de símbolos (identificadores):"));
        resultPanel.add(new JScrollPane(symbolArea));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(textScroll, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void onAnalyzeClick(ActionEvent event) {
        String code = textPane.getText();
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.scan(code);

        outputArea.setText("");
        errorArea.setText("");
        symbolArea.setText("");

        List<Symbol> symbols = new ArrayList<>();

        for (Token token : tokens) {
            outputArea.append(token.toString() + "\n");

            if (token.getType() == TokenType.ERROR) {
                errorArea.append("Error: símbolo inválido '" + token.getValue() +
                        "' en línea " + token.getLine() + ", columna " + token.getColumn() + "\n");
            }

            if (token.getType() == TokenType.IDENTIFIER) {
                symbols.add(new Symbol(token.getValue(), token.getLine(), token.getColumn()));
            }
        }

        // Mostrar tabla de símbolos
        for (Symbol symbol : symbols) {
            symbolArea.append(symbol.toString() + "\n");
        }

        Highlighter.applyHighlighting(textPane, tokens);
    }
}
