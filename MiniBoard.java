package org.cis1200.ultimatetictactoe;

/**
 * Represents a 3x3 mini-board in Ultimate Tic-Tac-Toe.
 */
public class MiniBoard {
    private final Cell[][] grid;
    private GameState gameState;

    public MiniBoard() {
        grid = new Cell[3][3];
        reset();
    }

    public char getWinner() {
        switch (gameState) {
            case X_WON:
                return 'X';
            case O_WON:
                return 'O';
            default:
                return Cell.EMPTY;
        }
    }

    public Cell getCell(int row, int col) {
        Cell cell = new Cell();
        cell.setSymbol(grid[row][col].getSymbol());
        return cell;
    }

    public boolean isFull() {
        if (gameState == GameState.DRAW) {
            return true;
        }

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void updateGameState() {
        if (gameState != GameState.IN_PROGRESS) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            /* Rows */
            if (checkLineWin(grid[i][0], grid[i][1], grid[i][2])) {
                gameState = grid[i][0].getSymbol() == 'X' ? GameState.X_WON : GameState.O_WON;
                return;
            }

            /* Columns */
            if (checkLineWin(grid[0][i], grid[1][i], grid[2][i])) {
                gameState = grid[0][i].getSymbol() == 'X' ? GameState.X_WON : GameState.O_WON;
                return;
            }
        }

        /* Diagonals */
        if (checkLineWin(grid[0][0], grid[1][1], grid[2][2])
                || checkLineWin(grid[0][2], grid[1][1], grid[2][0])) {
            gameState = grid[1][1].getSymbol() == 'X' ? GameState.X_WON : GameState.O_WON;
            return;
        }

        /* Draw */
        if (isFull()) {
            gameState = GameState.DRAW;
        }
    }

    private boolean checkLineWin(Cell cell1, Cell cell2, Cell cell3) {
        return !cell1.isEmpty() && cell1.getSymbol() == cell2.getSymbol()
                && cell1.getSymbol() == cell3.getSymbol();
    }

    public boolean placeMove(int row, int col, char symbol) {
        if (gameState == GameState.DRAW || !grid[row][col].isEmpty()) {
            return false;
        }

        grid[row][col].setSymbol(symbol);
        updateGameState();
        return true;
    }

    public void undoMove(int subRow, int subCol) {
        grid[subRow][subCol].reset();
        gameState = GameState.IN_PROGRESS;
        updateGameState();
    }

    public void reset() {
        gameState = GameState.IN_PROGRESS;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}