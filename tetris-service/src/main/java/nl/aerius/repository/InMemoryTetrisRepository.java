package nl.aerius.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import nl.aerius.domain.TetrisScore;
import nl.aerius.util.TetrisUtil;

@Component
@Profile("dev")
@Primary
public class InMemoryTetrisRepository implements TetrisRepository {
  private final List<TetrisScore> leaderboard = new ArrayList<>();

  public InMemoryTetrisRepository() {
    // Fill with some dummies..
    final TetrisScore score1 = new TetrisScore();
    score1.setScore(TetrisUtil.calculateScore(new int[] { 2, 4, 2, 2 }));
    score1.setName("Stikstofje");
    score1.setDate(new Date(2020 - 1900, 4, 20));

    final TetrisScore score2 = new TetrisScore();
    score2.setScore(TetrisUtil.calculateScore(new int[] { 4, 4 }));
    score2.setName("Kunstmestje");
    score2.setDate(new Date(2020 - 1900, 6, 9));

    submitScore(score1);
    submitScore(score2);
  }

  @Override
  public List<TetrisScore> getLeaderboard() {
    return leaderboard.stream()
        .sorted((o1, o2) -> -Integer.compare(o1.getScore(), o2.getScore()))
        .limit(10)
        .collect(Collectors.toList());
  }

  @Override
  public void submitScore(final TetrisScore score) {
    leaderboard.add(score);
  }
}
