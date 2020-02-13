package edu.gatech.seclass.words6300.statistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.game.GameActivity;
import edu.gatech.seclass.words6300.game.GameDetail;
import edu.gatech.seclass.words6300.menu.MainActivity;
import edu.gatech.seclass.words6300.menu.MainMenu;
import edu.gatech.seclass.words6300.menu.ManualActivity;
import edu.gatech.seclass.words6300.settings.SettingsActivity;
import edu.gatech.seclass.words6300.utilities.Letter;
import edu.gatech.seclass.words6300.utilities.Word;
import io.realm.Realm;

public class StatisticsActivity extends AppCompatActivity {
  SimpleFragmentPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statistics);

    ActionBar ab = getSupportActionBar();
    assert ab != null;
    ab.setDisplayHomeAsUpEnabled(false);
    ab.setHomeButtonEnabled(true);
    ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

    ViewPager viewPager = findViewById(R.id.viewpager);
    adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    TabLayout tabLayout = findViewById(R.id.sliding_tabs);
    tabLayout.setupWithViewPager(viewPager);
  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.stats, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_exit:
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        break;
      case R.id.action_play:
        MainActivity.menu.playGame();
        Intent play = new Intent(this, GameActivity.class);
        startActivity(play);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        break;
      case R.id.action_settings:
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        break;
      case R.id.action_manual:
        Intent manual = new Intent(this, ManualActivity.class);
        startActivity(manual);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      default:
        break;
    }
    return true;
  }

    public void clearStats(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(Word.class).findAll().deleteAllFromRealm();
        realm.where(Letter.class).findAll().deleteAllFromRealm();
        realm.where(GameDetail.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        MainActivity.initializeLetters(true);
        this.recreate();
    }
}