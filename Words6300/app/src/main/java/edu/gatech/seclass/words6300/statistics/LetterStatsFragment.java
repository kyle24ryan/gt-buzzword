package edu.gatech.seclass.words6300.statistics;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.game.GameDetail;
import edu.gatech.seclass.words6300.utilities.Letter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class LetterStatsFragment extends Fragment {
    private ListView listview;

    public LetterStatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_letter_stats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            listview = activity.findViewById(R.id.letter_stats_listview);
        }

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Letter> realmResults = realm.where(Letter.class).findAll().sort("timesInWord", Sort.ASCENDING);
        List<Letter> letters = realm.copyFromRealm(realmResults);

        if (letters == null) {
            letters = new ArrayList<>();
        }

        LetterStatsAdapter adapter = new LetterStatsAdapter(Objects.requireNonNull(getContext()), letters);
        listview.setAdapter(adapter);
    }
}








