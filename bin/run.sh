#!/usr/bin/env bash
set -euo pipefail

PUZZLE=""
LIST_ONLY=false
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

usage() {
    echo "[*] usage: $0 -p <puzzle-name> | -l"
    exit 1
}

list_puzzles() {
    echo "[*] available puzzles:"
    ls -1 "$ROOT_DIR/puzzles"
}

while getopts ":p:l" opt; do
    case $opt in
        p) PUZZLE="$OPTARG" ;;
        l) LIST_ONLY=true ;;
        *) usage ;;
    esac
done

if [ "$LIST_ONLY" = true ]; then
    list_puzzles
    exit 0
fi

if [ -z "$PUZZLE" ]; then
    usage
fi

PUZZLE_DIR="$ROOT_DIR/puzzles/$PUZZLE"
BUILD_DIR="$ROOT_DIR/build/$PUZZLE"

if [ ! -d "$PUZZLE_DIR" ]; then
    echo "[x] error : the puzzle '$PUZZLE' doesn't exist in $PUZZLE_DIR"
    echo ""
    list_puzzles
    exit 1
fi

if [ ! -f "$PUZZLE_DIR/Solver.java" ]; then
    echo "[x] error : $PUZZLE_DIR/Solver.java doesn't exist"
    exit 1
fi

mkdir -p "$BUILD_DIR"

javac -encoding UTF-8 -d "$BUILD_DIR" "$PUZZLE_DIR"/*.java

echo "[+] resolving : $PUZZLE"
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp "$BUILD_DIR" Solver