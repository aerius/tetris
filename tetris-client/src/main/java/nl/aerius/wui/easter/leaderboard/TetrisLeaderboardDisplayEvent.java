package nl.aerius.wui.easter.leaderboard;

import java.util.List;

import nl.aerius.wui.easter.service.TetrisScore;
import nl.aerius.wui.event.SimpleGenericEvent;

public class TetrisLeaderboardDisplayEvent extends SimpleGenericEvent<List<TetrisScore>> {
  public TetrisLeaderboardDisplayEvent(final List<TetrisScore> value) {
    super(value);
  }
}
