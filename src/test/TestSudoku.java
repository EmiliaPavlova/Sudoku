package test;

import model.*;

public class TestSudoku {
  public static void main(String[] args) {
    testSolverOnPresetBoard();
    testValidMove();
    testCloneBoard();
    testGeneratorCluesCount();
    testSolveEmptyBoard();
  }

  private static void testSolverOnPresetBoard() {
    SudokuBoard board = new SudokuBoard();
    int[][] preset = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        int value = preset[row][col];
        boolean fixed = value != 0;
        board.setCell(row, col, value, fixed);
      }
    }

    SudokuSolver solver = new SudokuSolver();
    boolean solved = solver.solve(board);

    if (solved) {
      System.out.println("testSolverOnPresetBoard passed");
      printBoard(board);
    } else {
      System.out.println("testSolverOnPresetBoard failed: could not solve");
    }
  }

  private static void testValidMove() {
    SudokuBoard board = new SudokuBoard();
    board.setCell(0, 0, 5, true);

    assert !board.isValidMove(0, 1, 5) : "5 should not be valid in same row";
    assert !board.isValidMove(1, 0, 5) : "5 should not be valid in same column";
    assert !board.isValidMove(1, 1, 5) : "5 should not be valid in same square";
    assert board.isValidMove(4, 4, 5) : "5 should be valid in an empty spot";

    System.out.println("✅ testValidMove passed");
  }

  private static void testCloneBoard() {
    SudokuBoard board1 = new SudokuBoard();
    board1.setCell(0, 0, 9, true);
    SudokuBoard board2 = board1.clone();
    board2.setCell(0, 0, 3, true);
    assert board1.getCell(0, 0).getValue() == 9 : "Original board should remain unchanged";
    System.out.println("testCloneBoard passed.");
  }

  public static void testGeneratorCluesCount() {
    int clues = 30;
    SudokuBoard board = SudokuGenerator.generate(clues);
    int filled = 0;
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (board.getCell(i, j).getValue() != 0) filled++;
      }
    }
    assert filled == clues : "Expected " + clues + " clues, got " + filled;
    System.out.println("testGeneratorCluesCount passed.");
  }

  public static void testSolveEmptyBoard() {
    SudokuBoard board = new SudokuBoard();
    SudokuSolver solver = new SudokuSolver();
    boolean result = solver.solve(board);
    assert result : "Solver should solve an empty board";
    System.out.println("testSolveEmptyBoard passed.");
  }

  private static void printBoard(SudokuBoard board) {
    for (int row = 0; row < 9; row++) {
      if (row % 3 == 0 && row != 0) System.out.println("------+-------+------");
      for (int col = 0; col < 9; col++) {
        if (col % 3 == 0 && col != 0) System.out.print("| ");
        System.out.print(board.getCell(row, col).getValue() + " ");
      }
      System.out.println();
    }
  }
}
