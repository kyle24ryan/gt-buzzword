package edu.gatech.seclass.words6300.game;

import edu.gatech.seclass.words6300.settings.Settings;
import io.realm.RealmObject;

public class GameDetail extends RealmObject {

    private int finalScore;
    private int numberOfTurns;
    private double avgScorePerTurn;
    private Settings gameSettings;

    public GameDetail() {}

    GameDetail(int finalScore, int numberOfTurns, double avgScorePerTurn, Settings gameSettings) {
        this.finalScore = finalScore;
        this.numberOfTurns = numberOfTurns;
        this.avgScorePerTurn = avgScorePerTurn;
        this.gameSettings = gameSettings;
    }

    void calcAvg() {
        if (numberOfTurns == 0) {
            avgScorePerTurn = 0.0;
        } else {
            avgScorePerTurn = (double) getFinalScore() / getNumberOfTurns();
        }
    }

    void updateScore(int score) {
        finalScore += score;
    }

    void updateTurns() {
        numberOfTurns++;
    }

    void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    void setAvgScorePerTurn(double avgScorePerTurn) {
        this.avgScorePerTurn = avgScorePerTurn;
    }

    void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    void setGameSettings(Settings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public double getAvgScorePerTurn() {
        return avgScorePerTurn;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public Settings getGameSettings() {
        return gameSettings;
    }
}

