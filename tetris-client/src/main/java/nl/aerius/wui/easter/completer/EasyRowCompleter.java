package nl.aerius.wui.easter.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ol.Feature;

import nl.aerius.geo.domain.ReceptorPoint;
import nl.aerius.geo.epsg.ReceptorGridSettings;
import nl.aerius.wui.easter.game.HexagonPath;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;

/**
 * Completer that allows row for a completion.
 * 
 * <pre>
 *  o o o o
 * x x x x x
 *  o o o o
 * </pre>
 * 
 * or
 * 
 * <pre>
 * o o o o o
 *  x x x x
 * o o o o o
 * </pre>
 * 
 * with 'x' being a filled block
 * 
 * TODO this fundamentally doesn't work because it will override existing blocks.
 */
public class EasyRowCompleter extends SimpleRowCompleter {
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
          .right(arena.getHexWidth() - 1)
          .view()
          .points().stream()
          .map(v -> v.getId())
          .collect(Collectors.toList()));
      path.clearAndReset();
      lowerRowIds.add(path
          .remember()
          .edit()
          .bottomRight()
          .right(arena.getHexWidth() - 2)
          .view()
          .points().stream()
          .map(v -> v.getId())
          .collect(Collectors.toList()));

      path.clearAndReset()
          .up();
    }
  }

  @Override
  protected void moveDown(final List<ReceptorPoint> lst, final int amount) {
    lst.forEach(rec -> {
      final HexagonPath neww = HexagonPath.builder(grid, rec, false)
          .downWiggle(false, amount)
          .persist();

      replace(rec.getId(), neww);
    });
  }

  @Override
  public int complete(final int period) {
    final int completeUpper = completeRows(period, upperRowIds);
    final int completeLower = completeRows(period, lowerRowIds);
    return completeUpper + completeLower;
  }
}
