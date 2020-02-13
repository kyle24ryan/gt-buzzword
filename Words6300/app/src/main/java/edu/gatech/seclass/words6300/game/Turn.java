package edu.gatech.seclass.words6300.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.gatech.seclass.words6300.menu.MainMenu;
import edu.gatech.seclass.words6300.utilities.Tile;
import edu.gatech.seclass.words6300.utilities.Word;
import edu.gatech.seclass.words6300.statistics.LetterStats;
import edu.gatech.seclass.words6300.statistics.WordBankStats;

public class Turn {
    private Game currentGame = MainMenu.getGameInProgress();
    private String wordPlayed;
    private int score;
    private List<Tile> tilesPlayed;

    Turn(String wordPlayed, int score, List<Tile> tilesPlayed) {
        this.wordPlayed = wordPlayed;
        this.score = score;
        this.tilesPlayed = tilesPlayed;
    }

    void chooseWord(List<Tile> tilesChosen) {

        for (Tile t : tilesChosen) {
            tilesPlayed.add(t);
            score += t.getPoints();
            LetterStats.played(t.getLetter());
        }

        updateTilesPlayed();
        updateScore();
        updateWordBank();

        if (!(currentGame.endGame() && currentGame.getPool().isEmpty())) {
            replaceTiles();
        }
    }

    private void updateTilesPlayed() {
        currentGame.setNumOfTilesPlayed(currentGame.getNumOfTilesPlayed() + tilesPlayed.size());
    }

    private void replaceTiles() {

        Random rand = new Random();
        List<Tile> tilesFromRack = new ArrayList<>();

        for (Tile t : tilesPlayed) {
            if(!t.isBoardTile()) {
                tilesFromRack.add(t);
            }
            else {
                for (Tile boardTile : currentGame.getBoard()) {
                    boardTile.setBoardTile(false);
                    boardTile.setBoardTilePlayedAlready(false);
                }
                currentGame.removeFromBoard(t);
            }
        }

        Tile newBoardTile = tilesFromRack.get(rand.nextInt(tilesFromRack.size()));
        currentGame.addToBoard(newBoardTile);
        currentGame.replaceRack(tilesFromRack);
    }

    private void updateScore() {
        currentGame.getGameStatus().updateScore(score);
        currentGame.getGameStatus().updateTurns();
        currentGame.getGameStatus().calcAvg();
    }

    private void updateWordBank() {
        Word lookup = WordBankStats.wordBank.get(wordPlayed);
        Word wordToStore;
        if (lookup != null) {
            int timesPlayed = lookup.getTimesPlayed();
            wordToStore = new Word(wordPlayed, timesPlayed + 1);
        } else {
            wordToStore = new Word(wordPlayed, 1);
        }
        wordToStore.setTimeLastPlayed(System.currentTimeMillis());
        WordBankStats.update(wordPlayed, wordToStore);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    void setWordPlayed(String wordPlayed) {
        this.wordPlayed = wordPlayed;
    }

    String getWordPlayed() {
        return wordPlayed;
    }
}
