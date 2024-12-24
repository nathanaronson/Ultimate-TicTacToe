package org.cis1200.ultimatetictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Manages the game logic for Ultimate Tic-Tac-Toe.
 */
public class UltimateTicTacToe {

    private record Move(int boardRow, int boardCol, int subRow, int subCol, char player) {
        @Override
        public String toString() {
            return boardRow + " " + boardCol + " " + subRow + " " + subCol + " " + player;
        }
    }

    private final MiniBoard[][] boards;
    private int nextCol;
    private int nextRow;
    private char currentPlayer;
    private GameState gameState;
    private final Stack<Move> moves;
    private final Stack<Move> undoneMoves;

    public UltimateTicTacToe() {
        boards = new MiniBoard[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j] = new MiniBoard();
            }
        }
        moves = new Stack<>();
        undoneMoves = new Stack<>();
        reset();
    }

    public boolean processMove(
            int x, int y, int boardHeight, int boardWidth, int miniBoardHeight, int miniBoardWidth,
            int cellHeight, int cellWidth
    ) {
        if (!isGameOver() && x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            int boardRow = y / miniBoardHeight;
            int boardCol = x / miniBoardWidth;
            int subRow = (y % miniBoardHeight) / cellHeight;
            int subCol = (x % miniBoardWidth) / cellWidth;

            if (nextRow == -1 || (boardRow == nextRow && boardCol == nextCol)) {
                return processMove(boardRow, boardCol, subRow, subCol, true);
            }
        }

        return false;
    }

    public boolean processMove(int boardRow, int boardCol, int subRow, int subCol, boolean manual) {
        if (manual && !undoneMoves.isEmpty()) {
            undoneMoves.clear();
        }

        if (!isValidIndex(boardRow) || !isValidIndex(boardCol) || !isValidIndex(subRow)
                || !isValidIndex(subCol)) {
            return false;
        }

        MiniBoard board = boards[boardRow][boardCol];

        if (!board.getCell(subRow, subCol).isEmpty()) {
            return false;
        }

        boolean success = board.placeMove(subRow, subCol, currentPlayer);

        if (success) {
            moves.add(new Move(boardRow, boardCol, subRow, subCol, currentPlayer));
            updateGameState();
            if (gameState != GameState.IN_PROGRESS) {
                nextRow = -1;
                nextCol = -1;
            } else {
                updateNextMove(subRow, subCol);
            }
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        }

        return false;
    }

    private void updateGameState() {
        /* Rows */
        for (int i = 0; i < 3; i++) {
            if (checkBoardLineWin(boards[i][0], boards[i][1], boards[i][2])) {
                return;
            }
        }

        /* Columns */
        for (int j = 0; j < 3; j++) {
            if (checkBoardLineWin(boards[0][j], boards[1][j], boards[2][j])) {
                return;
            }
        }

        /* Diagonals */
        if (checkBoardLineWin(
                boards[0][0], boards[1][1], boards[2][2]
        ) || checkBoardLineWin(boards[0][2], boards[1][1],
                boards[2][0]
        )) {
            return;
        }

        /* Draw */
        if (isFull()) {
            gameState = GameState.DRAW;
        }
    }

    private boolean checkBoardLineWin(MiniBoard board1, MiniBoard board2, MiniBoard board3) {
        char firstWinner = board1.getWinner();
        if (firstWinner != Cell.EMPTY && firstWinner == board2.getWinner()
                && firstWinner == board3.getWinner()) {
            gameState = firstWinner == 'X' ? GameState.X_WON : GameState.O_WON;
            return true;
        }
        return false;
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < 3;
    }

    public boolean isGameOver() {
        return gameState != GameState.IN_PROGRESS;
    }

    private boolean isFull() {
        for (MiniBoard[] boardRow : boards) {
            for (MiniBoard board : boardRow) {
                if (!board.isFull()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateNextMove(int subRow, int subCol) {
        if (boards[subRow][subCol].isFull()) {
            nextCol = -1;
            nextRow = -1;
        } else {
            nextCol = subCol;
            nextRow = subRow;
        }
    }

    public boolean undoLastMove() {
        if (moves.isEmpty()) {
            return false;
        }

        Move move = moves.pop();
        undoneMoves.push(move);
        boards[move.boardRow()][move.boardCol()].undoMove(move.subRow(), move.subCol());

        if (moves.isEmpty()) {
            nextRow = -1;
            nextCol = -1;
        } else {
            Move prev = moves.peek();
            updateNextMove(prev.subRow(), prev.subCol());
        }

        currentPlayer = move.player();
        gameState = GameState.IN_PROGRESS;
        return true;
    }

    public boolean redoLastMove() {
        if (undoneMoves.isEmpty()) {
            return false;
        }

        Move move = undoneMoves.pop();
        return processMove(move.boardRow(), move.boardCol(), move.subRow(), move.subCol(), false);
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boards[i][j].reset();
            }
        }

        nextRow = -1;
        nextCol = -1;
        gameState = GameState.IN_PROGRESS;
        currentPlayer = 'X';
        moves.clear();
        undoneMoves.clear();
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char getWinner() {
        switch (gameState) {
            case X_WON:
                return 'X';
            case O_WON:
                return 'O';
            default:
                throw new IllegalStateException("No Winner");
        }
    }

    public MiniBoard getBoard(int row, int col) {
        MiniBoard miniBoard = new MiniBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                miniBoard.placeMove(i, j, boards[row][col].getCell(i, j).getSymbol());
            }
        }

        return miniBoard;
    }

    public int getNextRow() {
        return nextRow;
    }

    public int getNextCol() {
        return nextCol;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<String> getMovesAsString() {
        ArrayList<String> text = new ArrayList<>();
        Stack<Move> tempStack = new Stack<>();

        while (!moves.isEmpty()) {
            Move move = moves.pop();
            text.add(move.toString());
            tempStack.push(move);
        }

        while (!tempStack.isEmpty()) {
            moves.push(tempStack.pop());
        }

        Collections.reverse(text);
        return text;
    }
}