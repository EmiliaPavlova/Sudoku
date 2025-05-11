package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.SudokuBoard;
import model.SudokuGenerator;
import model.SudokuSolver;
import model.SudokuCell;

/** The class mediated to the GUI using components from JavaFX */
public class SudokuUI extends Application {

  /** The dimension size of a sudoku board - 9 rows * 9 columns */
  private static final int SIZE = 9;

  /** The maximum number of available hints. Each one reveals the value of a cell */
  private static int MAX_HINTS = 5;

  /** The number of cells that will be visible and fixed on the board */
  private static int CLUES = 30;

  /** An array with all cells of the board */
  private TextField[][] cells = new TextField[SIZE][SIZE];

  /** The board object holding the current values of cells */
  private SudokuBoard board;

  /** A flag to track if the hint mode is active */
  private boolean hintMode = false;

  /** The current number of hints left */
  private int hintsLeft = MAX_HINTS;

  /** The button to activate the hint mode */
  private Button hintButton;

  /** A flag to track if the solution has been checked */
  private boolean hasChecked = false;

  /** Timer to track the time for solving the sudoku */
  private Timeline timer;

  /** A variable to store the time elapsed in seconds */
  private int timeElapsed = 0;

  /** The label to display the time */
  private Label timerLabel;

  /**
   * Initializing the game
   *
   * @param primaryStage The primary stage of the JavaFX application
   */
  @Override
  public void start(Stage primaryStage) {
    board = SudokuGenerator.generate(CLUES);

    BorderPane window = new BorderPane();
    GridPane grid = new GridPane();
    grid.setHgap(2);
    grid.setVgap(2);
    grid.setAlignment(Pos.CENTER);

    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        TextField cell = new TextField();
        cell.setPrefHeight(50);
        cell.setPrefWidth(50);
        cell.getStyleClass().add("text-field");

        int currentRow = row;
        int currentCol = col;

        cell.setOnMouseClicked(e -> {
          if (hintMode && cells[currentRow][currentCol].isEditable() && hintsLeft > 0) {
            showHint(currentRow, currentCol);
          }
        });

        // allow only single digit from 1 to 9 in a cell
        cell.textProperty().addListener((obs, oldText, newText) -> {
          if (!newText.matches("[1-9]?")) {
            cell.setText(oldText);
          }
          if (hasChecked) {
            resetCellStyles();
            hasChecked = false;
          }
        });

        SudokuCell sudokuCell = board.getCell(row, col);
        if (sudokuCell.getValue() != 0) {
          cell.setText(String.valueOf(sudokuCell.getValue()));
          cell.setEditable(false);
          cell.getStyleClass().add("fixed-cell");
        }

        if (row % 3 == 0 && col % 3 == 0) {
          cell.getStyleClass().add("border-top-left");
        } else if (row % 3 == 0 && col % 3 == 2) {
          cell.getStyleClass().add("border-top-right");
        } else if (row % 3 == 2 && col % 3 == 0) {
          cell.getStyleClass().add("border-bottom-left");
        } else if (row % 3 == 2 && col % 3 == 2) {
          cell.getStyleClass().add("border-bottom-right");
        } else {
          if (row % 3 == 0) cell.getStyleClass().add("border-top");
          if (col % 3 == 0) cell.getStyleClass().add("border-left");
          if (row % 3 == 2) cell.getStyleClass().add("border-bottom");
          if (col % 3 == 2) cell.getStyleClass().add("border-right");
        }

        cells[row][col] = cell;
        grid.add(cell, col, row);
      }
    }

    hintButton = new Button("Hint (" + hintsLeft + ")");
    hintButton.getStyleClass().add("sudoku-button");
    hintButton.setOnAction(e -> {
      if (hintsLeft > 0) {
        hintMode = true;
      }
    });

    Button newGameButton = new Button("New Game");
    newGameButton.getStyleClass().add("button");
    newGameButton.setOnAction(e -> {
      newGame();
    });

    Button checkButton = new Button("Check");
    checkButton.getStyleClass().add("button");
    checkButton.setOnAction(e -> {
      checkSolution();
    });

    BorderPane bottomPane = new BorderPane();
    bottomPane.setLeft(newGameButton);
    bottomPane.setCenter(checkButton);
    bottomPane.setRight(hintButton);
    bottomPane.setPadding(new Insets(16));

    timerLabel = new Label("00:00");
    BorderPane topPane = new BorderPane();
    topPane.setCenter(timerLabel);
    topPane.setPadding(new Insets(10));
    timerLabel.getStyleClass().add("timer-label");

    window.setTop(topPane);
    window.setCenter(grid);
    window.setBottom(bottomPane);

    Scene scene = new Scene(window, 500, 550);
    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    primaryStage.setTitle("Sudoku");
    primaryStage.setScene(scene);
    primaryStage.show();

    startTimer();
  }

  /**
   * Shows the correct value on a cell click
   *
   * @param row The row index of the cell
   * @param col The column index of the cell
   */
  private void showHint(int row, int col) {
    SudokuSolver solver = new SudokuSolver();
    SudokuBoard solvedBoard = board.clone();
    solver.solve(solvedBoard);

    int correctValue = solvedBoard.getCell(row, col).getValue();
    cells[row][col].setText(String.valueOf(correctValue));
    cells[row][col].setEditable(false);
    cells[row][col].getStyleClass().add("text-field");

    hintsLeft--;
    hintMode = false;

    updateHintButton();
  }

  /**
   * Starts a new game, resets the board state and flags
   */
  private void newGame() {
    board = SudokuGenerator.generate(CLUES);
    hintsLeft = MAX_HINTS;
    hintMode = false;
    hasChecked = false;
    hintButton.setDisable(false);
    updateBoard();
    updateHintButton();
    resetTimer();
    startTimer();
  }

  /**
   * Updates the board with initial state when New game is called
   */
  private void updateBoard() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        SudokuCell sudokuCell = board.getCell(row, col);
        TextField cell = cells[row][col];
        if (sudokuCell.getValue() != 0) {
          cell.setText(String.valueOf(sudokuCell.getValue()));
          cell.setEditable(false);
          cell.getStyleClass().removeAll("error-cell", "hint-cell");
          cell.getStyleClass().add("fixed-cell");
        } else {
          cell.setText("");
          cell.setEditable(true);
          cell.getStyleClass().removeAll("fixed-cell", "error-cell", "hint-cell");
        }

      }
    }
  }

  /**
   * Decreases the hints after every usage and disables the button when no hints are left
   */
  private void updateHintButton() {
    hintButton.setText("Hint (" + hintsLeft + ")");
    if (hintsLeft <= 0) {
      hintButton.setDisable(true);
    }
  }

  /**
   * Checks the current solution against the correct one
   */
  private void checkSolution() {
    boolean hasErrors = false;

    SudokuSolver solver = new SudokuSolver();
    SudokuBoard solvedBoard = board.clone();
    solver.solve(solvedBoard);

    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        TextField cell = cells[row][col];
        String text = cell.getText();

        if (text.isEmpty()) {
          cell.getStyleClass().add("error-cell");
          hasErrors = true;
          continue;
        }

        int value;
        try {
          value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
          cell.getStyleClass().add("error-cell");
          hasErrors = true;
          continue;
        }

        int correctValue = solvedBoard.getCell(row, col).getValue();
        boolean isFixed = board.getCell(row, col).isFixed();

        if (value != correctValue) {
          cell.getStyleClass().add("error-cell");
          hasErrors = true;
        } else {
          if (isFixed) {
            cell.getStyleClass().add("fixed-cell");
          } else {
            cell.getStyleClass().add("text-field");
          }
        }
      }
    }

    if (hasErrors) {
      showAlert("You can do it!", "Fix the errors and try again.");
    } else {
      timer.stop();
      int minutes = timeElapsed / 60;
      int seconds = timeElapsed % 60;
      String timeMessage = String.format("Your time is: %02d:%02d", minutes, seconds);

      showAlert("Hooray!", "Good job! You did it! 🎉" + timeMessage);
    }

    hasChecked = true;
  }

  /**
   * Resets the styles of the board to the default state
   */
  private void resetCellStyles() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        TextField cell = cells[row][col];
        SudokuCell sudokuCell = board.getCell(row, col);
        cell.getStyleClass().remove("error-cell");
      }
    }
  }

  /**
   * Displays the result from the sudoku check
   *
   * @param title the title of the dialog
   * @param message the content of the dialog
   */
  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Starts the game timer on every new game. Displays the time in mm:ss format.
   */
  private void startTimer() {
    timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      timeElapsed++;
      int minutes = timeElapsed / 60;
      int seconds = timeElapsed % 60;
      timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }));
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  /**
   * Stops and resets the timer
   */
  private void resetTimer() {
    timeElapsed = 0;
    if (timer != null) {
      timer.stop();
    }
  }

  /**
   * Starts the application
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
