package edu.gatech.seclass.words6300.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

import edu.gatech.seclass.words6300.menu.MainMenu;
import edu.gatech.seclass.words6300.settings.LetterSetting;
import edu.gatech.seclass.words6300.settings.Settings;
import edu.gatech.seclass.words6300.statistics.LetterStats;
import edu.gatech.seclass.words6300.utilities.Tile;

public class Game {
    private List<Tile> board;
    private List<Tile> rack;
    private List<Tile> pool;
    public static List<Tile> word;
    private GameDetail gameStatus;
    private HashSet<String> wordSelected;

    private int numOfTilesPlayed;
    private Settings settings;
    private Turn currentTurn;
    private boolean isGameEnded;
    public String swapped;
    private static Realm realm = Realm.getDefaultInstance();

    //triggered by clicked 'PLAY GAME'
    public void init() {
        /** case 1
         *  There is a unfinished game data in the DB, then load it fromm DB.
         *  This cannot be implemented without DB
         */


        /** case 2
         *  There is no unfinished game data in the DB, then initialize a new game.
         */

        //Step 0: Settings class needs to read data from user input.
        //        This has not been implemented in Setting class. All data is still hard coded

        //Step 1: get settings data from Settings class
        if (getSettings() == null) {

            RealmResults<Settings> mainSettings = realm.where(Settings.class).equalTo("mainSettings", true).findAll();

            realm.beginTransaction();
            settings = realm.createObject(Settings.class);

            if (!mainSettings.isEmpty()) {
                for(Settings s : mainSettings) {
                    settings.adjustMaxTurns(s.getMaxTurns());
                    realm.commitTransaction();
                    settings.adjustLetterSettings(s.getLetterSettings());
                }
            } else {
                realm.commitTransaction();
                settings.initSettings();
            }

        }
        //Step 2: initialize numOfTurns, tilesPlayed, and totalScore
        int numOfTurns = 0;
        int gameScore = 0;
        numOfTilesPlayed = 0;

        //Step 3: initialize pool
        pool = new ArrayList<>();
        List<LetterSetting> letterSettings = settings.getLetterSettings();
        for (int i = 0; i < letterSettings.size(); i++) {
            int distribution = letterSettings.get(i).getDistribution();
            for (int j = 0; j < distribution; j++) {
                Tile tile = new Tile();
                tile.setLetter(letterSettings.get(i).getLetter());
                tile.setPoints(letterSettings.get(i).getValue());
                pool.add(tile);
            }
        }

        //Step 4: initialize board (random select 4 tiles from pool, and delete them from pool)
        //use helper function to random select 4 elements from pool
        board = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int randomIndex = randomSelect(pool);
            //move random selected tiles from pool to board
            board.add(pool.get(randomIndex));
            LetterStats.drawn(pool.get(randomIndex).getLetter());
            pool.remove(randomIndex);
        }

        //Step 5: initialize rack (random select 7 tiles from pool, and delete them from pool)
        rack = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int randomIndex = randomSelect(pool);
            //move random selected tiles from pool to rack
            rack.add(pool.get(randomIndex));
            LetterStats.drawn(pool.get(randomIndex).getLetter());
            pool.remove(randomIndex);
        }
        word = new ArrayList<>();
        //Step 6: initialize hashmaps --- MOVED TO initialize when game created


        //Step 7: initialize gameDetails
        gameStatus = new GameDetail(gameScore, numOfTurns,0, settings);
    }

    //helper function to random select 1 element from list, return index
    private int randomSelect(List<Tile> list) {
        Random rand = new Random();
        return rand.nextInt(list.size());
    }

    void startNewTurn() {
        String wordPlayed = "";
        int turnScore = 0;
        List<Tile> tilesPlayed = new ArrayList<>();
        currentTurn = new Turn(wordPlayed, turnScore, tilesPlayed);
    }

    void leaveGame(List<Tile> word) {
        if (!endGame()) {
            isGameEnded = false;
            setWord(word);
        }
    }

    boolean endGame() {
        int maxTurns = getGameStatus().getGameSettings().getMaxTurns();
        int turnsTaken = getGameStatus().getNumberOfTurns();

        if (turnsTaken == maxTurns || (getRack().size() != 7 && getPool().isEmpty())) {
            /*
             * Things to do after a game is ended
             * 0. show game over and final score on screen (frontend)
             * 1. update game Detailshow
             */

            isGameEnded = true;

            //Step1: update gameDetail
            if (getPool().isEmpty() && getRack().size() < 7) {
                gameStatus.setFinalScore(gameStatus.getFinalScore() + 10);
            }

            realm.beginTransaction();

            GameDetail realmGameDetail = realm.createObject(GameDetail.class);
            realmGameDetail.setGameSettings(settings);
            realmGameDetail.setAvgScorePerTurn(gameStatus.getAvgScorePerTurn());
            realmGameDetail.setNumberOfTurns(gameStatus.getNumberOfTurns());
            realmGameDetail.setFinalScore(gameStatus.getFinalScore());

            realm.commitTransaction();
            
            MainMenu.setGameInProgress(null);
        }
        return isGameEnded;
    }

    void swapToPool(List<Tile> tilesToPool) {

        int count = 0;
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        if (tilesToPool.size() > getPool().size()) {
            //only swap up to pool.size()
            int numToSwap = getPool().size();

            for (int i = 0; i < numToSwap; i++) {
                LetterStats.swapped(tilesToPool.get(i).getLetter());
                pool.add(tilesToPool.get(i));
                rack.remove(tilesToPool.get(i));
                sb.append(tilesToPool.get(i).getLetter());
                count++;
            }
        } else {
            for (Tile t : tilesToPool) {
                LetterStats.swapped(t.getLetter());
                pool.add(t);
                rack.remove(t);
                sb.append(t.getLetter());
                count++;
            }
        }
        swapped = sb.toString();

        for (int i = 0; i < count; i++) {
            int drawIndex = rand.nextInt(pool.size());
            Tile drawn = pool.get(drawIndex);
            rack.add(drawn);
            LetterStats.drawn(pool.get(drawIndex).getLetter());
            pool.remove(drawIndex);
        }

        getGameStatus().updateTurns();
        endGame();
    }

    void replaceRack(List<Tile> tilesToReplace) {

        Random rand = new Random();

        for (Tile t : tilesToReplace) {
            rack.remove(t);
            Tile drawn = pool.get(rand.nextInt(pool.size()));
            rack.add(drawn);
            LetterStats.drawn(drawn.getLetter());
            pool.remove(drawn);
        }
    }

    void addToBoard(Tile tileToBoard) {
        board.add(tileToBoard);
    }

    void removeFromBoard(Tile tileFromBoard) {
        board.remove(tileFromBoard);
    }

    void addToRack(Tile tileToRack) {
        rack.add(tileToRack);
    }

    public void setWord(List<Tile> word) {
        Game.word = word;
    }

    public List<Tile> getWord() {
        return word;
    }

    List<Tile> getBoard() {
        return board;
    }

    List<Tile> getRack() {
        return rack;
    }

    List<Tile> getPool() {
        return pool;
    }

    GameDetail getGameStatus() {
        return gameStatus;
    }

    void setNumOfTilesPlayed(int numOfTilesPlayed) {
        this.numOfTilesPlayed = numOfTilesPlayed;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    boolean isGameInProgress() {
        return !isGameEnded;
    }

    int getNumOfTilesPlayed() {
        return numOfTilesPlayed;
    }

    Turn getCurrentTurn() {
        return currentTurn;
    }

    public String getSwapped() {
        return swapped;
    }
}
