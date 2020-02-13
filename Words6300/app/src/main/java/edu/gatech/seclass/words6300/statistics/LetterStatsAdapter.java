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
import edu.gatech.seclass.words6300.utilities.Letter;

public class LetterStatsAdapter extends ArrayAdapter<Letter> {
  private Context context;
  private List<Letter> letters;

  LetterStatsAdapter(@NonNull Context context, List<Letter> letters) {
    super(context, 0, letters);
    this.context = context;
    this.letters = letters;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItem = convertView;

    if (listItem == null)
      listItem = LayoutInflater.from(context).inflate(R.layout.item_letter, parent, false);

    Letter letter = letters.get(position);

    TextView letterChar = listItem.findViewById(R.id.letter_char);
    letterChar.setText(letter.getLetter());

    TextView timesPlayed = listItem.findViewById(R.id.times_played);
    timesPlayed.setText(String.valueOf(letter.getTimesInWord()));

    TextView timesTradedBack = listItem.findViewById(R.id.traded_back);
    timesTradedBack.setText(String.valueOf(letter.getTimesTradedBack()));

    TextView usagePercent = listItem.findViewById(R.id.usage_percent);
    usagePercent.setText(context.getString(R.string.usage_percent, letter.getPercentageInWord()));

    return listItem;
  }
}
