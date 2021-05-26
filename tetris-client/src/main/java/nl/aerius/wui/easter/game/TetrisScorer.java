package nl.aerius.wui.easter.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import jsinterop.annotations.JsProperty;

public class TetrisScorer {
  private final int[] TETRIS_SCORES = new int[] { 40, 100, 300, 1200 };

  private int score;
  private int lineClears;

  private final @JsProperty List<Integer> lines = new ArrayList<>();

  public void completeLines(final int size) {
    lines.add(size);
    lineClears += size;

    IntStream.range(0, size)
        .forEach(v -> increment(size));
  }

  private void increment(final int size) {
    score += TETRIS_SCORES[size - 1] * getLevel();
  }

  public int getLevel() {
    return getLineClears() / 10 + 1;
  }

  public int getScore() {
    return score;
  }

  public int getLineClears() {
    return lineClears;
  }

  public List<Integer> getLines() {
    return lines;
  }
}
