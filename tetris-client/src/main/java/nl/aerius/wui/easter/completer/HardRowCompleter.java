package nl.aerius.wui.easter.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ol.Feature;

import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.aerius.wui.easter.game.HexagonPath;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;

/**
 * Completer that requires a filled row for a completion.
 * 
 * <pre>
 * o o o o o
 *  x x x x
 * x x x x x
 *  o o o o
 * </pre>
 * 
 * or
 * 
 * <pre>
 *  o o o o
 * x x x x x
 *  x x x x
 * o o o o o
 * </pre>
 * 
 */
public class HardRowCompleter extends SimpleRowCompleter {
  private final ArrayList<List<Integer>> upperRowIds = new ArrayList<>();
  private final ArrayList<List<Integer>> lowerRowIds = new ArrayList<>();

  @Override
  public void init(final ReceptorGridSettings grid,
      final TetrisArena arena,
      final Map<Integer, ReceptorPoint> blocks,
      final Map<Integer, Feature> blocksFeatures,
      final TetrisScorer score,
      final TetrisVectorLayer layer) {
    super.init(grid, arena, blocks, blocksFeatures, score, layer);

    final HexagonPath path = HexagonPath.builder(grid, false)
        .point(arena.getOrigin())
        .down(arena.getHexHeight());
    for (int i = 0; i < arena.getHexHeight(); i++) {
      upperRowIds.add(path
          .remember()
          .edit()
          .rightFilled(arena.getHexWidth() - 1, false)
          .view()
          .points().stream()
          .map(v -> v.getId())
          .collect(Collectors.toList()));
      path.clearAndReset();
      lowerRowIds.add(path
          .remember()
          .edit()
          .rightFilled(arena.getHexWidth() - 1, true)
          .view()
          .points().stream()
          .map(v -> v.getId())
          .collect(Collectors.toList()));

      path.clearAndReset()
          .up();
    }
  }

  @Override
  public int complete(final int period) {
    final int upperComplete = completeRows(period, upperRowIds);
    final int lowerComplete = completeRows(period, lowerRowIds);
    return upperComplete + lowerComplete;
  }
}
