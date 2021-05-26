package nl.aerius.wui.easter.leaderboard;

import nl.aerius.wui.event.SimpleGenericEvent;

public class SubmitTetrisScoreCommand extends SimpleGenericEvent<TetrisScoreSubmission> {
  public SubmitTetrisScoreCommand(final TetrisScoreSubmission value) {
    super(value);
  }
}
