# Sudoku
A project for NBU master's degree course in Java.

A classic sudoku game, created in Java and JavaFX. Supports check, hints, starting new game.

- Start the project `java -jar Sudoku.jar`
- Run tests `java -cp out test.TestSudoku`
- After code changes
  - compilation `java --module-path [path-to-JavaFX]/lib --add-modules javafx.controls,javafx.fxml -d out src/*/*.java`
  - start `java --module-path [path-to-JavaFX]/lib --add-modules javafx.controls,javafx.fxml -cp out app.Main` 