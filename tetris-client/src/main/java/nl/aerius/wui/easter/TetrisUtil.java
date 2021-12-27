package nl.aerius.wui.easter;

import ol.Extent;
import ol.Feature;
import ol.Map;
import ol.OLFactory;
import ol.format.Wkt;
import ol.format.WktReadOptions;

import nl.aerius.wui.easter.domain.InformationZoomLevel;
import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.aerius.wui.easter.game.HexagonPath;
import nl.aerius.wui.easter.game.TetrisArena;
import nl.aerius.wui.easter.game.TetrisShapes;
import nl.aerius.wui.easter.game.TetrisVectorLayer;
import nl.aerius.wui.easter.util.EasterGeoUtil;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;
import nl.overheid.aerius.shared.domain.v2.geojson.Point;
import nl.overheid.aerius.shared.geometry.ReceptorUtil;

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
    final ReceptorPoint origin = EasterGeoUtil.getReceptorFromPoint(recUtil, originPoint);

    final int width = 9;
    final int height = 20;
    final TetrisArena arena = new TetrisArena(origin, width, height);

    layer.addFeatures(HexagonPath.builder(grid)
        .sequence(v -> TetrisShapes.arena(v, arena))
        .features());

    return arena;
  }

  public Feature createHexagon(final Point value) {
    final ReceptorPoint receptor = EasterGeoUtil.getReceptorFromPoint(recUtil, value);

    final String hexagonWKT = EasterGeoUtil.createHexagonWkt(receptor, InformationZoomLevel.get());
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
