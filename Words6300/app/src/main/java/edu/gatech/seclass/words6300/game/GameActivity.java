package edu.gatech.seclass.words6300.game;

/*
    Class adapted from:
    Name: Julia Kozhukhovskaya
    Project: DragDropTwoRecyclerViews
    Date: May 10th, 2017
    URL: https://github.com/jkozh/DragDropTwoRecyclerViews
*/

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;
        import butterknife.BindView;
        import butterknife.*;
        import edu.gatech.seclass.words6300.R;
        import edu.gatech.seclass.words6300.menu.MainActivity;
        import edu.gatech.seclass.words6300.menu.ManualActivity;
        import edu.gatech.seclass.words6300.menu.MainMenu;
        import edu.gatech.seclass.words6300.settings.SettingsActivity;
        import edu.gatech.seclass.words6300.statistics.StatisticsActivity;
        import edu.gatech.seclass.words6300.utilities.Tile;

public class GameActivity extends AppCompatActivity implements Listener {

    @BindView(R.id.rvBoard)
    RecyclerView rvBoard;
    @BindView(R.id.rvWord)
    RecyclerView rvWord;
    @BindView(R.id.rvRack)
    RecyclerView rvRack;
    @BindView(R.id.tvEmptyBoard)
    TextView tvEmptyBoard;
    @BindView(R.id.tvEmptyWord)
    TextView tvEmptyWord;
    @BindView(R.id.tvEmptyRack)
    TextView tvEmptyRack;
    @BindView(R.id.dropzone)
    View dropZone;

    ListAdapter boardAdapter;
    ListAdapter wordAdapter;
    ListAdapter rackAdapter;

    TextView scoreTV;
    TextView turnsTV;
    TextView playedTV;
    TextView leftTV;
    TextView maxTV;

    Game currentGame;
    HashSet<String> currentGameWordBank;

    List<Tile> word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setHomeButtonEnabled(true);
        ab.setIcon(getDrawable(R.drawable.ic_exit));
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        currentGame = MainMenu.getGameInProgress();

        initBoardRecyclerView();
        initWordRecyclerView();
        initRackRecyclerView();

        tvEmptyBoard.setVisibility(View.GONE);
        tvEmptyWord.setVisibility(View.VISIBLE);
        tvEmptyRack.setVisibility(View.GONE);

        currentGameWordBank = new HashSet<>();

        currentGame.startNewTurn();

        scoreTV = findViewById(R.id.total_score);
        turnsTV = findViewById(R.id.turns_taken);
        maxTV = findViewById(R.id.turns_max);
        playedTV = findViewById(R.id.tiles_played);
        leftTV = findViewById(R.id.tiles_left);

