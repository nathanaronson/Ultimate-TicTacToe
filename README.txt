=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

  1. 2D Arrays – I use 2D Arrays in this project to organize the main board of the game,
  namely a 3x3 grid of MiniBoards (boards). Additionally, within each MiniBoard, I use another
  2D Array to organize the 3x3 grid of Cells (cells). The reason I use 2D Arrays for these boards
  rather than other data structures that the game is naturally structured as a matrix.
  This makes the game logic– making moves, checking for wins, displaying– very easy to incorporate.
  No feedback was given for this.

  2. Collections – I use a Stack to store the game history, namely every Move that was made.
  The reason I use a Stack and not another datatype is due to the invariants of first-in, last-out,
  which follows the exact properties I want for accessing Move history. I use this to implement the Undo
  and Redo features, which can be easily implemented using the .pop() and .push() commands (easily access
  most recent move, hard to access first move). The Move history Stack (moves) gets added to
  after every move, while the Redo Stack (undoneMoves) only has gets added to after Undo is called.
  Both start out empty, and undoneMoves is also cleared after all moves are redone, or after a player
  manually makes a move, altering the Move history. As for feedback, I received "A linked list isn't
  a novel data structure. It would satisfy collections tho." I originally proposed the LinkedList
  data structure, however, while programming the game, recognized that a Stack would be better.

  3. JUnit Testable Components – I use extensive JUnit Testable Components to ensure
  that my game logic is handled properly. Ultimate Tic-Tac-Toe involves a lot of invariants,
  namely which clicks legal, when a player has one a board, where the next row should be,
  undoing and redoing properly, the list goes on. For that reason, I spent extensive time testing
  all game state/model-based features through 19 tests. I also used these tests to ensure
  that my game logic is handled properly. No feedback was given for this.

  4. File I/O – I implemented File I/O in the form of importing and exporting games via
  buttons called "Import" and "Export" respectively. Games are imported and exported
  through ".uttt" files, and are stored in the form of the Move history, with each line
  being the next move, reading top-to-bottom. Games can be exported at any time in the game,
  and when a game is imported, the old game gets lost. I use a BufferedReader and BufferedWriter
  to ensure that reading and writing the files is handled properly, and it throws the proper errors,
  such as the Move invariants not being held properly, or the file is not formatted properly.
  The benefit about storing the game state in the sequence of Moves is that the BufferedReader effectively
  "plays" the game, with each move being the next line in the file. This ensures that the game rules
  are applied to the file. In the event that reading or writing fails, a pop-up error message is displayed.
  This feature is appropriate if you would like to save games for later, or would like to resume
  a previous game. Here is a sample file:

  0 0 2 2 X
  2 2 0 2 O
  0 2 2 0 X

  Move 1: Player 1 makes a move in the top-left mini-board in the bottom-right cell.
  Move 2: Player 2 makes a move in the bottom-right mini-board in the top-right cell.
  Move 3: Player 1 makes a move in the top-right mini-board in the bottom-left cell.

  As for feedback, originally, I proposed that the game state would be managed using Complex Logic,
  however that was rejected. Instead, they proposed that I implement this instead, which
  is what I did.

===============================
=: File Structure Screenshot :=
===============================

- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named
  "file_structure.png".

  Screenshot included in Codio submission.

  hw09_local_temp
    .idea
    .run
    files
    src
        main
            java
                org.cis1200
                    mushroom
                    tictactoe
                    ultimatetictactoe
                        Cell.java
                        GameBoard.java
                        GameState.java
                        MiniBoard.java
                        RunUltimateTicTacToe.java
                        UltimateTicTacToe.java
                    Game
        test
            java
                org.cis1200
                    mushroom
                    tictactoe
                    ultimatetictactoe
                        UltimateTicTacToeTest.java
                    CompilationTest.java
    target
    file_structure.png
    Makefile
    pom.xml
    README.md
    README.txt

=========================
=: Your Implementation :=
=========================

Cell:

    Description: Represents a single square in a Tic-Tac-Toe grid. A cell can be either
                 empty '\0', or contain an X or O symbol. It is utilized in the form of
                 a 2D array, cells within the MiniBoard class. The game itself is composed of
                 81 Cells.

    Instance Variables: char symbol – the symbol of the cell

    Constants: Cell.EMPTY which is the empty character of a Cell, namely '\0'.

    Functions: getSymbol() which gets the symbol value of a Cell (used when updating gameState or displaying),
    setSymbol() which sets the symbol value of a Cell (used when making a move), isEmpty() which checks if the Cell
    is empty (used when making a move to make sure it is legal), and reset(), which sets the symbol to be empty.

GameState:

    Description: Represents the game state of both each miniature Tic-Tac-Toe board and
    the Ultimate Tic-Tac-Toe board itself. Either the game is IN_PROGRESS, X_WON, O_WON,
    or DRAW. Pretty intuitive, used in game logic.

