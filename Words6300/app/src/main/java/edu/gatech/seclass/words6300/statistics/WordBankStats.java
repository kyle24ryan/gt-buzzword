package edu.gatech.seclass.words6300.statistics;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

import edu.gatech.seclass.words6300.utilities.Word;

public class WordBankStats {

  public static HashMap<String, Word> wordBank;
  private static Realm realm = Realm.getDefaultInstance();

  public static void update(String playedString, Word playedWord) {
    wordBank.put(playedString, playedWord);

    RealmResults<Word> results = realm.where(Word.class).equalTo("word", playedString).findAll();

    realm.beginTransaction();

    if (results.isEmpty()) {
      Word realmPlayedWord = realm.createObject(Word.class);

      realmPlayedWord.setWord(playedWord.getWord());
      realmPlayedWord.setTimeLastPlayed(playedWord.getTimeLastPlayed());
      realmPlayedWord.setTimesPlayed(playedWord.getTimesPlayed());
    }
    else {
      for(Word realmPlayedWord : results){
        realmPlayedWord.setTimeLastPlayed(System.currentTimeMillis());
        realmPlayedWord.setTimesPlayed(realmPlayedWord.getTimesPlayed()+1);
      }
    }

    realm.commitTransaction();
  }

  public void setWordStats(HashMap<String, Word> wordBank) {
    WordBankStats.wordBank = wordBank;
  }

  public static HashMap<String, Word> getWordBank() {
    return wordBank;
  }
}
