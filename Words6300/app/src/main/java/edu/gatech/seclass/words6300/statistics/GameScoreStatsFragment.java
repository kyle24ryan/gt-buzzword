package edu.gatech.seclass.words6300.statistics;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.game.GameDetail;
import edu.gatech.seclass.words6300.menu.MainMenu;
import edu.gatech.seclass.words6300.settings.LetterSetting;
import edu.gatech.seclass.words6300.settings.SettingsAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameScoreStatsFragment extends Fragment {
    private ListView listview;

    public GameScoreStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_game_stats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();
        if (activity != null) {
            listview = activity.findViewById(R.id.game_stats_listview);
        }

        if (MainMenu.getGameInProgress() != null) {
            Button clearStats = activity.findViewById(R.id.clear_button);
            clearStats.setVisibility(View.GONE);
        }

        Realm realm = Realm.getDefaultInstance();

        RealmResults<GameDetail> realmResults = realm.where(GameDetail.class).findAll().sort("finalScore", Sort.DESCENDING);
        List<GameDetail> detailsList = realm.copyFromRealm(realmResults);

        if (detailsList == null) {
            detailsList = new ArrayList<>();
        }

        GameScoreStatsAdapter gameScoreStatsAdapter = new GameScoreStatsAdapter(Objects.requireNonNull(getContext()), detailsList);
        listview.setAdapter(gameScoreStatsAdapter);


        final List<GameDetail> finalDetailsList = detailsList;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_game_stats_item, null);

                int maxTurns = finalDetailsList.get(position).getGameSettings().getMaxTurns();
                List<LetterSetting> settingsList = finalDetailsList.get(position).getGameSettings().getLetterSettings();

                SettingsAdapter settingsAdapter = new SettingsAdapter(parent.getContext(), settingsList);
                TextView textview = dialogView.findViewById(R.id.dialogMaxTurns);
                GridView gridview = dialogView.findViewById(R.id.game_stats_gridview);

                gridview.setAdapter(settingsAdapter);
                textview.setText(getString(R.string.dialog_max_turns, maxTurns));

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setNegativeButton("Dismiss", null)
                        .show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }
}
