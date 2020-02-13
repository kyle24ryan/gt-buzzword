package edu.gatech.seclass.words6300.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.utilities.Word;

class WordBankStatsAdapter extends ArrayAdapter<Word> {
    private Context context;
    private List<Word> wordBank;

    WordBankStatsAdapter(@NonNull Context context, List<Word> wordBank) {
        super(context, 0, wordBank);
        this.context = context;
        this.wordBank = wordBank;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_word, parent, false);

        Word word = wordBank.get(position);

        TextView playedWord = listItem.findViewById(R.id.word);
        playedWord.setText(word.getWord());

        TextView timesPlayed = listItem.findViewById(R.id.word_times_played);
        timesPlayed.setText(String.valueOf(word.getTimesPlayed()));

        return listItem;
    }
}
