package edu.gatech.seclass.words6300.game;

/*
Class adapted from:
Name: Julia Kozhukhovskaya
Project: DragDropTwoRecyclerViews
Date: May 10th, 2017
URL: https://github.com/jkozh/DragDropTwoRecyclerViews
*/

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;

import java.util.List;
import edu.gatech.seclass.words6300.R;
import edu.gatech.seclass.words6300.utilities.Tile;

public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener listener;

    DragListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            isDropped = true;
            int positionTarget = -1;

            View viewSource = (View) event.getLocalState();
            int viewId = v.getId();
            final int flItem = R.id.frame_layout_item;
            final int tvEmptyBoard = R.id.tvEmptyBoard;
            final int tvEmptyWord = R.id.tvEmptyWord;
            final int tvEmptyRack = R.id.tvEmptyRack;
            final int rvBoard = R.id.rvBoard;
            final int rvWord = R.id.rvWord;
            final int rvRack = R.id.rvRack;
            final int dropZone = R.id.dropzone;

            switch (viewId) {
                case flItem:
                case tvEmptyBoard:
                case tvEmptyWord:
                case tvEmptyRack:
                case rvBoard:
                case rvWord:
                case rvRack:
                case dropZone:

                    RecyclerView target;

                    switch (viewId) {
                        case tvEmptyBoard:
                        case rvBoard:
                            target = (RecyclerView) v.getRootView().findViewById(rvBoard);
                            break;
                        case dropZone:
                        case tvEmptyWord:
                        case rvWord:
                            target = (RecyclerView) v.getRootView().findViewById(rvWord);
                            break;
                        case tvEmptyRack:
                        case rvRack:
                            target = (RecyclerView) v.getRootView().findViewById(rvRack);
                            break;
                        default:
                            target = (RecyclerView) v.getParent();
                            positionTarget = (int) v.getTag();
                    }

                    if (viewSource != null) {
                        RecyclerView source = (RecyclerView) viewSource.getParent();
                        ListAdapter sourceAdapter = (ListAdapter) source.getAdapter();
                        int sourcePosition = (int) viewSource.getTag();
                        int sourceId = source.getId();
                        int targetId = target.getId();

                        Tile sourceTile = sourceAdapter.getList().get(sourcePosition);
                        List<Tile> sourceList = sourceAdapter.getList();

                        ListAdapter targetAdapter = (ListAdapter) target.getAdapter();
                        List<Tile> targetList = targetAdapter.getList();

                        // Allows board tile to be put back
                        if (sourceId != rvBoard && targetId == rvBoard && sourceTile.isBoardTile()) {
                            sourceList.remove(sourcePosition);
                            sourceAdapter.updateList(sourceList);
                            sourceAdapter.notifyDataSetChanged();
                            for (Tile tile : targetList) {
                                tile.setBoardTilePlayedAlready(false);
                            }
                            if (sourceList.size() == 0) {
                                listener.setEmptyWord(true);
                            }
                            break;
                        }

                        // Prevents board tiles from being moved to rack
                        if ((sourceId == rvBoard || sourceTile.isBoardTile()) && targetId == rvRack) {
                            break;
                        }

                        // Prevents removing tiles from board
                        if (sourceId != rvBoard) {
                            sourceList.remove(sourcePosition);
                            sourceAdapter.updateList(sourceList);
                            sourceAdapter.notifyDataSetChanged();
                        } else if (sourceTile.isBoardTilePlayedAlready()) {
                            break;
                        }

                        // Prevents rearranging tiles in the board
                        if (sourceId == rvBoard && targetId == rvBoard) {
                            break;
                        }

                        // Prevents adding tiles to the board
                        if (targetList.size() == 4 && targetId == rvBoard) {
                            sourceList.add(sourcePosition, sourceTile);
                            sourceAdapter.updateList(sourceList);
                            sourceAdapter.notifyDataSetChanged();
                            break;
                        }

                        // Adds tiles to target
                        if (positionTarget >= 0) {
                            targetList.add(positionTarget, sourceTile);
                        } else {
                            targetList.add(sourceTile);
                        }

                        // Marks played tile and all tiles that a boardTile has been played
                        if (sourceId == rvBoard) {
                            sourceTile.setBoardTile(true);
                            for (Tile tile : sourceList) {
                                tile.setBoardTilePlayedAlready(true);
                            }
                        }

                        targetAdapter.updateList(targetList);
                        targetAdapter.notifyDataSetChanged();

                        if (sourceId == rvRack && sourceAdapter.getItemCount() < 1) {
                            listener.setEmptyRack(true);
                        }
                        if (viewId == tvEmptyRack) {
                            listener.setEmptyRack(false);
                        }
                        if (sourceId == rvWord && sourceAdapter.getItemCount() < 1) {
                            listener.setEmptyWord(true);
                        }
                        if (viewId == tvEmptyWord) {
                            listener.setEmptyWord(false);
                        }
                        if (sourceId == rvBoard && sourceAdapter.getItemCount() < 1) {
                            listener.setEmptyBoard(true);
                        }
                        if (viewId == tvEmptyBoard) {
                            listener.setEmptyBoard(false);
                        }
                    }
                    break;
            }
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }
}