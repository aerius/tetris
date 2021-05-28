package nl.aerius.wui.easter;


import com.google.web.bindery.event.shared.EventBus;

import ol.Map;
import ol.layer.Vector;

import nl.aerius.geo.domain.Point;
import nl.aerius.geo.epsg.ReceptorGridSettings;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisGameControls;
import nl.aerius.wui.easter.game.TetrisGameEngine;
import nl.aerius.wui.easter.game.TetrisGameOverEvent;
import nl.aerius.wui.easter.game.TetrisGameStartEvent;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;
import nl.aerius.wui.util.NotificationUtil;
import nl.aerius.wui.util.SchedulerUtil;

public class TetrisGame {
  private static TetrisGame game;

  private final TetrisVectorLayer layer;
  private final TetrisUtil util;

  private final TetrisGameEngine engine;

  private final Map map;

  public TetrisGame(final Map map, final ReceptorGridSettings grid, final EventBus eventBus) {
    this.map = map;
    layer = new TetrisVectorLayer();
    util = new TetrisUtil(grid);

    final Point origin = new Point(133330, 486354);
    final TetrisArena arena = util.decorateTerrain(layer, origin);
    util.fit(map, arena);

    final TetrisScorer scorer = new TetrisScorer();

    engine = new TetrisGameEngine(grid, scorer, layer);
    engine.scene(arena);
    engine.onGameOver(score -> eventBus.fireEvent(new TetrisGameOverEvent(score)));

    TetrisGameControls.attach(engine);

    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris ronde begint in 3..");
    }, 1000);
    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris ronde begint in 2..");
    }, 2000);
    SchedulerUtil.delay(() -> {
      NotificationUtil.broadcastMessage(eventBus, "Tetris ronde begint in 1..");
    }, 3000);
    SchedulerUtil.delay(() -> {
      eventBus.fireEvent(new TetrisGameStartEvent());
      engine.play();
    }, 4000);
  }

  private Vector getLayer() {
    return layer;
  }

  public static void go(final Map olMap, final ReceptorGridSettings grid, final EventBus eventBus) {
    // If a game already exists, destroy it
    destroy();
    
    game = new TetrisGame(olMap, grid, eventBus);
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
