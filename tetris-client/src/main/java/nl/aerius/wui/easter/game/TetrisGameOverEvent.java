package nl.aerius.wui.easter.game;

import nl.aerius.wui.event.SimpleGenericEvent;

public class TetrisGameOverEvent extends SimpleGenericEvent<TetrisScorer> {
  public TetrisGameOverEvent(final TetrisScorer value) {
    super(value);
  }
}
