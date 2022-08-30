package nl.aerius.wui.easter;

import com.google.web.bindery.event.shared.EventBus;

import ol.Map;
import ol.layer.Vector;

import nl.aerius.shared.domain.geojson.Point;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisGameControls;
import nl.aerius.wui.easter.game.TetrisGameEngine;
import nl.aerius.wui.easter.game.TetrisGameOverEvent;
import nl.aerius.wui.easter.game.TetrisGameStartEvent;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;
import nl.aerius.wui.util.NotificationUtil;
import nl.aerius.wui.util.SchedulerUtil;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;

public class TetrisGame {
  private static TetrisGame game;

  private final TetrisVectorLayer layer;
  private final TetrisUtil util;

  private final TetrisGameEngine engine;

  private final Map map;

  public TetrisGame(final Map map, final ReceptorGridSettings grid, final EventBus eventBus, final Point origin) {
    this.map = map;
    layer = new TetrisVectorLayer();
    util = new TetrisUtil(grid);

    final TetrisArena arena = util.decorateTerrain(layer, origin);
    util.fit(map, arena);

    final TetrisScorer scorer = new TetrisScorer();

    engine = new TetrisGameEngine(grid, scorer, layer);
    engine.scene(arena);
    engine.onGameOver(score -> eventBus.fireEvent(new TetrisGameOverEvent(score)));

    TetrisGameControls.attach(engine);

    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris in 3..");
    }, 1000);
    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris in 2..");
    }, 2000);
    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris in 1..");
    }, 3000);
    SchedulerUtil.delay(() -> {
      eventBus.fireEvent(new TetrisGameStartEvent());
      engine.play();
    }, 4000);
  }

  private Vector getLayer() {
    return layer;
  }
  
  public static boolean isPlaying() {
    return game != null;
  }

  public static void go(final Map olMap, final ReceptorGridSettings grid, final EventBus eventBus, final Point origin) {
    // If a game already exists, destroy it
    destroy();

    game = new TetrisGame(olMap, grid, eventBus, origin);
    olMap.addLayer(game.getLayer());
  }

  public static void destroy() {
    if (game != null) {
      game.internalDestroy();
      game = null;
    }
  }

  private void internalDestroy() {
    map.removeLayer(layer);
    engine.destroy();
  }
}
