package edu.gatech.seclass.words6300.utilities;
import io.realm.RealmObject;

public class Letter extends RealmObject {
    private String letter;
    private int timesDrawn;
    private int timesInWord;
    private int timesTradedBack;
    private double percentageInWord;

    public Letter() {}

    public Letter(String letter, int timesDrawn, int timesInWord, int timesTradedBack, float percentageInWord) {
        this.letter = letter;
        this.timesDrawn = timesDrawn;
        this.timesInWord = timesInWord;
        this.timesTradedBack = timesTradedBack;
        this.percentageInWord = percentageInWord;
    }

    public void updateDrawn() {
        timesDrawn++;
        calcPercentageInWord();
    }

    public void updateUsedInWord() {
        timesInWord++;
        calcPercentageInWord();
    }

    public void updateTradedBack() {
        timesTradedBack++;
    }

    private void calcPercentageInWord() {
        if (timesDrawn != 0) {
            percentageInWord = (((float)timesInWord)/timesDrawn) * 100;
        } else {
            percentageInWord = 0;
        }
    }

    public String getLetter() {
        return letter;
    }

    public double getPercentageInWord() {
        return percentageInWord;
    }

    public int getTimesInWord() {
        return timesInWord;
    }

    public int getTimesTradedBack() {
        return timesTradedBack;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setPercentageInWord(double percentageInWord) {
        this.percentageInWord = percentageInWord;
    }

    public void setTimesDrawn(int timesDrawn) {
        this.timesDrawn = timesDrawn;
    }

    public void setTimesInWord(int timesInWord) {
        this.timesInWord = timesInWord;
    }

    public void setTimesTradedBack(int timesTradedBack) {
        this.timesTradedBack = timesTradedBack;
    }
}
