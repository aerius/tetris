package nl.aerius.wui.easter.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.overheid.aerius.shared.domain.geo.HexagonUtil;
import nl.overheid.aerius.shared.domain.geo.HexagonZoomLevel;
import nl.overheid.aerius.shared.domain.v2.geojson.Point;
import nl.overheid.aerius.shared.domain.v2.geojson.Polygon;
import nl.overheid.aerius.shared.geometry.ReceptorUtil;

public final class EasterGeoUtil {
  private EasterGeoUtil() {}

  public static ReceptorPoint getReceptorFromPoint(final ReceptorUtil util, final Point point) {
    final int recId = util.getReceptorIdFromPoint(point);
    return getReceptorFromId(util, recId);
  }

  public static ReceptorPoint getReceptorFromId(final ReceptorUtil util, final int recId) {
    final Point recCenter = util.getPointFromReceptorId(recId);
    return new ReceptorPoint(recId, recCenter.getX(), recCenter.getY());
  }

  public static String createHexagonWkt(final ReceptorPoint receptor, final HexagonZoomLevel hexagonZoomLevel) {
    final Point point = new Point(receptor.getX(), receptor.getY());
    final Polygon poly = HexagonUtil.createHexagon(point, hexagonZoomLevel);
    final String wktGeometry = "POLYGON((" + Stream.of(poly.getCoordinates()[0])
        .map(v -> v[0] + " " + v[1])
        .collect(Collectors.joining(",")) + "))";

    return wktGeometry;
  }
}
