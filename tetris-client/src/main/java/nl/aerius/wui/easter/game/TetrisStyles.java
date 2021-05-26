package nl.aerius.wui.easter.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ol.OLFactory;
import ol.style.Fill;
import ol.style.Stroke;
import ol.style.Style;

public class TetrisStyles {
  public static Style WALL = new Style();
  public static Style WALL_THIN = new Style();

  private static Style BABY_BLUE = new Style();
  private static Style APPLE = new Style();
  private static Style CYBER_YELLOW = new Style();
  private static Style BEER = new Style();
  private static Style RYB_RED = new Style();

  private static Style HIGHLIGHT_BABY_BLUE = new Style();
  private static Style HIGHLIGHT_APPLE = new Style();
  private static Style HIGHLIGHT_CYBER_YELLOW = new Style();
  private static Style HIGHLIGHT_BEER = new Style();
  private static Style HIGHLIGHT_RYB_RED = new Style();

  private static List<Style> DEFAULT_STYLES = new ArrayList<>();
  private static List<Style> DEFAULT_HIGHLIGHT_STYLES = new ArrayList<>();

  private static int NUM;

  static {
    WALL.setFill(fill("#a88"));
    WALL_THIN.setFill(fill("#acc"));

    BABY_BLUE.setFill(fill("#5199f3"));
    APPLE.setFill(fill("#72CB3B"));
    CYBER_YELLOW.setFill(fill("#FFD500"));
    BEER.setFill(fill("#FF971C"));
    RYB_RED.setFill(fill("#FF3213"));

    HIGHLIGHT_BABY_BLUE.setFill(fill("#5199f3"));
    HIGHLIGHT_BABY_BLUE.setStroke(stroke("#000"));
    HIGHLIGHT_BABY_BLUE.setZIndex(1);

    HIGHLIGHT_APPLE.setFill(fill("#72CB3B"));
    HIGHLIGHT_APPLE.setStroke(stroke("#000"));
    HIGHLIGHT_APPLE.setZIndex(1);

    HIGHLIGHT_CYBER_YELLOW.setFill(fill("#FFD500"));
    HIGHLIGHT_CYBER_YELLOW.setStroke(stroke("#000"));
    HIGHLIGHT_CYBER_YELLOW.setZIndex(1);

    HIGHLIGHT_BEER.setFill(fill("#FF971C"));
    HIGHLIGHT_BEER.setStroke(stroke("#000"));
    HIGHLIGHT_BEER.setZIndex(1);

    HIGHLIGHT_RYB_RED.setFill(fill("#FF3213"));
    HIGHLIGHT_RYB_RED.setStroke(stroke("#000"));
    HIGHLIGHT_RYB_RED.setZIndex(1);

    DEFAULT_STYLES.add(BABY_BLUE);
    DEFAULT_STYLES.add(APPLE);
    DEFAULT_STYLES.add(CYBER_YELLOW);
    DEFAULT_STYLES.add(BEER);
    DEFAULT_STYLES.add(RYB_RED);

    DEFAULT_HIGHLIGHT_STYLES.add(HIGHLIGHT_BABY_BLUE);
    DEFAULT_HIGHLIGHT_STYLES.add(HIGHLIGHT_APPLE);
    DEFAULT_HIGHLIGHT_STYLES.add(HIGHLIGHT_CYBER_YELLOW);
    DEFAULT_HIGHLIGHT_STYLES.add(HIGHLIGHT_BEER);
    DEFAULT_HIGHLIGHT_STYLES.add(HIGHLIGHT_RYB_RED);

    NUM = DEFAULT_STYLES.size();
  }

  public static Function<HexagonPath, HexagonPath> random() {
    final int rand = (int) (Math.random() * NUM);
    return get(rand);
  }

  public static Function<HexagonPath, HexagonPath> get(final int i) {
    final Style style = DEFAULT_STYLES.get(i);
    final Style highlightStyle = DEFAULT_HIGHLIGHT_STYLES.get(i);
    return path -> path.style(style, highlightStyle);
  }

  private static Fill fill(final String clr) {
    return OLFactory.createFill(OLFactory.createColor(clr));
  }

  private static Stroke stroke(final String clr) {
    return OLFactory.createStroke(OLFactory.createColor(clr), 3);
  }
}
