package edu.gatech.seclass.words6300.menu;

import edu.gatech.seclass.words6300.game.Game;

public class MainMenu {
    private static Game gameInProgress;

    public void playGame() {
        if (gameInProgress == null) {
            Game newGame = new Game();
            newGame.init();
            gameInProgress = newGame;
        }
    }

    public static Game getGameInProgress() {
        return gameInProgress;
    }

    public static void setGameInProgress(Game gameInProgress) {
        MainMenu.gameInProgress = gameInProgress;
    }
}