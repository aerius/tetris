package nl.aerius.wui.easter.leaderboard;

import java.util.List;

public class TetrisScoreSubmission {
  private final String name;
  private final List<Integer> score;

  public TetrisScoreSubmission(final String name, final List<Integer> score) {
    this.name = name;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public List<Integer> getScore() {
    return score;
  }
}
