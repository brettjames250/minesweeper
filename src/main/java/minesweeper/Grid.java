package minesweeper;

import java.util.ArrayList;
import java.util.Random;

class Grid {
    private final char[][] grid;
    final static int totalCells = 81;
    final static int gridSize = 9;
    final int totalMines;
    ArrayList<int[]> mineCoordinates;

    public Grid(int totalMines) {
        this.grid = new char[9][9];
        this.totalMines = totalMines;
        generateNewGrid();
    }

    void generateNewGrid() {
        fillUserGrid();
        placeMines(totalMines);
        placeHints();
    }

    private void fillUserGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = '.';
            }
        }
    }

    void printGrid() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < Grid.gridSize; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }


    void placeMines(int numMines) {
        this.mineCoordinates = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numMines; i++) {
            boolean mineNotPlaced = true;
            while (mineNotPlaced) {
                int randomRow = random.nextInt(gridSize);
                int randomCol = random.nextInt(gridSize);
                int[] coordinates = new int[]{randomCol + 1, randomRow + 1};

                if (!(grid[randomRow][randomCol] == 'X')) {
                    grid[randomRow][randomCol] = 'X';
                    mineNotPlaced = false;
                    mineCoordinates.add(coordinates);
                }
            }
        }

    }

    void placeHints() {
        int countOfMines;

        // iterate over entire grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 'X') {
                    countOfMines = getNumMinesNear(i, j);
                    grid[i][j] = countOfMines != 0 ? Character.forDigit(countOfMines, 10) : '/';

                }
            }
        }
    }

    int getNumMinesNear(int row, int col) {
        int minesCount = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // look at row above, actual row and row below
        for (int i = row - 1; i <= row + 1; i++) {
            // check if row is even in grid
            if (isInBounds(i, rows)) {
                // check cell to left, actual cell and cell to right
                for (int j = col - 1; j <= col + 1; j++) {
                    // check if cell is even in grid
                    if (isInBounds(j, cols)) {
                        // if cell is X, then increment counter
                        if (grid[i][j] == 'X') {
                            minesCount++;
                        }

                    }
                }
            }
        }
        return minesCount;
    }

    boolean isInBounds(int x, int boundary) {
        return x >= 0 && x < boundary;
    }

    public char getCell(int row, int col) {
        return grid[row - 1][col - 1];
    }

    public boolean isNumber(int row, int col) {
        return Character.isDigit(grid[row - 1][col - 1]);
    }

    boolean isMine(int row, int col) {
        return grid[row - 1][col - 1] == 'X';
    }

    public boolean isFree(int row, int col) {
        return grid[row - 1][col - 1] == '/';
    }

    public char[][] getGrid() {
        return this.grid;
    }
}

