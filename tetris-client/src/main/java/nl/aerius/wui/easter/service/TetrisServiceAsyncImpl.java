package nl.aerius.wui.easter.service;

import java.util.Date;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.gwt.user.client.rpc.AsyncCallback;

import elemental2.dom.FormData;

import nl.aerius.wui.easter.TetrisConfiguration;
import nl.aerius.wui.easter.leaderboard.TetrisScoreSubmission;
import nl.aerius.wui.util.InteropRequestUtil;

public class TetrisServiceAsyncImpl implements TetrisServiceAsync {
  private static final String LEADERBOARD = "leaderboard";
  private static final String SUBMIT = "submit";

  @Inject TetrisConfiguration cfg;

  @Override
  public void retrieveLeaderboard(final AsyncCallback<TetrisScore[]> callback) {
    final String url = InteropRequestUtil.prepareUrl(cfg.getTetrisLeaderboardEndpoint(), LEADERBOARD);

    InteropRequestUtil.doGet(url, callback);
  }

  @Override
  public void submitScore(final TetrisScoreSubmission score, final AsyncCallback<TetrisScore[]> callback) {
    final String url = InteropRequestUtil.prepareUrl(cfg.getTetrisLeaderboardEndpoint(), SUBMIT);

    final FormData data = new FormData();
    data.append("name", score.getName());
    data.append("date", String.valueOf(new Date().getTime()));
    data.append("clientScore", String.valueOf(score.getScoreNumber()));
    data.append("score", score.getScore().stream()
        .map(v -> String.valueOf(v))
        .collect(Collectors.joining(",")));
    data.append("token", "abcd");

    InteropRequestUtil.doPost(url, data, callback);
  }
}
