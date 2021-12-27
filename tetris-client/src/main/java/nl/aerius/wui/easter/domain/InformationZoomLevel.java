package nl.aerius.wui.easter.domain;

import nl.overheid.aerius.shared.domain.geo.HexagonZoomLevel;

public final class InformationZoomLevel extends HexagonZoomLevel {
  private static final long serialVersionUID = 1L;

  private static InformationZoomLevel INSTANCE;

  private InformationZoomLevel() {
    super(1, 100 * 100);
  }

  public static HexagonZoomLevel get() {
    if (INSTANCE == null) {
      INSTANCE = new InformationZoomLevel();
    }

    return INSTANCE;
  }
}
