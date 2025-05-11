package model;

/**
 * Every cell on the board
 */
public class SudokuCell {
  /** The value of the cell (0 = empty, 1-9 = numbers) */
  private int value;

  /** Fixed cell is shown on the board as a clue when the game starts and is not editable */
  private boolean fixed;

  /**
   * Constructs a SudokuCell with the given value and fixed status
   *
   * @param value the initial value of the cell (0-9)
   * @param fixed whether the cell is fixed
   */
  public SudokuCell(int value, boolean fixed) {
    this.value = value;
    this.fixed = fixed;
  }

  /**
   * Returns the value of the cell
   *
   * @return the cell value
   */
  public int getValue() {
    return value;
  }

  /**
   * Sets the value of the cell
   *
   * @param value the value to set (0-9 allowed)
   */
  public void setValue(int value) {
    if (!fixed) {
      this.value = value;
    }
  }

  /**
   * Returns whether the cell is fixed
   *
   * @return true if the cell is fixed, false if not
   */
  public boolean isFixed() {
    return fixed;
  }

  /**
   * Sets whether the cell is fixed
   *
   * @param fixed true if the cell is fixed, false if not
   */
  public void setFixed(boolean fixed) {
    this.fixed = fixed;
  }
}
