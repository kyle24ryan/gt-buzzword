package edu.gatech.seclass.words6300.statistics;

import java.util.HashMap;
import io.realm.Realm;
import io.realm.RealmResults;

import edu.gatech.seclass.words6300.utilities.Letter;

public class LetterStats {

  public static HashMap<String, Letter> letters;
  private static Realm realm = Realm.getDefaultInstance();

  public static void drawn(String l) {

    RealmResults<Letter> results = realm.where(Letter.class).equalTo("letter", l).findAll();

    realm.beginTransaction();

    for(Letter letterToUpdate : results){
      letterToUpdate.updateDrawn();
    }

    realm.commitTransaction();
  }

  public static void played(String l) {

    RealmResults<Letter> results = realm.where(Letter.class).equalTo("letter", l).findAll();

    realm.beginTransaction();

    for(Letter letterToUpdate : results){
      letterToUpdate.updateUsedInWord();
    }

    realm.commitTransaction();
  }

  public static void swapped(String l) {

    RealmResults<Letter> results = realm.where(Letter.class).equalTo("letter", l).findAll();

    realm.beginTransaction();

    for(Letter letterToUpdate : results){
      letterToUpdate.updateTradedBack();
    }

    realm.commitTransaction();
  }
}
