package nl.aerius.wui.easter.leaderboard;

import java.util.ArrayList;
import java.util.List;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

import elemental2.core.JsArray;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;

import nl.aerius.wui.easter.game.TetrisGameOverEvent;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.service.TetrisScore;
import nl.aerius.wui.util.SchedulerUtil;

@Component
public class TetrisLeaderboardComponent implements IsVueComponent, HasCreated {
  private static final TetrisLeaderboardComponentEventBinder EVENT_BINDER = GWT.create(TetrisLeaderboardComponentEventBinder.class);

  private static final String[] PRIMARY_COLORS = new String[] { "red", "green", "blue" };

  interface TetrisLeaderboardComponentEventBinder extends EventBinder<TetrisLeaderboardComponent> {}

  @Data int colorCounter;

  @Prop EventBus eventBus;

  @Data boolean leaderboardShowing = false;
  @Data @JsProperty List<TetrisScore> leaderboard = new ArrayList<>();

  @Ref HTMLDivElement board;

  @Data TetrisScorer scorer = null;
  @Data int firstEligibleScoreIndex;

  @Data String name;
  @Data String nameFallback;

  @Ref JsArray<HTMLElement> nameElement;
  @Ref HTMLElement nameFallbackElement;

  private boolean celebrationScheduled;

  private void changeCelebrationColors() {
    if (board == null) {
      return;
    }

    colorCounter++;
    board.style.borderColor = getColor();
    board.style.boxShadow = "0px 0px 50px " + getColor();

    if (nameElement != null) {
      nameElement.forEach((a, b, c) -> a.style.borderColor = getColor());
    }

    if (nameFallbackElement != null) {
      nameFallbackElement.style.borderColor = getColor();
    }

    delayCelebrate();
  }

  @JsMethod
  public void close() {
    leaderboardShowing = false;
  }

  @JsMethod
  public boolean hasEligibleScore() {
    if (scorer != null
        && leaderboard.get(leaderboard.size() - 1).score < scorer.getScore()) {
      return true;
    } else {
      return false;
    }
  }

  @JsMethod
  public boolean isEligibleScore(final TetrisScore score, final int index) {
    if (scorer == null) {
      return false;
    }

    if (index > firstEligibleScoreIndex) {
      return false;
    }

    if (scorer.getScore() > score.score) {
      firstEligibleScoreIndex = Math.min(firstEligibleScoreIndex, index);
      return true;
    }

    return false;
  }

  @JsMethod
  public boolean isEligibleScoreFallback() {
    return firstEligibleScoreIndex > 10
        && scorer != null && leaderboard.size() < 10;
  }

  private String getColor() {
    return PRIMARY_COLORS[colorCounter % PRIMARY_COLORS.length];
  }

  @Watch(value = "leaderboardShowing", isImmediate = true)
  public void onLeaderboardShowingChange(final boolean neww) {
    if (neww) {
      delayCelebrate();
    }
  }

  @EventHandler
  public void onTetrisGameOverEvent(final TetrisGameOverEvent e) {
    scorer = e.getValue();
    firstEligibleScoreIndex = Integer.MAX_VALUE;
  }

  @EventHandler
  public void onTetrisLeaderboardDisplayEvent(final TetrisLeaderboardDisplayEvent e) {
    leaderboardShowing = true;
    leaderboard = e.getValue();
    changeCelebrationColors();
  }

  @JsMethod
  public void submit(final String name) {
    final TetrisScoreSubmission submission = new TetrisScoreSubmission(name, scorer.getLines());

    eventBus.fireEvent(new SubmitTetrisScoreCommand(submission));
    scorer = null;
    firstEligibleScoreIndex = Integer.MAX_VALUE;
  }

  @JsMethod
  public int getIndexMod(final int idx) {
    return idx < firstEligibleScoreIndex ? 0 : 1;
  }

  private void delayCelebrate() {
    if (celebrationScheduled) {
      return;
    }

    if (!leaderboardShowing) {
      return;
    }

    celebrationScheduled = true;
    SchedulerUtil.delay(() -> {
      celebrationScheduled = false;
      changeCelebrationColors();
    }, 500);
  }

  @Override
  public void created() {
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
