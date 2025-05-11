#!/bin/bash

# Компилиране на проекта
javac --module-path /Users/emiliapavlova/Documents/Programs.nosync/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml -d out src/**/*.java

# Стартиране на приложението
java --module-path /Users/emiliapavlova/Documents/Programs.nosync/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml -cp out app.Main
