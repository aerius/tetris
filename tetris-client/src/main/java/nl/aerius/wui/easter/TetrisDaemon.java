package nl.aerius.wui.easter;

import java.util.Arrays;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.aerius.wui.dev.GWTProd;
import nl.aerius.wui.easter.game.TetrisGameOverEvent;
import nl.aerius.wui.easter.leaderboard.SubmitTetrisScoreCommand;
import nl.aerius.wui.easter.leaderboard.TetrisLeaderboardDisplayEvent;
import nl.aerius.wui.easter.service.TetrisServiceAsync;
import nl.aerius.wui.event.BasicEventComponent;
import nl.aerius.wui.future.AppAsyncCallback;

public class TetrisDaemon extends BasicEventComponent {
  private static final TetrisDaemonEventBinder EVENT_BINDER = GWT.create(TetrisDaemonEventBinder.class);

  interface TetrisDaemonEventBinder extends EventBinder<TetrisDaemon> {}

  @Inject private TetrisServiceAsync service;

  @EventHandler
  public void onTetrisGameOverEvent(final TetrisGameOverEvent e) {
    service.retrieveLeaderboard(AppAsyncCallback.create(leaderboard -> {
      eventBus.fireEvent(new TetrisLeaderboardDisplayEvent(Arrays.asList(leaderboard)));
    }, ex -> {
      GWTProd.warn("Tetris leaderboard API unreachable.");
    }));
  }

  @EventHandler
  public void onSubmitTetrisScoreCommand(final SubmitTetrisScoreCommand c) {
    service.submitScore(c.getValue(), AppAsyncCallback.create(leaderboard -> {
      eventBus.fireEvent(new TetrisLeaderboardDisplayEvent(Arrays.asList(leaderboard)));
    }));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
