package org.cis1200.ultimatetictactoe;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Initializes and runs the Ultimate Tic-Tac-Toe game application.
 * Follows the Model-View-Controller (MVC) design pattern.
 */
public class RunUltimateTicTacToe implements Runnable {

    @Override
    public void run() {
        final JFrame frame = new JFrame("Ultimate Tic-Tac-Toe");

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);

        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        final JButton redo = new JButton("Redo");
        redo.addActionListener(e -> board.redo());
        control_panel.add(redo);

        final JButton import_game = new JButton("Import");
        import_game.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a .uttt file to import the game from");

            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getAbsolutePath().endsWith(".uttt")
                        || !board.importGame(file.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Failed to import from " + file.getName() + ".",
                            "Unable to Import",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        control_panel.add(import_game);

        final JButton export_game = new JButton("Export");
        export_game.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a .uttt file to export the game to");

            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getAbsolutePath().endsWith(".uttt")
                        || !board.exportGame(file.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Failed to export to " + file.getName() + ".",
                            "Unable to Export",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        control_panel.add(export_game);

        final JButton rules = new JButton("Rules");
        rules.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    null,
                    "<html><body style='width: 400px; padding: 10px;'>" +
                            "<h2>Ultimate Tic-Tac-Toe</h2>" +
                            "<p><b>Written By:</b> Nathan Aronson in Fall 2024 for HW09 CIS 1200" +
                            "</p>"
                            +
                            "<h3>Gameplay Rules:</h3>" +
                            "<ul>" +
                            "<li>Just like regular " +
                            "Tic-Tac-Toe, two players, 'X' and 'O' taken turns.</li>"
                            +
                            "<li>However, rather " +
                            "than there being nine spots, there are nine mini-boards, "
                            +
                            "each of which " +
                            "is its own Tic-Tac-Toe board.</li>" +
                            "<li>The game starts with X clicking wherever they want in any of the "
                            +
                            "81 empty spots.</li>" +
                            "<li>Next, O makes their move, however " +
                            "they are confined to click in the mini-board "
                            +
                            "whose " +
                            "coordinates correspond to the sub-coordinates" +
                            " of the most recent cell that X played."
                            +
                            "<li>For example, if X clicks in the" +
                            " top-left spot of a given mini-board, O's next move "
                            +
                            "must" +
                            " be in the top-left mini-board.</li>" +
                            "<li>If a move is played such that" +
                            " it wins a mini-board by the rules of regular "
                            +
                            "Tic-Tac-Toe," +
                            " then that entire mini-board is " +
                            "marked as won by the player in the larger board."
                            +
                            "<li>Even though a mini-board may be " +
                            "won, players can continue to click in that board "
                            +
                            "until " +
                            "all of its cells are filled, however " +
                            "the winner of it remains whoever achieved "
                            +
                            "three-in-a-" +
                            "row first.</li>" +
                            "<li>If a player sends their opponent " +
                            "into a full mini-board, then that opponent may "
                            +
                            "click " +
                            "in any open mini-board.</li>" +
                            "</ul>" +
                            "<h3>Winning:</h3>" +
                            "<ul>" +
                            "<li>To win a mini-board, a player " +
                            "must claim three spots in a row (horizontal, "
                            +
                            "vertical, " +
                            "or diagonal).</li>" +
                            "<li>To win the main board, a player must " +
                            "win three mini-boards in a row.</li>"
                            +
                            "<li>The game is a draw if all mini-boards" +
                            " are full with no overall winner.</li>"
                            +
                            "<li>The game may seem complicated at " +
                            "first, however you will get the hang of it "
                            +
                            "quickly.</li>" +
                            "<li>Good luck and have fun!</li>" +
                            "</ul>" +
                            "</body></html>",
                    "Ultimate Tic-Tac-Toe Rules",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        control_panel.add(rules);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        board.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunUltimateTicTacToe());
    }
}