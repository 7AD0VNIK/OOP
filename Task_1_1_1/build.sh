#!/bin/bash
set -e

# путь к JUnit jar
JUNIT_JAR="junit-platform-console-standalone-1.9.3.jar"

OUT_DIR="out"

echo "[1] Очистка сборок..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "[2] Компиляция исходников..."
find src -name "*.java" > sources.txt
javac -cp "$JUNIT_JAR" -d "$OUT_DIR" @sources.txt

echo "[3] Генерация документации..."
javadoc -d docs ./src/main/java/ru/nsu/k/sadov/sort/*.java

echo "[4] Запуск тестов..."
java -jar "$JUNIT_JAR" -cp "$OUT_DIR" --scan-class-path --include-classname=".*Test"

