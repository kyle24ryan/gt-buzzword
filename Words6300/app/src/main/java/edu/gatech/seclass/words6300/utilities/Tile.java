package edu.gatech.seclass.words6300.utilities;

public class Tile {
  private int points;
  private String letter;
  private boolean boardTile = false;
  private boolean boardTilePlayedAlready = false;

  public String getLetter() {
    return letter;
  }

  public int getPoints() {
    return points;
  }

  public boolean isBoardTile() {
    return boardTile;
  }

  public boolean isBoardTilePlayedAlready() {
    return boardTilePlayedAlready;
  }

  public void setLetter(String letter) {
    this.letter = letter;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void setBoardTile(boolean boardTile) {
    this.boardTile = boardTile;
  }

  public void setBoardTilePlayedAlready(boolean boardTilePlayedAlready) {
    this.boardTilePlayedAlready = boardTilePlayedAlready;
  }
}
