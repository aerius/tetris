package nl.aerius.wui.easter.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

import nl.aerius.wui.easter.leaderboard.TetrisScoreSubmission;

@ImplementedBy(TetrisServiceAsyncImpl.class)
public interface TetrisServiceAsync {
  void submitScore(TetrisScoreSubmission score, AsyncCallback<TetrisScore[]> callback);

  void retrieveLeaderboard(AsyncCallback<TetrisScore[]> callback);
}
