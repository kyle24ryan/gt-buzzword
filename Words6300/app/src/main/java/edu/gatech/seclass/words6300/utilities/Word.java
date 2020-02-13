package edu.gatech.seclass.words6300.utilities;

import static java.lang.System.currentTimeMillis;
import io.realm.RealmObject;

public class Word extends RealmObject {
    private String word;
    private int timesPlayed;
    private double timeLastPlayed;

    public Word() {}

    public Word(String w, int t) {
        word = w;
        timesPlayed = t;
        timeLastPlayed = currentTimeMillis();
    }

    public double getTimeLastPlayed() {
        return timeLastPlayed;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public String getWord() {
        return word;
    }

    public void setTimeLastPlayed(double timeLastPlayed) {
        this.timeLastPlayed = timeLastPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
