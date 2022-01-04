package nl.aerius.wui.easter.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gwt.user.client.Timer;

import ol.Feature;

import nl.aerius.wui.easter.completer.HardRowCompleter;
import nl.aerius.wui.easter.completer.TetrisRowCompletion;
import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;

public class TetrisGameEngine {
  private final ReceptorGridSettings grid;
  private final TetrisVectorLayer layer;

  private final TetrisScorer scorer;

  private final TetrisRowCompletion completer = new HardRowCompleter();

  private final Map<Integer, ReceptorPoint> blocks = new HashMap<>();
  private final Map<Integer, Feature> blocksFeatures = new HashMap<>();

  private ReceptorPoint tetrominoOrigin;
  private int tetrominoRotation;

  private ReceptorPoint nextTetrominoLocation;
  private Function<HexagonPath, HexagonPath> nextTetromino;
  private List<Feature> nextTetrominoFeatures;

  private ReceptorPoint tetrominoLocation;
  private Function<HexagonPath, HexagonPath> tetromino;
  private List<ReceptorPoint> tetrominoPoints;
  private Map<Integer, Feature> tetrominoFeatures;
  private boolean bottom;

  private boolean skipNextCycle;

  /**
   * TODO Reduce period based on level.
   */
  private final int period = 300;
  private final Timer loop = new Timer() {
    @Override
    public void run() {
      if (skipNextCycle) {
        skipNextCycle = false;
        return;
      }

      performGameCycle();
    }
  };
  private Consumer<TetrisScorer> onGameOver;

  public TetrisGameEngine(final ReceptorGridSettings grid, final TetrisScorer scorer, final TetrisVectorLayer layer) {
    this.grid = grid;
    this.scorer = scorer;
    this.layer = layer;
  }

  public void scene(final TetrisArena arena) {
    blocks.putAll(HexagonPath.builder(grid)
        .view()
        .move(arena.getOrigin())
        .topLeft()
        .edit()
        .down(arena.getHeight())
        .rightFilled(arena.getHexWidth(), false)
        .up(arena.getHeight())
        .points()
        .stream()
        .collect(Collectors.toMap(v -> v.getId(), v -> v)));

    completer.init(grid, arena, blocks, blocksFeatures, scorer, layer);

    nextTetrominoLocation = HexagonPath.builder(grid, arena.getOrigin())
        .view()
        .topLeft()
        .rightHop(arena.getHexWidth())
        .right(2)
        .down(5)
        .pointer();

    tetrominoOrigin = HexagonPath.builder(grid, false)
        .point(arena.getOrigin())
        .right(arena.getHexWidth() / 2)
        .down()
        .pointer();
  }

  public void play() {
    stageInitial();
    loop.scheduleRepeating(period);
  }

  public boolean isPlaying() {
    return loop.isRunning();
  }

  private void performGameCycle() {
    // Move tetromino pointer location down by one
    tetrominoLocation = HexagonPath.builder(grid, tetrominoLocation)
        .down()
        .pointer();

    // Redraw, and if collision settle the block
    if (!redraw()) {
      settle();
      completeRows();

      reset();
      stageInitial();
    }
  }

  private void completeRows() {
    if (completer.complete(period) > 0) {
      skipNextCycle = true;
    }
  }

  /**
   * Redraw the tetromino, and return false|true if the redraw was successful.
   */
  private boolean redraw() {
    final HexagonPath path = determinePath(tetromino, tetrominoLocation, tetrominoRotation);

    // If collision, don't draw
    if (checkCollision(path)) {
      return false;
    } else {
      draw(path);
      return true;
    }
  }

  private void settle() {
    tetrominoFeatures.values().forEach(feat -> feat.getStyle().setStroke(null));
    blocks.putAll(tetrominoPoints.stream()
        .collect(Collectors.toMap(v -> v.getId(), v -> v)));
    blocksFeatures.putAll(tetrominoFeatures);
  }

  private void reset() {
    tetrominoFeatures.clear();
    tetromino = null;
  }

  private void stageInitial() {
    if (nextTetromino == null) {
      nextTetromino = getNextTetromino();
    }

    tetromino = nextTetromino;
    tetrominoRotation = 0;
    tetrominoLocation = tetrominoOrigin;
    bottom = false;

    nextTetromino = getNextTetromino();
    final HexagonPath next = determinePath(nextTetromino, nextTetrominoLocation, 0);
    drawPreview(next);

    final HexagonPath initial = determinePath(tetromino, tetrominoLocation, tetrominoRotation);
    if (checkCollision(initial)) {
      // Game over
      draw(initial);
      gameOver();
    } else {
      draw(initial);
    }
  }

  private void drawPreview(final HexagonPath next) {
    if (nextTetrominoFeatures != null) {
      layer.removeFeatures(nextTetrominoFeatures);
    }

    final List<Feature> features = next.features();
    layer.addFeatures(features);
    nextTetrominoFeatures = features;
  }

  private void gameOver() {
    loop.cancel();
    onGameOver.accept(scorer);
  }

  private boolean checkCollision(final HexagonPath path) {
    return path.points().stream()
        .anyMatch(v -> blocks.containsKey(v.getId()));
  }

  private void draw(final HexagonPath next) {
    if (tetrominoFeatures != null) {
      layer.removeFeatures(tetrominoFeatures.values());
    }

    final Map<Integer, Feature> features = next.featuresMap();
    layer.addFeatures(features.values());
    tetrominoPoints = next.points();
    tetrominoFeatures = features;
  }

  private HexagonPath determinePath(final Function<HexagonPath, HexagonPath> tetromino, final ReceptorPoint origin, final int rotation) {
    return HexagonPath.builder(grid, origin)
        .rotate(rotation)
        .sequence(tetromino);
  }

  private Function<HexagonPath, HexagonPath> getNextTetromino() {
    final TetrisShape shape = TetrisShapes.random();

    return TetrisStyles.random()
        .andThen(path -> path
            .sequence(shape));
  }

  public void moveLeft() {
    tryMove(path -> flipBottom(path, v -> v.topLeft(), v -> v.bottomLeft()));
  }

  public void moveRight() {
    tryMove(path -> flipBottom(path, v -> v.topRight(), v -> v.bottomRight()));
  }

  public void moveDown() {
    tryMove(path -> path.down());
  }

  private void tryMove(final Function<HexagonPath, HexagonPath> func) {
    final ReceptorPoint oldLocation = tetrominoLocation;
    tetrominoLocation = HexagonPath.builder(grid, tetrominoLocation)
        .sequence(func)
        .pointer();
    if (!redraw()) {
      tetrominoLocation = oldLocation;
    }
  }

  public void rotateClockwise() {
    tetrominoRotation++;
    if (!redraw()) {
      tetrominoRotation--;
      redraw();
    }
  }

  public void roateCounterClockwise() {
    tetrominoRotation--;
    if (!redraw()) {
      tetrominoRotation++;
      redraw();
    }
  }

  private HexagonPath flipBottom(final HexagonPath path,
      final Function<HexagonPath, HexagonPath> variant1,
      final Function<HexagonPath, HexagonPath> variant2) {
    final HexagonPath ret = bottom ? variant1.apply(path) : variant2.apply(path);
    bottom = !bottom;
    return ret;
  }

  public void onGameOver(final Consumer<TetrisScorer> onGameOver) {
    this.onGameOver = onGameOver;
  }

  public void drop() {
    // TODO: Support tetromino drop
  }

  public void destroy() {
    loop.cancel();
  }
}
