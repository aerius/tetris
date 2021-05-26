package nl.aerius.wui.easter.game;

import nl.aerius.geo.domain.ReceptorPoint;

public class TetrisArena {
  private final ReceptorPoint origin;
  private final int width;
  private final int height;

  public TetrisArena(final ReceptorPoint origin, final int width, final int height) {
    assert width % 2 == 1 : "width must be an uneven number";

    this.origin = origin;
    this.width = width;
    this.height = height;
  }

  public ReceptorPoint getOrigin() {
    return origin;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height + 2;
  }

  public int getHexWidth() {
    return (width + 1) / 2;
  }

  public int getHexHeight() {
    return height;
  }
}
