package edu.gatech.seclass.words6300.settings;

import io.realm.RealmObject;

public class LetterSetting extends RealmObject {
  private String letter;
  private int distribution;
  private int value;

  public LetterSetting() {}

  public String getLetter() {
    return letter;
  }

  public void setLetter(String letter) {
    this.letter = letter;
  }

  public int getDistribution() {
    return distribution;
  }

  public void setDistribution(int distribution) {
    this.distribution = distribution;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
