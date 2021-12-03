package minesweeper;

import java.util.Scanner;

public class Game {

    Grid gameGrid;
    User userGrid;
    private final int totalMines;
    private int moves;
    boolean gamePlaying = true;
    int minesFound;
    int uncoveredCells;

    public Game() throws Exception {
        this.totalMines = getRequestedMines();
        this.gameGrid = new Grid(totalMines);
        this.userGrid = new User();
        this.moves = 0;
        this.minesFound = 0;
        this.uncoveredCells = Grid.totalCells - totalMines;
    }


    static int getRequestedMines() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        if (mines >= Grid.totalCells) {
            throw new Exception("You entered too many mines!");
        }
        System.out.println();
        return mines;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (gamePlaying) {

            UserMove userMove = new UserMove();

            while (userMove.isInvalidInput()) {
                try {

                    System.out.print("Set/unset mines marks or claim a cell as free: ");
                    userMove.setCol(scanner.nextInt());
                    userMove.setRow(scanner.nextInt());
                    userMove.setMove(scanner.next());
                } catch (Exception e) {
                    System.out.println("Invalid input");
                    scanner.close();
                    continue;
                }
                userMove.setInvalidInput(false);
            }

            processMove(userMove);

            System.out.println();

            if (gamePlaying) {
                printUserView();
            }

            //  System.out.println("Cells to uncover: " + uncoveredCells + " / " + Grid.totalCells);
            //  System.out.println("Mines found: " + minesFound + " / " + totalMines);

            // check if all mines have been found
            if (minesFound == totalMines || uncoveredCells == 0) {
                System.out.println("Congratulations! You found all the mines!");
                gamePlaying = false;
            }
        }

    }

    public void floodFill(boolean[][] visitted, int row, int col) {

        // quit if off the grid
        if (row <= 0 || row > 9 || col <= 0 || col > 9) {
            return;
        }

        // quit if visited:
        if (visitted[row - 1][col - 1]) {
            return;
        } else {
            visitted[row - 1][col - 1] = true;
        }

        // quit if mine
        if (gameGrid.isMine(row, col)) {
            return;
        }

        // if number, populate cell then return
        if (gameGrid.isNumber(row, col)) {
            userGrid.setCell(row, col, gameGrid.getCell(row, col));
            uncoveredCells -= 1;
            return;
        }

        // set user grid equivalent to cell in original grid
        userGrid.setCell(row, col, gameGrid.getCell(row, col));
        uncoveredCells -= 1;


        // recursively fill in all directions

        // cell above
        floodFill(visitted, row + 1, col);
        // cell above
        floodFill(visitted, row - 1, col);
        // cell to right
        floodFill(visitted, row, col + 1);
        // cell to left
        floodFill(visitted, row, col - 1);
        // top right diag
        floodFill(visitted, row + 1, col + 1);
        // top left diag
        floodFill(visitted, row + 1, col - 1);
        // botton right diag
        floodFill(visitted, row - 1, col + 1);
        // bottom left diag
        floodFill(visitted, row - 1, col - 1);

    }

    void processMove(UserMove userMove) {
        switch (userMove.getMove()) {
            case "mine":
                if (userGrid.isStar(userMove.getRow(), userMove.getCol())) {
                    userGrid.setCell(userMove.getRow(), userMove.getCol(), '.');
                    break;
                }
                if (gameGrid.isMine(userMove.getRow(), userMove.getCol())) {
                    userGrid.setCell(userMove.getRow(), userMove.getCol(), '*');
                    incrementMinesFound();
                } else if (userGrid.isStar(userMove.getRow(), userMove.getCol())) {
                    userGrid.setCell(userMove.getRow(), userMove.getCol(), '.');
                } else {
                    userGrid.setCell(userMove.getRow(), userMove.getCol(), '*');
                }
                break;
            case "free":

                if (gameGrid.isMine(userMove.getRow(), userMove.getCol())) {
                    if (moves == 0) {
                        gameGrid.generateNewGrid();
                        processMove(userMove);
                        return;
                    }
                    userGrid.setCell(userMove.getRow(), userMove.getCol(), 'X');
                    revealAllMines();
                    System.out.println();
                    printUserView();
                    System.out.println("You stepped on a mine and failed!");
                    gamePlaying = false;
                    return;
                } else if (gameGrid.isFree(userMove.getRow(), userMove.getCol()) || gameGrid.isNumber(userMove.getRow(), userMove.getCol())) {
                    floodFill(new boolean[9][9], userMove.getRow(), userMove.getCol());
                }
                incrementMoves();
                break;
            default:
                System.out.println("Please enter a valid input");
        }
    }

    private void revealAllMines() {
        for (int i = 0; i < Grid.gridSize; i++) {
            for (int j = 0; j < Grid.gridSize; j++) {
                if (gameGrid.isMine(j + 1, i + 1)) {
                    userGrid.setCell(j + 1, i + 1, 'X');
                }
            }
        }
    }

    void printUserView() {
        userGrid.printGrid();
    }

    void printGameView() {
        System.out.println("GAME VIEW:");
        gameGrid.printGrid();
    }

    void incrementMoves() {
        this.moves++;
    }

    void incrementMinesFound() {
        this.minesFound++;
    }


}

