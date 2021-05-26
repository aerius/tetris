package nl.aerius.wui.easter.completer;

import java.util.Map;

import com.google.inject.ImplementedBy;

import ol.Feature;

import nl.aerius.geo.domain.ReceptorPoint;
import nl.aerius.geo.epsg.ReceptorGridSettings;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisScorer;
import nl.aerius.wui.easter.game.TetrisVectorLayer;

@ImplementedBy(EasyRowCompleter.class)
public interface TetrisRowCompletion {
  void init(ReceptorGridSettings grid,
      TetrisArena arena,
      Map<Integer, ReceptorPoint> blocks,
      Map<Integer, Feature> blocksFeatures,
      TetrisScorer score,
      TetrisVectorLayer layer);

  int complete(int period);

}
