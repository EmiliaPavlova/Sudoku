package model;

/**
 * A Sudoku solver that uses a backtracking algorithm to fill in the board
 */
public class SudokuSolver {

  /**
   * Attempts to solve the given Sudoku board using a recursive backtracking strategy
   *
   * @param board the Sudoku board to solve
   * @return true if the board was successfully solved, false if no solution
   */
  public boolean solve(SudokuBoard board) {
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        SudokuCell cell = board.getCell(row, col);

        if (cell.getValue() == 0) {
          for (int num = 1; num <= 9; num++) {
            if (board.isValidMove(row, col, num)) {
              cell.setValue(num);

              if (solve(board)) {
                return true;
              }

              cell.setValue(0);
            }
          }
          return false;
        }
      }
    }
    return true;
  }
}
