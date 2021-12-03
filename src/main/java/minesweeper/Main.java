package minesweeper;

public class Main {

    public static void main(String[] args) throws Exception {

        Game game = new Game();

        game.printUserView();

        game.play();

    }
}
