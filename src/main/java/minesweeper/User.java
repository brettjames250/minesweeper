package minesweeper;

public class User {

    private final char[][] userGrid;

    User() {
        this.userGrid = new char[9][9];
        fillUserGrid();
    }

    private void fillUserGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                userGrid[i][j] = '.';
            }
        }
    }

    void printGrid() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < Grid.gridSize; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < userGrid[i].length; j++) {
                System.out.print(userGrid[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public void setCell(int row, int col, char c) {
        userGrid[row - 1][col - 1] = c;
    }

    public char getCell(int row, int col) {
        return userGrid[col - 1][row - 1];
    }

    boolean isStar(int row, int col) {
        return userGrid[row - 1][col - 1] == '*';
    }
}

