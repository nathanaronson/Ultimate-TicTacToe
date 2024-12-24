package org.cis1200.ultimatetictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * Represents the graphical game board for Ultimate Tic-Tac-Toe.
 * Manages the game UI, drawing, and user interactions.
 */
public class GameBoard extends JPanel {
    private final UltimateTicTacToe uttt;
    private final JLabel status;
    private int boardWidth;
    private int boardHeight;
    private int miniBoardWidth;
    private int miniBoardHeight;
    private int cellWidth;
    private int cellHeight;

    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        uttt = new UltimateTicTacToe();
        status = statusInit;
        boardWidth = 675;
        boardHeight = 675;
        miniBoardWidth = 225;
        miniBoardHeight = 225;
        cellWidth = 75;
        cellHeight = 75;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (uttt.processMove(
                        e.getX(), e.getY(), boardHeight, boardWidth, miniBoardHeight,
                        miniBoardWidth,
                        cellHeight, cellWidth
                )) {
                    updateStatus();
                    repaint();
                }
            }
        });
    }

    public void reset() {
        uttt.reset();
        status.setText("Player 1's Turn");
        repaint();
        requestFocusInWindow();
    }

    public boolean undo() {
        boolean undoSuccessful = uttt.undoLastMove();
        if (undoSuccessful) {
            updateStatus();
            repaint();
            return true;
        }
        return false;
    }

    public boolean redo() {
        boolean redoSuccessful = uttt.redoLastMove();
        if (redoSuccessful) {
            updateStatus();
            repaint();
            return true;
        }
        return false;
    }

    public boolean exportGame(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            for (String s : uttt.getMovesAsString()) {
                bw.write(s + "\n");
            }
            bw.flush();
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean importGame(String path) {
        reset();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null && !uttt.isGameOver()) {
                String[] move = line.split(" ");

                if (move.length != 5) {
                    throw new NumberFormatException();
                }

                int boardRow = Integer.parseInt(move[0]);
                int boardCol = Integer.parseInt(move[1]);
                int subRow = Integer.parseInt(move[2]);
                int subCol = Integer.parseInt(move[3]);
                char player = move[4].charAt(0);

                if (!uttt.isValidIndex(boardRow) || !uttt.isValidIndex(boardCol)
                        || !uttt.isValidIndex(subRow) ||
                        !uttt.isValidIndex(subCol) || player != uttt.getCurrentPlayer()) {
                    throw new NumberFormatException();
                }

                if (uttt.getNextRow() == -1
                        || (boardRow == uttt.getNextRow() && boardCol == uttt.getNextCol())) {
                    uttt.processMove(boardRow, boardCol, subRow, subCol, true);
                }
            }

            br.close();
            updateStatus();
            repaint();
            return true;
        } catch (IOException | NumberFormatException e) {
            reset();
            return false;
        }
    }

    private void updateStatus() {
        if (uttt.isGameOver()) {
            status.setText(
                    uttt.getWinner() == 'X' ? "Player 1 Wins!"
                            : uttt.getWinner() == 'O' ? "Player 2 Wins!" : "Draw!"
            );
        } else {
            status.setText(uttt.getCurrentPlayer() == 'X' ? "Player 1's Turn" : "Player 2's Turn");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        updateDimensions();
        drawBoardGridLines(g2d);
        drawMiniBoardGridLines(g2d);
        drawSymbols(g2d);
        highlightBoardStates(g2d);
    }

    private void updateDimensions() {
        boardHeight = getHeight();
        boardWidth = getWidth();
        miniBoardHeight = boardHeight / 3;
        miniBoardWidth = boardWidth / 3;
        cellHeight = miniBoardHeight / 3;
        cellWidth = miniBoardWidth / 3;
    }

    private void drawBoardGridLines(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        for (int i = 1; i < 3; i++) {
            g2d.drawLine(i * miniBoardWidth, 0, i * miniBoardWidth, boardHeight);
            g2d.drawLine(0, i * miniBoardHeight, boardWidth, i * miniBoardHeight);
        }
    }

    private void drawMiniBoardGridLines(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        for (int boardRow = 0; boardRow < 3; boardRow++) {
            for (int boardCol = 0; boardCol < 3; boardCol++) {
                int boardX = boardCol * miniBoardWidth;
                int boardY = boardRow * miniBoardHeight;

                for (int i = 1; i < 3; i++) {
                    g2d.drawLine(
                            boardX + i * cellWidth, boardY, boardX + i * cellWidth,
                            boardY + miniBoardHeight
                    );
                    g2d.drawLine(
                            boardX, boardY + i * cellHeight, boardX + miniBoardWidth,
                            boardY + i * cellHeight
                    );
                }
            }
        }
    }

    private void drawSymbols(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        for (int boardRow = 0; boardRow < 3; boardRow++) {
            for (int boardCol = 0; boardCol < 3; boardCol++) {
                MiniBoard miniBoard = uttt.getBoard(boardRow, boardCol);

                for (int subRow = 0; subRow < 3; subRow++) {
                    for (int subCol = 0; subCol < 3; subCol++) {
                        Cell cell = miniBoard.getCell(subRow, subCol);
                        drawCellSymbol(g2d, cell, boardRow, boardCol, subRow, subCol);
                    }
                }
            }
        }
    }

    private void drawCellSymbol(
            Graphics2D g2d, Cell cell, int boardRow, int boardCol, int subRow, int subCol
    ) {
        if (!cell.isEmpty()) {
            int x = boardCol * miniBoardWidth + subCol * cellWidth + cellWidth / 4;
            int y = boardRow * miniBoardHeight + subRow * cellHeight + cellHeight / 4;
            int xSize = cellWidth / 2;
            int ySize = cellHeight / 2;

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            if (cell.getSymbol() == 'O') {
                g2d.drawOval(x, y, xSize, ySize);
            } else if (cell.getSymbol() == 'X') {
                g2d.drawLine(x, y, x + xSize, y + ySize);
                g2d.drawLine(x + xSize, y, x, y + ySize);
            }
        }
    }

    private void highlightBoardStates(Graphics2D g2d) {
        for (int boardRow = 0; boardRow < 3; boardRow++) {
            for (int boardCol = 0; boardCol < 3; boardCol++) {
                MiniBoard miniBoard = uttt.getBoard(boardRow, boardCol);
                highlightMiniBoard(g2d, miniBoard, boardRow, boardCol);
            }
        }

        highlightNextMoveBoard(g2d);
    }

    private void highlightMiniBoard(
            Graphics2D g2d, MiniBoard miniBoard, int boardRow, int boardCol
    ) {
        Color highlightColor;

        if (miniBoard.getWinner() != Cell.EMPTY) {
            highlightColor = miniBoard.getWinner() == 'X' ? new Color(200, 255, 200, 100)
                    : new Color(255, 200, 200, 100);
        } else if (miniBoard.isFull()) {
            highlightColor = new Color(255, 255, 100, 100);
        } else {
            return;
        }

        g2d.setColor(highlightColor);
        g2d.fillRect(
                boardCol * miniBoardWidth, boardRow * miniBoardHeight, miniBoardWidth,
                miniBoardHeight
        );
    }

    private void highlightNextMoveBoard(Graphics2D g2d) {
        int nextRow = uttt.getNextRow();
        int nextCol = uttt.getNextCol();

        if (nextRow != -1 && nextCol != -1) {
            g2d.setColor(new Color(200, 200, 255, 100));
            g2d.fillRect(
                    nextCol * miniBoardWidth, nextRow * miniBoardHeight, miniBoardWidth,
                    miniBoardHeight
            );
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardWidth, boardHeight);
    }
}