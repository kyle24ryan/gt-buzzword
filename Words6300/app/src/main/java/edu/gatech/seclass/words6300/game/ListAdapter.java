package edu.gatech.seclass.words6300.game;

/*
    Class adapted from:
    Name: Julia Kozhukhovskaya
    Project: DragDropTwoRecyclerViews
    Date: May 10th, 2017
    URL: https://github.com/jkozh/DragDropTwoRecyclerViews
*/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.utilities.Tile;


class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>
        implements View.OnTouchListener {

    private List<Tile> list;
    private Listener listener;
    private Context context;
    private int index = -1;


    ListAdapter(List<Tile> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ListViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.text.setText(list.get(position).getLetter());
        holder.number.setText(String.valueOf(list.get(position).getPoints()));
        holder.frameLayout.setTag(position);
        holder.frameLayout.setOnTouchListener(this);
        holder.frameLayout.setOnDragListener(new DragListener(listener));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int deviceWidth, deviceHeight;
        if (list.size() > 4) {
            deviceWidth = displayMetrics.widthPixels / list.size();
        } else {
            deviceWidth = displayMetrics.widthPixels / 4;
        }
        deviceHeight = deviceWidth;
        holder.frameLayout.getLayoutParams().width = deviceWidth;
        holder.frameLayout.getLayoutParams().height = deviceHeight;

        Typeface type = Typeface.createFromAsset(context.getAssets(),  "scramble_mixed.ttf");
        holder.text.setTypeface(type);

        Tile tile = list.get(position);
        if (tile.isBoardTile() && list.equals(Game.word)) {
            index = holder.getAdapterPosition();
        }

        if (index == position && tile.isBoardTile()) {
            holder.frameLayout.setBackground(context.getDrawable(R.drawable.board_tile_border));
            holder.text.setTextColor(context.getColor(R.color.white));
            holder.number.setTextColor(context.getColor(R.color.white));
        } else {
            holder.frameLayout.setBackground(context.getDrawable(R.drawable.tile_border));
            holder.text.setTextColor(context.getColor(R.color.colorPrimaryDark));
            holder.number.setTextColor(context.getColor(R.color.colorPrimaryDark));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");

            RecyclerView viewSource = (RecyclerView) v.getParent();
            int viewId = viewSource.getId();
            int sourcePosition = (int) v.getTag();
            ListAdapter sourceAdapter = (ListAdapter) viewSource.getAdapter();
            Tile sourceTile = sourceAdapter.getList().get(sourcePosition);
            final int rvBoard = R.id.rvBoard;
            View.DragShadowBuilder shadowBuilder;

            if (viewId == rvBoard && sourceTile.isBoardTilePlayedAlready()) {
                shadowBuilder = new View.DragShadowBuilder(v);
                Drawable invalid = v.getContext().getDrawable(R.drawable.invalid_tile);
                shadowBuilder.getView().setBackground(invalid);
            } else {
                shadowBuilder = new View.DragShadowBuilder(v);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            } else {
                v.startDrag(data, shadowBuilder, v, 0);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            RecyclerView viewSource = (RecyclerView) v.getParent();
            int viewId = viewSource.getId();
            int sourcePosition = (int) v.getTag();
            ListAdapter sourceAdpater = (ListAdapter) viewSource.getAdapter();
            Tile sourceTile = sourceAdpater.getList().get(sourcePosition);
            final int rvBoard = R.id.rvBoard;

            if (viewId == rvBoard && sourceTile.isBoardTilePlayedAlready()) {
                Drawable normal = v.getContext().getDrawable(R.drawable.tile_border);
                v.setBackground(normal);
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
            }
            return true;
        }
        return false;
    }

    List<Tile> getList() {
        return list;
    }

    void updateList(List<Tile> list) {
        this.list = list;
    }

    DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        AppCompatTextView text;
        @BindView(R.id.number)
        AppCompatTextView number;
        @BindView(R.id.frame_layout_item)
        FrameLayout frameLayout;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}