        if (currentGame.isGameInProgress()) {
            scoreTV.setText(getString(R.string.score, currentGame.getGameStatus().getFinalScore()));
            maxTV.setText(getString(R.string.max, currentGame.getSettings().getMaxTurns()));
            turnsTV.setText(getString(R.string.turns, currentGame.getGameStatus().getNumberOfTurns()));
            playedTV.setText(getString(R.string.tiles_played, currentGame.getNumOfTilesPlayed()));
            leftTV.setText(getString(R.string.tiles_left, currentGame.getPool().size()));

            wordAdapter.updateList(currentGame.getWord());
            wordAdapter.notifyDataSetChanged();

            boardAdapter.notifyDataSetChanged();
            rackAdapter.notifyDataSetChanged();
            setEmptyWord(false);
        } else {
            scoreTV.setText(getString(R.string.score, currentGame.getGameStatus().getFinalScore()));
            maxTV.setText(getString(R.string.max, currentGame.getSettings().getMaxTurns()));
            turnsTV.setText(getString(R.string.turns, currentGame.getGameStatus().getNumberOfTurns()));
            playedTV.setText(getString(R.string.tiles_played, currentGame.getNumOfTilesPlayed()));
            leftTV.setText(getString(R.string.tiles_left, currentGame.getPool().size()));
        }
    }

    private void initBoardRecyclerView() {
        rvBoard.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        List<Tile> board = currentGame.getBoard();

        boardAdapter = new ListAdapter(board, this);
        rvBoard.setAdapter(boardAdapter);
        tvEmptyBoard.setOnDragListener(boardAdapter.getDragInstance());
        rvBoard.setOnDragListener(boardAdapter.getDragInstance());
    }

    private void initWordRecyclerView() {
        rvWord.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        if (currentGame.isGameInProgress()) {
            word = currentGame.getWord();
        } else {
            word = new ArrayList<>();
        }

        wordAdapter = new ListAdapter(word, this);
        rvWord.setAdapter(wordAdapter);
        tvEmptyWord.setOnDragListener(wordAdapter.getDragInstance());
        rvWord.setOnDragListener(wordAdapter.getDragInstance());
        dropZone.setOnDragListener(wordAdapter.getDragInstance());
    }

    private void initRackRecyclerView() {
        rvRack.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        List<Tile> rack = currentGame.getRack();

        rackAdapter = new ListAdapter(rack, this);
        rvRack.setAdapter(rackAdapter);
        tvEmptyRack.setOnDragListener(rackAdapter.getDragInstance());
        rvRack.setOnDragListener(rackAdapter.getDragInstance());
    }

    @Override
    public void setEmptyBoard(boolean visibility) {
        tvEmptyBoard.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvBoard.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyWord(boolean visibility) {
        tvEmptyWord.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvWord.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyRack(boolean visibility) {
        tvEmptyRack.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvRack.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    public void onSwapClick(View v) {
        boolean validTiles = true;
        Button swapWord = findViewById(R.id.swap_button);
        for (Tile tile : word) {
            if (tile.isBoardTile()) {
                swapWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                Toast toast = Toast.makeText(this, "Board Tiles Cannot be Swapped!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 325);
                toast.show();
                validTiles = false;
            }
        }
        if (!word.isEmpty() && validTiles) {
            currentGame.swapToPool(word);
            word.clear();
            wordAdapter.notifyDataSetChanged();
            rackAdapter.notifyDataSetChanged();
            setEmptyWord(true);
            setEmptyRack(false);

            checkForEndGame();

            scoreTV.setText(getString(R.string.score, currentGame.getGameStatus().getFinalScore()));
            maxTV.setText(getString(R.string.max, currentGame.getSettings().getMaxTurns()));
            turnsTV.setText(getString(R.string.turns, currentGame.getGameStatus().getNumberOfTurns()));
            playedTV.setText(getString(R.string.tiles_played, currentGame.getNumOfTilesPlayed()));
            leftTV.setText(getString(R.string.tiles_left, currentGame.getPool().size()));
        } else if (word.isEmpty()) {
            swapWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast toast = Toast.makeText(this, "No Tiles to Swap!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 325);
            toast.show();
        }
    }

    private void checkForEndGame() {
        if (currentGame.isGameInProgress()) {
            if (currentGame.getCurrentTurn().getScore() != 0) {
                String text = getString(R.string.played_word_toast, currentGame.getCurrentTurn().getWordPlayed(), currentGame.getCurrentTurn().getScore());
                Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 325);
                toast.show();
            } else {
                String swapped = currentGame.getSwapped();
                String text = getString(R.string.swapped_toast, swapped.length(), swapped);
                Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 325);
                toast.show();
            }
            currentGame.startNewTurn();
        } else {
            //ToDo: Finish Game Over Popup
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_end_game, null);

            TextView endGameScore = dialogView.findViewById(R.id.end_game_score);
            TextView endGameAvg = dialogView.findViewById(R.id.end_game_avg);
            TextView endGameTurns = dialogView.findViewById(R.id.end_game_turns);

            endGameScore.setText(getString(R.string.end_game_score, currentGame.getGameStatus().getFinalScore()));
            endGameTurns.setText(getString(R.string.end_game_turns, currentGame.getGameStatus().getNumberOfTurns()));
            endGameAvg.setText(getString(R.string.end_game_avg, currentGame.getGameStatus().getAvgScorePerTurn()));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("DISMISS", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GameActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public void onPlayWordClick(View v) {
        boolean hasBoardTile = false;
        StringBuilder sb = new StringBuilder();

        for (Tile tile : word) {
            sb.append(tile.getLetter());
            if (tile.isBoardTile()) {
                hasBoardTile = true;
            }
        }
        Button playWord = findViewById(R.id.play_button);
        String playedWord = sb.toString();
        boolean validWord = !currentGameWordBank.contains(playedWord);
        if (word.size() > 1 && hasBoardTile && validWord ) {
            Turn turn = currentGame.getCurrentTurn();
            turn.setWordPlayed(playedWord);
            turn.chooseWord(word);
            currentGameWordBank.add(playedWord);
            word.clear();
            wordAdapter.notifyDataSetChanged();
            rackAdapter.notifyDataSetChanged();
            boardAdapter.notifyDataSetChanged();
            setEmptyWord(true);
            setEmptyRack(false);

            checkForEndGame();

            scoreTV.setText(getString(R.string.score, currentGame.getGameStatus().getFinalScore()));
            maxTV.setText(getString(R.string.max, currentGame.getSettings().getMaxTurns()));
            turnsTV.setText(getString(R.string.turns, currentGame.getGameStatus().getNumberOfTurns()));
            playedTV.setText(getString(R.string.tiles_played, currentGame.getNumOfTilesPlayed()));
            leftTV.setText(getString(R.string.tiles_left, currentGame.getPool().size()));

        } else if (!validWord) {
            playWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast toast = Toast.makeText(this, "Duplicate Word, Try Again!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 325);
            toast.show();
            currentGame.startNewTurn();
            Button reset = findViewById(R.id.reset_button);
            onResetClick(reset);
        } else {
            playWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast toast = Toast.makeText(this, "Word Must Contain a Tile from the Board and Rack!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 325);
            toast.show();
        }
    }

    public void onResetClick(View v) {
        Button resetBtn = findViewById(R.id.reset_button);
        if (!word.isEmpty()) {
            for (Tile tile : word) {
                if (tile.isBoardTile()) {
                    for (Tile boardTile : currentGame.getBoard()) {
                        boardTile.setBoardTile(false);
                        boardTile.setBoardTilePlayedAlready(false);
                    }
                } else {
                    currentGame.addToRack(tile);
                }
            }
            word.clear();
            wordAdapter.notifyDataSetChanged();
            rackAdapter.notifyDataSetChanged();
            setEmptyWord(true);
        } else {
            resetBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            Toast toast = Toast.makeText(this, "No Tiles to Reset!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 325);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentGame.leaveGame(word);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game, menu);
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
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
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
}