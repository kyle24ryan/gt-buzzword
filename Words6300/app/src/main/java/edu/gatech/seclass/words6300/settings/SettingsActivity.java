package edu.gatech.seclass.words6300.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import java.util.List;
import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.game.GameActivity;
import edu.gatech.seclass.words6300.menu.MainActivity;
import edu.gatech.seclass.words6300.menu.MainMenu;
import edu.gatech.seclass.words6300.menu.ManualActivity;
import edu.gatech.seclass.words6300.statistics.StatisticsActivity;
import io.realm.Realm;
import io.realm.RealmResults;

public class SettingsActivity extends AppCompatActivity {
  private Settings newSettings;
  private GridView gridview;

  static Realm realm = Realm.getDefaultInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ActionBar ab = getSupportActionBar();
    assert ab != null;
    ab.setDisplayHomeAsUpEnabled(false);
    ab.setHomeButtonEnabled(true);
    ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

    final List<LetterSetting> settingsList;
    TextView maxTurns = findViewById(R.id.maxTurns);

    RealmResults<Settings> mainSettings = realm.where(Settings.class).equalTo("mainSettings", true).findAll();

    if (mainSettings.isEmpty()) {

      realm.beginTransaction();
      Settings realmSettings = realm.createObject(Settings.class);
      realmSettings.setMainSettings();
      realm.commitTransaction();

      realmSettings.initSettings();

      settingsList = realmSettings.getLetterSettings();
      maxTurns.setText(String.valueOf(realmSettings.getMaxTurns()));
    } else {
      Settings lastSetting = new Settings();

      for(Settings s : mainSettings) {
        lastSetting = s;
      }
      settingsList = lastSetting.getLetterSettings();
      maxTurns.setText(String.valueOf(lastSetting.getMaxTurns()));
    }

    if (MainMenu.getGameInProgress() != null) {
      View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings_game_in_progress, null);

      AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          MainMenu.setGameInProgress(null);
        }
      });
      builder.setNegativeButton("DISMISS", null);
      builder.setView(dialogView);

      final AlertDialog dialog = builder.create();
      dialog.setOnShowListener( new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface arg0) {
          dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
          dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
          dialog.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
          dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setGravity(Gravity.CENTER);
        }
      });
      dialog.setCanceledOnTouchOutside(false);
      dialog.show();
    }

    SettingsAdapter adapter = new SettingsAdapter(this, settingsList);

    gridview = findViewById(R.id.game_settings_gridview);
    gridview.setAdapter(adapter);
    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final LetterSetting tile = settingsList.get(position);
        String letter = tile.getLetter();
        int distribution = tile.getDistribution();
        int value = tile.getValue();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_tile_setting, parent, false);
        final ScrollableNumberPicker dialogDistribution = dialogView.findViewById(R.id.dialog_distribution_picker);
        final ScrollableNumberPicker dialogValue = dialogView.findViewById(R.id.dialog_value_picker);

        TextView tileLetter = dialogView.findViewById(R.id.dialog_letter);
        Typeface type = Typeface.createFromAsset(SettingsActivity.this.getAssets(),  "scramble_mixed.ttf");
        tileLetter.setTypeface(type);
        tileLetter.setText(letter);

        TextView tileDistribution = dialogView.findViewById(R.id.dialog_tile_distribution);
        tileDistribution.setText(String.format("x%s", String.valueOf(distribution)));

        TextView tileValue = dialogView.findViewById(R.id.dialog_tile_value);
        tileValue.setText(String.valueOf(value));

        dialogDistribution.setValue(distribution);
        dialogValue.setValue(value);

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            realm.beginTransaction();
            tile.setValue(dialogValue.getValue());
            tile.setDistribution(dialogDistribution.getValue());
            gridview.invalidateViews();
            settingsList.set(position, tile);
            realm.commitTransaction();
          }
        });
        builder.setNegativeButton("DISMISS", null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
          @Override
          public void onShow(DialogInterface arg0) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setGravity(Gravity.CENTER);
          }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
      }
    });

    final View maxTurnsLayout = (View) findViewById(R.id.maxTurnsLayout);
    maxTurnsLayout.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        final TextView maxTurnsTextView = findViewById(R.id.maxTurns);
        int maxTurns = Integer.parseInt(maxTurnsTextView.getText().toString());
        final View maxTurnsView = getLayoutInflater().inflate(R.layout.dialog_max_turns, null);
        final ScrollableNumberPicker maxTurnsPicker = maxTurnsView.findViewById(R.id.dialog_max_turns_picker);
        maxTurnsPicker.setValue(maxTurns);

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            RealmResults<Settings> mainSettings = realm.where(Settings.class).equalTo("mainSettings", true).findAll();
            for(Settings s : mainSettings) {
              newSettings = s;
            }
            realm.beginTransaction();
            newSettings.adjustMaxTurns(maxTurnsPicker.getValue());
            realm.commitTransaction();
            maxTurnsTextView.setText(String.valueOf(maxTurnsPicker.getValue()));
            maxTurnsTextView.invalidate();
          }
        });
        builder.setNegativeButton("DISMISS", null);
        builder.setView(maxTurnsView);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
          @Override
          public void onShow(DialogInterface arg0) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setGravity(Gravity.CENTER);
          }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
      }
    });

  }

  @Override
  public void onBackPressed() {
    if (MainMenu.getGameInProgress() == null) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    } else {
      super.onBackPressed();
    }

    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.settings, menu);
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
      case R.id.action_stats:
        Intent stats = new Intent(this, StatisticsActivity.class);
        startActivity(stats);
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

  public void restoreDefaults(View view) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.where(Settings.class).findAll().deleteAllFromRealm();
    realm.commitTransaction();

    this.recreate();
  }
}

