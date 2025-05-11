package model;

import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating valid Sudoku puzzle
 */
public class SudokuGenerator {
  private static final Random random = new Random();

  /**
   * Generates a board with the specified number of clues
   *
   * @param clues the number of pre-filled cells on the board
   * @return a board object with a valid puzzle
   */
  public static SudokuBoard generate(int clues) {
    SudokuBoard board = new SudokuBoard();
    fillBoard(board);
    removeCells(board, 81 - clues);
    return board;
  }

  /**
   * Recursively fills the board with a valid solution using backtracking
   *
   * @param board the board of the puzzle to be solved
   * @return true if the board is successfully filled, false if not
   */
  private static boolean fillBoard(SudokuBoard board) {
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        if (board.getCell(row, col).getValue() == 0) {
          List<Integer> numbers = shuffledNumbers();
          for (int num : numbers) {
            if (board.isValidMove(row, col, num)) {
              board.getCell(row, col).setValue(num);
              if (fillBoard(board)) return true;
              board.getCell(row, col).setValue(0);
            }
          }
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Generates a shuffled list of numbers from 1 to 9 that are used to fill the board
   *
   * @return shuffled list of integers 1–9
   */
  private static List<Integer> shuffledNumbers() {
    List<Integer> nums = new ArrayList<>();
    for (int i = 1; i <= 9; i++) nums.add(i);
    Collections.shuffle(nums);
    return nums;
  }

  /**
   * Randomly removes cells values from a solved board
   *
   * @param board the filled board
   * @param cellsToRemove the number of cells to blank out
   */
  private static void removeCells(SudokuBoard board, int cellsToRemove) {
    int removed = 0;
    while (removed < cellsToRemove) {
      int row = random.nextInt(9);
      int col = random.nextInt(9);
      SudokuCell cell = board.getCell(row, col);
      if (cell.getValue() != 0) {
        cell.setValue(0);
        cell.setFixed(false);
        removed++;
      }
    }
  }
}
