package nl.aerius.wui.easter;



import ol.Extent;
import ol.Feature;
import ol.Map;
import ol.OLFactory;
import ol.format.Wkt;
import ol.format.WktReadOptions;

import nl.aerius.geo.domain.InformationZoomLevel;
import nl.aerius.geo.domain.Point;
import nl.aerius.geo.domain.ReceptorPoint;
import nl.aerius.geo.epsg.ReceptorGridSettings;
import nl.aerius.geo.util.HexagonUtil;
import nl.aerius.geo.util.ReceptorUtil;
import nl.aerius.wui.easter.game.HexagonPath;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisShapes;
import nl.aerius.wui.easter.game.TetrisVectorLayer;

/**
 * Boilerplate and bloated code
 */
public class TetrisUtil {
  private final ReceptorGridSettings grid;
  private final ReceptorUtil recUtil;

  public TetrisUtil(final ReceptorGridSettings grid) {
    this.grid = grid;
    recUtil = new ReceptorUtil(grid);
  }

  public TetrisArena decorateTerrain(final TetrisVectorLayer layer, final Point originPoint) {
    final ReceptorPoint origin = recUtil.createReceptorIdFromPoint(originPoint);

    final int width = 9;
    final int height = 20;
    final TetrisArena arena = new TetrisArena(origin, width, height);

    layer.addFeatures(HexagonPath.builder(grid)
        .sequence(v -> TetrisShapes.arena(v, arena))
        .features());
    
    return arena;
  }

  public Feature createHexagon(final Point value) {
    final ReceptorPoint receptor = recUtil.createReceptorIdFromPoint(value);

    final String hexagonWKT = HexagonUtil.createHexagonWkt(receptor, InformationZoomLevel.get());
    final Wkt wkt = new Wkt();
    final WktReadOptions wktOptions = OLFactory.createOptions();
    return wkt.readFeature(hexagonWKT, wktOptions);
  }

  public void fit(final Map map, final TetrisArena arena) {
    final ReceptorPoint origin = arena.getOrigin();

    final HexagonPath pathFinder = HexagonPath.builder(grid)
        .view();

    final ReceptorPoint bottomRight = pathFinder
        .move(arena.getOrigin())
        .right(arena.getWidth() / 2)
        .right(arena.getWidth() / 2)
        .down(arena.getHeight())
        .pointer();

    map.getView().fit(new Extent(origin.getX(), bottomRight.getY(), bottomRight.getX(), origin.getY()));
  }
}
