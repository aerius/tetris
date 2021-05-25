package nl.aerius.repository;

import java.util.List;

import nl.aerius.domain.TetrisScore;

public interface TetrisRepository {

  List<TetrisScore> getLeaderboard();

  void submitScore(TetrisScore score);

}
