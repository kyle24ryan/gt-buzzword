package edu.gatech.seclass.words6300.game;

/*
    Class adapted from:
    Name: Julia Kozhukhovskaya
    Project: DragDropTwoRecyclerViews
    Date: May 10th, 2017
    URL: https://github.com/jkozh/DragDropTwoRecyclerViews
*/

interface Listener {
    void setEmptyBoard(boolean visibility);
    void setEmptyWord(boolean visibility);
    void setEmptyRack(boolean visibility);
}
