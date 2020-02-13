package edu.gatech.seclass.words6300.menu;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.settings.SettingsActivity;
import edu.gatech.seclass.words6300.statistics.LetterStats;
import edu.gatech.seclass.words6300.statistics.StatisticsActivity;
import edu.gatech.seclass.words6300.game.GameActivity;
import edu.gatech.seclass.words6300.statistics.WordBankStats;
import edu.gatech.seclass.words6300.utilities.Letter;
import edu.gatech.seclass.words6300.utilities.Word;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
  public static MainMenu menu;
  static Realm realm;
  boolean create;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    Realm.init(this);
    RealmConfiguration realmConfig = new RealmConfiguration.Builder()
            .name("words.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfig);

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    TextView instructionsBtn = findViewById(R.id.instructions_button);
    Typeface type = Typeface.createFromAsset(this.getAssets(),  "font_awesome.ttf");
    instructionsBtn.setTypeface(type);

    initBuzzWord();
  }

  private void initBuzzWord() {
    menu = new MainMenu();
    realm = Realm.getDefaultInstance();
    create = false;

    RealmResults<Letter> results = realm.where(Letter.class).equalTo("letter", "A").findAll();
    if (results.isEmpty()) { create = true; }

    initializeLetters(create);

    WordBankStats wordBankStats = new WordBankStats();
    wordBankStats.setWordStats(new HashMap<String, Word>());
  }

  public static void initializeLetters(boolean create) {
    LetterStats.letters = new HashMap<>();

    for (int i = 0; i < 26; i++) {
      LetterStats.letters.put(String.valueOf((char)('A' + i)), new Letter(String.valueOf((char)('A' + i)), 0,0,0,0));

      if (create) {
        realm.beginTransaction();

        Letter newLetter = realm.createObject(Letter.class);
        newLetter.setLetter(String.valueOf((char) ('A' + i)));
        newLetter.setTimesDrawn(0);
        newLetter.setTimesInWord(0);
        newLetter.setTimesTradedBack(0);
        newLetter.setPercentageInWord(0);

        realm.commitTransaction();
      }
    }
  }

  public void playGame(View view) {
    menu.playGame();
    Intent game = new Intent(this, GameActivity.class);
    startActivity(game);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  public void changeSettings(View view) {
    Intent settings = new Intent(this, SettingsActivity.class);
    startActivity(settings);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  public void viewStats(View view) {
    Intent statistics = new Intent(this, StatisticsActivity.class);
    startActivity(statistics);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  }

  public void viewInstructions(View view) {
    Intent instructions = new Intent(this, ManualActivity.class);
    startActivity(instructions);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}