MiniBoard:

    Description: Represents an individual Tic-Tac-Toe game. Consists of a 3x3 board of Cells
    in the form of a 2D array, grid. Has all functions necessary for the individual game built-in,
    including a managing the gameState, checking for win-conditions, placing moves, reseting, and so on.
    Nine MiniBoards (nine individual Tic-Tac-Toe games) are stored within the UltimateTicTacToe instance variable,
    boards, in the form of a 2D array of MiniBoards.

    Instance Variables: Cell[][] grid – the board
                        GameState gameState – the current state of the game

    Functions: getWinner() gets the winner based on gameState, getCell() gets the cell of a given
    row and column, isFull() checks if all Cells' symbols in the board are not empty, updateGameState()
    is a private function that is called whenever a move is made to update gameState. checkLineWin() is passed
    three Cells and checks to see if the symbols are all the same and are not empty, placeMove() places a move on
    the board by updating the value of the symbol of the Cell at given coordinates (while also checking its legality,
    as in the game is still going and the Cell is not empty), undoMoves() resets the Cell
    at a given coordinate, reset() resets every Cell in the entire board, and getGameState() returns the GameState.

UltimateTicTacToe:

    Description: The highest-level class in the game, which incorporates a GameState for the game's state,
    a 2D array of MiniBoards in the form of a 3x3 grid, each of which is comprised of a 3x3 grid
    of Cells. This is the actual game itself that is displayed on the GameBoard, and stores all
    game logic from the board, to the move history, to who the player is, and to which board should the next move be in.

    Instance Variables: MiniBoard[][] boards - the main board comprised of 9 MiniBoards.
                        int nextCol – The column of the board where the next move should be. -1 if it can be anywhere.
                        int nextRow – The row of the board where the next move should be. -1 if it can be anywhere.
                        char currentPlayer – The current player of a given turn– passed down to the setSymbol() function
                                             of a Cell when making a move. Alternates between 'X' and 'O'.
                        GameState gameState – the current state of the game
                        Stack<Move> moves – the history of every move made, added to the back and accessed from the back
                        Stack<Move> undoneMoves – the history of every mode that was undone, cleared whenever the user
                                                  makes a manual move

    Records: Move
                1. int boardRow – the row coordinate of the MiniBoard where the move was played
                2. int boardCol – the column coordinate of the MiniBoard where the move was played
                3. int subRow – the row coordinate of the Cell within the MiniBoard where the move was played
                4. int subCol – the column coordinate of the Cell within the MiniBoard where the move was played
                5. char player – the symbol of the player who made the move

                Patterns:
                    1. Within moves, the player component should be alternating every-other
                    2. Within moves, the previous (subRow, subCol) should be equal to the next (boardRow, boardCol)
                       unless the board whose coordinates are (subRow, subCol) is full.

    Functions: toString() method of Move is used for formatting the Move for the sake of importing/exporting games, as
               games can be stored in the form of move history. processMove() is an overloaded method which can either
               be passed click coordinates, or a Move that is deconstructed. It runs through logic to make sure that the
               click/Move are legal (within the right bounds, the Cell is empty, the game is not over, etc.) and then
               processes the move by calling the placeMove() function of the MiniBoard at the respective coordinates. It
               then updates by gameState and sets the nextRow and nextCol to their appropriate values, and flips the
               currentPlayer. updateGameState() is a private method that is called whenever a Move is processed. It
               checks for a win or a draw. checkBoardLineWin() is a helper function that is called within the
               updateGameState() method. It is passed three boards and checks that the three winners of the boards are
               all the same and are not empty. isValidIndex() is a helper function that checks if a number is between
               0 and 3– useful when making sure that a coordinate is within the bounds of a grid. isGameOver() checks
               if the game is over, isFull() checks if all MiniBoards are full, updateNextMove() is a helper function
               that updates the nextRow and nextCol based on if the board that the previous move sends the next player
               to is full. undoLastMove() undoes the last move by popping it from the Stack, adding it to undoneMoves,
               and updating the game state accordingly. redoLastMove() essentially calls the placeMove() method based on
               the Move at the end of the undoneMoves stack. reset() resets the game back to its original state.
               getMovesAsString() converts the moves into their String equivalent for easy parsing into the .uttt file
               during a game export. The rest of the functions are getters, with the getBoards() returning a deep copy,
               and so on, for proper encapsulation.

