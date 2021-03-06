package nl.aerius.wui.easter.leaderboard;

import java.util.List;

public class TetrisScoreSubmission {
  private final String name;
  private final List<Integer> score;
  private final int scoreNumber;

  public TetrisScoreSubmission(final String name, final int scoreNumber, final List<Integer> score) {
    this.name = name;
    this.scoreNumber = scoreNumber;
    this.score = score;
  }

  public String getName() {
    return name;
  }
  
  public int getScoreNumber() {
    return scoreNumber;
  }

  public List<Integer> getScore() {
    return score;
  }
}
