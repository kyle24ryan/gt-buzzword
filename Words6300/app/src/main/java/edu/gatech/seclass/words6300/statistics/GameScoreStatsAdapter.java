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
import edu.gatech.seclass.words6300.game.GameDetail;

public class GameScoreStatsAdapter extends ArrayAdapter<GameDetail> {
  private Context context;
  private List<GameDetail> gameDetails;

  GameScoreStatsAdapter(@NonNull Context context, List<GameDetail> details) {
    super(context, 0, details);
    this.context = context;
    gameDetails = details;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItem = convertView;

    if (listItem == null)
      listItem = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);

    GameDetail gameDetail = gameDetails.get(position);

    TextView finalScore = listItem.findViewById(R.id.final_score);
    finalScore.setText(String.valueOf(gameDetail.getFinalScore()));

    TextView numOfTurns = listItem.findViewById(R.id.turns);
    numOfTurns.setText(String.valueOf(gameDetail.getNumberOfTurns()));

    TextView avgScore = listItem.findViewById(R.id.avg_score);
    avgScore.setText(context.getString(R.string.avg_score, gameDetail.getAvgScorePerTurn()));

    return listItem;
  }
}