GameBoard:

    Description: GameBoard represents the graphical game board for Ultimate Tic-Tac-Toe. It manages the game UI,
    drawing, and user interactions. Comprised of multiple functions that display the various components of the game.

    Instance Variables: UltimateTicTacToe uttt – the game state itself
                        JLabel status – the status display of the game at the bottom of the screen
                        int boardWidth – the pixel width of the board (the width of the JPanel)
                        int boardHeight – the pixel height of the board (the height of the JPanel)
                        int miniBoardWidth – the pixel width of a MiniBoard (1/3 boardWidth)
                        int miniBoardHeight – the pixel height of a MiniBoard (1/3 boardHeight)
                        int cellWidth – the pixel width of a Cell (1/9 boardWidth or 1/3 miniBoardWidth)
                        int cellHeight – the pixel height of a Cell (1/9 boardHeight or 1/3 miniBoardHeight)

    Functions: There is a mouseListener added to JPanel itself such that when a click is registered, uttt.processMove()
    is called with the given coordinates. If it is successful, it updates the status label and repaints the board.
    reset() resets the game, resets the status, and repaints the board. undo() attempts to undo the previous move by
    calling uttt.undoLastMove(). If it is successful, it updates the status label and repaints the board. redo() does
    the exact same thing but for uttt.redoLastMove(). exportGame() attempts to export the game to a .uttt file
    by using a BufferedWriter and writing the uttt.getMovesAsString() List<String> with each move being its own line.
    importGame() attempts to import the game from a .uttt file by using a BufferedReader and reading each line for a
    Move, ensuring its proper invariants, and then calling the uttt.processMove() on that given Move. If successful,
    it repaints the board and updates the status. updateStatus() updates the JLabel status depending on whose move it
    is, or if the game is over. paintComponent() is parent function that updates the board dimensions in case the
    player resizes the application, draws board and MiniBoard gridlines, draws the symbols, and highlights any boards.
    updateDimensions() updates the dimensions of the last six instance variables in the event that the user resizes
    their application. drawBoardGridLines() draws two black vertical and two horizontal lines to make the grid.
    drawMiniBoardGridLines() does the exact same thing, but at a smaller scale for each MiniBoard. drawSymbols() draws
    all necessary symbols that were placed in any Cell. drawCellSymbol() is a helper function that is called within
    drawSymbols() to draw either an 'X' or an 'O' depending on the symbol of the cell. highlightBoardStates() is a
    parent function that calls the various highlighting functions. highlightMiniBoard(), which highlights a board green
    if won by X, red if won by O, and yellow if a draw. highlightNextBoardMove() highlights the next MiniBoard that the
    nextRow and nextCol point to (the MiniBoard where the next move should be). Nothing is highlighting if the next move
    is free. getPreferredSize() returns the boardWidth and boardHeight as a Dimension object.

RunUltimateTicTacToe:

    Description: RunUltimateTicTacToe initializes and runs the Ultimate Tic-Tac-Toe game application. It follows the
    Model-View-Controller (MVC) design pattern.

    Overview:

        JFrame frame is the application frame itself
        JPanel status_panel is the panel at the bottom of the frame that displays the game status
        JLabel status is the status label that is incorporated into the status_panel
        GameBoard board is the Canvas for the game
        JPanel control_panel is the panel at the top of the screen that incorporates the various buttons
        JButton reset is the button that resets the game by calling the reset() function
        JButton undo is the button that undoes the most recent move of the game by calling the undo() function
        JButton redo is the button that redoes the previously undone move of the game by calling the redo() function
        JButton import_game is the button that attempts to import a game by using a JFileChooser and ensuring the proper
        file type.
        JButton export_game is the button that attempts to export the game by using a JFileChooser and ensuring the
        proper file type.
        JButton rules is the button that pop-ups the rules of the game via a JOptionPane.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  Nope. There were no difficulties aside from learning about some niche Java syntax including lambdas with the double
  colons. It was also a bit tedious to create the rules because I had to research the various options Swing has.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I believe my design is very effective, with proper separation of functionality. All logic is performed at the
  lower-level possible to maximize efficiency, and barely any logic is performed in the GameBoard class. In other words,
  the GUI and the game logic are separated properly. I encapsulated private state properly with correct method headers,
  and ensured that the getters() returned structurally equal boards rather than reference equal. I spent a lot of time
  bug fixing and now am having difficulty finding any. In fact, I spent quite some time refactoring my code after I
  completed it. The only major thing I would change would be method-level comments for descriptions. If I want to
  incorporate this project into my portfolio, I would want for it to be well-commented, formatted, and coded.


========================
=: External Resources :=
========================

No external resources were used aside from the starter code for Tic-Tac-Toe, along with
lecture slides and other CIS1200-provided resources. Much of my code was strongly
influenced from previous homework assignments (arrays from 6, game state from 7, I/O from 8),
however none of it was copy-pasted. All were altered during translation.

If you consider getting the game rules as an external resource, then I based them on
the Wikipedia article, which can be found here: https://en.wikipedia.org/wiki/Ultimate_tic-tac-toe.