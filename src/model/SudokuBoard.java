package model;

public class SudokuBoard {

  /** The grid for the board cells */
  private SudokuCell[][] board;

  /** The grid size */
  private static final int GRID_SIZE = 9;

  /** The size of each 3x3 subgrid in the board */
  private static final int SQUARE_SIZE = 3;

  /**
   * Constructs a new empty Sudoku board, where all cells are initialized with value 0 and marked as non-fixed.
   */
  public SudokuBoard() {
    board = new SudokuCell[GRID_SIZE][GRID_SIZE];

    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        board[row][col] = new SudokuCell(0, false);
      }
    }
  }

  /**
   * Returns the {@link SudokuCell} at the specified row and column.
   *
   * @param row the row index (0–8)
   * @param col the column index (0–8)
   * @return the cell at the specified position
   */
  public SudokuCell getCell(int row, int col)
  {
    return board[row][col];
  }

  /**
   * Sets a new {@link SudokuCell} at the specified location with the given value and fixed flag.
   *
   * @param row the row index (0–8)
   * @param col the column index (0–8)
   * @param value the value to set (0–9, where 0 means empty)
   * @param fixed not editable cell used as a clue
   */
  public void setCell(int row, int col, int value, boolean fixed)
  {
    board[row][col] = new SudokuCell(value, fixed);
  }

  /**
   * Checks whether placing a number in a specific cell is a valid move
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @param number the number to validate (1–9)
   * @return true if the move is valid or false if not
   */
  public boolean isValidMove(int row, int col, int number)  {
    // check row
    for (int i = 0; i < GRID_SIZE; i++) {
      if (board[row][i].getValue() == number) {
        return false;
      }
    }

    // check column
    for (int i = 0; i < GRID_SIZE; i++) {
      if (board[i][col].getValue() == number) {
        return false;
      }
    }

    // check square
    int startRow = (row / SQUARE_SIZE) * SQUARE_SIZE;
    int startCol = (col / SQUARE_SIZE) * SQUARE_SIZE;
    for (int i = 0; i < SQUARE_SIZE; i++) {
      for (int j = 0; j < SQUARE_SIZE; j++) {
        if (board[startRow + i][startCol + j].getValue() == number) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Creates a deep copy of the Sudoku board.
   *
   * @return a new Sudoku board instance with copied cell values and fixed flags
   */
  @Override
  public SudokuBoard clone() {
    SudokuBoard copy = new SudokuBoard();
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        SudokuCell cell = this.getCell(row, col);
        copy.setCell(row, col, cell.getValue(), cell.isFixed());
      }
    }
    return copy;
  }
}