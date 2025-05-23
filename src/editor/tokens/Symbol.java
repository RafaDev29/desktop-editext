package editor.tokens;

public class Symbol {
    private final String name;
    private final int line;
    private final int column;

    public Symbol(String name, int line, int column) {
        this.name = name;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("%s at [line %d, column %d]", name, line, column);
    }
}
