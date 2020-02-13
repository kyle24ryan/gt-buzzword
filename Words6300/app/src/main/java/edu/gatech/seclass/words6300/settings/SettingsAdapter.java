package edu.gatech.seclass.words6300.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import edu.gatech.seclass.words6300.R;

public class SettingsAdapter extends ArrayAdapter<LetterSetting> {
  private Context context;
  private List<LetterSetting> letterSettings;

  public SettingsAdapter(@NonNull Context context, List<LetterSetting> settings) {
    super(context, 0, settings);
    this.context = context;
    letterSettings = settings;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItem = convertView;

    if (listItem == null)
      listItem = LayoutInflater.from(context).inflate(R.layout.item_setting, parent, false);

    LetterSetting letterSetting = letterSettings.get(position);

    TextView letter = listItem.findViewById(R.id.letter);
    letter.setText(String.valueOf(letterSetting.getLetter()));

    TextView distribution = listItem.findViewById(R.id.distribution);
    distribution.setText(String.format("x%s", String.valueOf(letterSetting.getDistribution())));

    TextView value = listItem.findViewById(R.id.value);
    value.setText(String.valueOf(letterSetting.getValue()));

    Typeface type = Typeface.createFromAsset(context.getAssets(),  "scramble_mixed.ttf");
    letter.setTypeface(type);
    return listItem;
  }
}
