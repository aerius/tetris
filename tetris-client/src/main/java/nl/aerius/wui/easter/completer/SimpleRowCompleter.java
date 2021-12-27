package nl.aerius.wui.easter.completer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ol.Feature;

import nl.aerius.wui.dev.GWTProd;
import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.aerius.wui.easter.game.HexagonPath;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;
import nl.aerius.wui.util.GWTAtomicInteger;
import nl.aerius.wui.util.SchedulerUtil;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;

public abstract class SimpleRowCompleter implements TetrisRowCompletion {
  protected ReceptorGridSettings grid;
  protected Map<Integer, ReceptorPoint> blocks;
  protected Map<Integer, Feature> blocksFeatures;
  protected TetrisScorer score;
  protected TetrisVectorLayer layer;

  @Override
  public void init(final ReceptorGridSettings grid,
      final TetrisArena arena,
      final Map<Integer, ReceptorPoint> blocks,
      final Map<Integer, Feature> blocksFeatures,
      final TetrisScorer score,
      final TetrisVectorLayer layer) {
    this.grid = grid;
    this.blocks = blocks;
    this.blocksFeatures = blocksFeatures;
    this.score = score;
    this.layer = layer;
  }

  protected int completeRows(final int period, final List<List<Integer>> rowIds) {
    // Determine complete rows
    final List<List<Integer>> completeRows = rowIds.stream()
        .filter(v -> v.stream().allMatch(id -> blocks.containsKey(id)))
        .collect(Collectors.toList());

    if (!completeRows.isEmpty()) {
      score.completeLines(completeRows.size());
    }

    // Get features for each row
    final List<List<Feature>> features = completeRows.stream()
        .map(v -> v.stream()
            .map(id -> blocksFeatures.get(id))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());

    // Remove from layer
    features.forEach(lst -> layer.removeFeaturesFancily(lst, period));

    // Remove from indices
    completeRows.forEach(lst -> blocks.keySet().removeAll(lst));
    completeRows.forEach(lst -> blocksFeatures.keySet().removeAll(lst));

    if (!completeRows.isEmpty()) {
      SchedulerUtil.delay(() -> {
        final GWTAtomicInteger completeCounter = new GWTAtomicInteger(0);
        for (int i = 0; i < rowIds.size(); i++) {
          final List<Integer> row = rowIds.get(i);

          if (completeCounter.get() < completeRows.size()) {
            final List<Integer> firstCompleteRow = completeRows.get(completeCounter.get());
            if (firstCompleteRow.get(0) == row.get(0)) {
              completeCounter.incrementAndGet();
            }
          }

          if (completeCounter.get() == 0) {
            continue;
          }

          final List<ReceptorPoint> moveDown = row.stream()
              .map(v -> blocks.get(v))
              .filter(v -> v != null)
              .collect(Collectors.toList());
          moveDown(moveDown, completeCounter.get());
        }
      }, period - 20);
    }

    return completeRows.size();
  }

  protected void moveDown(final List<ReceptorPoint> lst, final int amount) {
    lst.forEach(v -> {
      final HexagonPath neww = HexagonPath.builder(grid, v, false)
          .down(amount)
          .persist();

      replace(v.getId(), neww);
    });
  }

  protected void replace(final int id, final HexagonPath neww) {
    final Feature oldFeature = blocksFeatures.get(id);
    layer.removeFeature(oldFeature);
    blocks.remove(id);

    final Feature newFeature = neww.features().iterator().next();
    newFeature.setStyle(oldFeature.getStyle());

    final ReceptorPoint newPointer = neww.pointer();
    layer.addFeature(newFeature);
    final ReceptorPoint prev = blocks.put(newPointer.getId(), newPointer);
    if (prev != null) {
      GWTProd.warn("Moved into an existing block!");
    }
    blocksFeatures.put(newPointer.getId(), newFeature);
  }

}
