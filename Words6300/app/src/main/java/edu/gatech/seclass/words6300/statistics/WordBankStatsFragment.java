package edu.gatech.seclass.words6300.statistics;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.utilities.Word;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordBankStatsFragment extends Fragment {
    private ListView listview;

    public WordBankStatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wordbank_stats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            listview = activity.findViewById(R.id.wordbank_stats_listview);
        }

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Word> realmResults = realm.where(Word.class).findAll().sort("timeLastPlayed", Sort.DESCENDING);

        List<Word> wordBank = realm.copyFromRealm(realmResults);

        if (wordBank == null) {
            wordBank = new ArrayList<>();
        }

        WordBankStatsAdapter adapter = new WordBankStatsAdapter(Objects.requireNonNull(getContext()), wordBank);
        listview.setAdapter(adapter);
    }
}
