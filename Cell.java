package org.cis1200.ultimatetictactoe;

/**
 * Represents a single cell in a Tic-Tac-Toe grid.
 * A cell can be empty or contain an X or O symbol.
 */
public class Cell {
    public static final char EMPTY = '\0';
    private char symbol;

    public Cell() {
        reset();
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isEmpty() {
        return symbol == EMPTY;
    }

    public void reset() {
        this.symbol = EMPTY;
    }
}