package edu.gatech.seclass.words6300.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Random;

import edu.gatech.seclass.words6300.settings.Settings;

import static org.junit.Assert.*;

public class GameDetailTest {

    private GameDetail gameDetail;
    private Random rand = new Random();

    @Mock
    private Settings gameSettings;

    @Before
    public void setUp() {
        final int finalScore = rand.nextInt(100);
        final int numberOfTurns = rand.nextInt(100);
        final double avgScorePerTurn = (double) finalScore / numberOfTurns;
        gameDetail = new GameDetail(finalScore, numberOfTurns, avgScorePerTurn, gameSettings);

    }

    @Test
    public void calcAvg() {
        final double exptAvgScorePerTurn = gameDetail.getNumberOfTurns() == 0
                ? 0.0 : (double) gameDetail.getFinalScore() / gameDetail.getNumberOfTurns();
        assertEquals(exptAvgScorePerTurn, gameDetail.getAvgScorePerTurn(), 0.001);
    }

    @Test
    public void updateScore() {
        final int score = rand.nextInt(100);
        final int currentScore = gameDetail.getFinalScore();
        final int updatedScore = currentScore + score;
        gameDetail.updateScore(score);
        assertEquals(updatedScore, gameDetail.getFinalScore());
    }

    @Test
    public void updateTurns() {
        final int currentNumOfTurns = gameDetail.getNumberOfTurns();
        final int updatedNumOfTurns = currentNumOfTurns + 1;
        gameDetail.updateTurns();
        assertEquals(updatedNumOfTurns, gameDetail.getNumberOfTurns());
    }

    @Test
    public void setFinalScore() {
        final int newScore = rand.nextInt(100);
        gameDetail.setFinalScore(newScore);
        assertEquals(newScore, gameDetail.getFinalScore());
    }

    @Test
    public void setAvgScorePerTurn() {
        final double newAvgScorePerTurn = rand.nextDouble();
        gameDetail.setAvgScorePerTurn(newAvgScorePerTurn);
        assertEquals(newAvgScorePerTurn, gameDetail.getAvgScorePerTurn(), 0.0001);
    }

    @Test
    public void setNumberOfTurns() {
        final int newNumOfTurns = rand.nextInt(100);
        gameDetail.setNumberOfTurns(newNumOfTurns);
        assertEquals(newNumOfTurns, gameDetail.getNumberOfTurns());
    }

}