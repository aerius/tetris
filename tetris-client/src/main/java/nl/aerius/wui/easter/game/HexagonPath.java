package nl.aerius.wui.easter.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ol.Feature;
import ol.OLFactory;
import ol.format.Wkt;
import ol.format.WktReadOptions;
import ol.style.Style;

import nl.aerius.wui.easter.domain.InformationZoomLevel;
import nl.aerius.wui.easter.domain.ReceptorPoint;
import nl.aerius.wui.easter.util.EasterGeoUtil;
import nl.aerius.wui.util.MathUtil;
import nl.overheid.aerius.shared.domain.geo.ReceptorGridSettings;
import nl.overheid.aerius.shared.geometry.ReceptorUtil;

public final class HexagonPath {
  private final ReceptorUtil recUtil;
  private final ReceptorGridSettings grid;

  private ReceptorPoint pointer;
  private ReceptorPoint remember;

  private final Map<Integer, ReceptorPoint> points = new LinkedHashMap<>();
  private final Map<Integer, Feature> features = new LinkedHashMap<>();

  private boolean editMode = true;
  private boolean highlight;

  private int rotation = 0;
  private static List<Function<HexagonPath, HexagonPath>> movements = new ArrayList<>();
  private Style style;
  private Style highlightStyle;
  private boolean highlightNext;
  static {
    movements.add(HexagonPath::upAbsolute);
    movements.add(HexagonPath::topRightAbsolute);
    movements.add(HexagonPath::bottomRightAbsolute);
    movements.add(HexagonPath::downAbsolute);
    movements.add(HexagonPath::bottomLeftAbsolute);
    movements.add(HexagonPath::topLeftAbsolute);
  }

  private HexagonPath(final ReceptorGridSettings grid) {
    this.grid = grid;
    recUtil = new ReceptorUtil(grid);
  }

  public static HexagonPath builder(final ReceptorGridSettings grid) {
    return new HexagonPath(grid);
  }

  public static HexagonPath builder(final ReceptorGridSettings grid, final ReceptorPoint origin) {
    return new HexagonPath(grid)
        .point(origin);
  }

  public static HexagonPath builder(final ReceptorGridSettings grid, final ReceptorPoint origin, final boolean edit) {
    return new HexagonPath(grid)
        .point(origin)
        .edit(edit);
  }

  public static HexagonPath builder(final ReceptorGridSettings grid, final boolean edit) {
    return new HexagonPath(grid)
        .edit(edit);
  }

  public HexagonPath clear() {
    points.clear();
    features.clear();
    return this;
  }

  public HexagonPath clearAndReset() {
    return clear()
        .reset();
  }

  private HexagonPath edit(final boolean edit) {
    editMode = edit;
    return this;
  }

  public HexagonPath edit() {
    return edit(true);
  }

  public HexagonPath view() {
    return edit(false);
  }

  public HexagonPath persist() {
    points.put(pointer.getId(), pointer);

    final Feature feat = createHexagon(pointer);
    feat.setStyle(highlight ? highlightStyle : style);
    features.put(pointer.getId(), feat);
    if (highlightNext) {
      highlight = false;
      highlightNext = false;
    }
    return this;
  }

  public HexagonPath style(final Style style) {
    this.style = style;
    return this;
  }

  public HexagonPath style(final Style style, final Style highlightStyle) {
    this.style = style;
    this.highlightStyle = highlightStyle;
    return this;
  }

  public HexagonPath point(final int id) {
    return point(EasterGeoUtil.getReceptorFromId(recUtil, id));
  }

  public HexagonPath remember() {
    this.remember = pointer;
    return this;
  }

  public HexagonPath reset() {
    this.pointer = remember;
    return this;
  }

  public HexagonPath point(final ReceptorPoint pointer) {
    this.pointer = pointer;
    return this;
  }

  public HexagonPath move(final int id) {
    return move(EasterGeoUtil.getReceptorFromId(recUtil, id));
  }

  public HexagonPath move(final ReceptorPoint pointer) {
    this.pointer = pointer;
    if (editMode) {
      persist();
    }
    return this;
  }

  public HexagonPath up(final int num) {
    return doRepeated(num, () -> up());
  }

  public HexagonPath upHop(final int num) {
    return doRepeatedThenPersist(num, () -> up());
  }

  public HexagonPath topRight(final int num) {
    return doRepeated(num, () -> topRight());
  }

  public HexagonPath topRightHop(final int num) {
    return doRepeatedThenPersist(num, () -> topRight());
  }

  public HexagonPath bottomRight(final int num) {
    return doRepeated(num, () -> bottomRight());
  }

  public HexagonPath bottomRightHop(final int num) {
    return doRepeatedThenPersist(num, () -> bottomRight());
  }

  public HexagonPath topLeft(final int num) {
    return doRepeated(num, () -> topLeft());
  }

  public HexagonPath topLeftHop(final int num) {
    return doRepeatedThenPersist(num, () -> topLeft());
  }

  public HexagonPath bottomLeft(final int num) {
    return doRepeated(num, () -> bottomLeft());
  }

  public HexagonPath bottomLeftHop(final int num) {
    return doRepeatedThenPersist(num, () -> bottomLeft());
  }

  public HexagonPath downWiggle() {
    return downWiggle(true);
  }

  public HexagonPath downWiggle(final boolean shift) {
    return isOffsetted(grid, pointer.getId()) | shift
        ? bottomRight()
        : bottomLeft();
  }

  public HexagonPath downWiggle(final int num) {
    return doRepeated(num, () -> downWiggle());
  }

  public HexagonPath downWiggle(final boolean shift, final int num) {
    return doRepeated(num, () -> downWiggle(shift));
  }

  public HexagonPath down(final int num) {
    return doRepeated(num, () -> down());
  }

  public HexagonPath downHop(final int num) {
    return doRepeatedThenPersist(num, () -> down());
  }

  public HexagonPath right() {
    return move(pointer.getId() + 1);
  }

  public HexagonPath right(final int num) {
    return doRepeated(num, () -> right());
  }

  public HexagonPath rightFilled(final int num) {
    return rightFilled(num, true);
  }

  public HexagonPath rightFilled(final int num, final boolean bottom) {
    return doFilled(num, () -> right(), bottom, () -> bottomRight(), () -> topRight());
  }

  public HexagonPath rightHop(final int num) {
    return doRepeatedThenPersist(num, () -> right());
  }

  public HexagonPath left() {
    return move(pointer.getId() - 1);
  }

  public HexagonPath left(final int num) {
    return doRepeated(num, () -> left());
  }

  public HexagonPath leftFilled(final int num) {
    return leftFilled(num, true);
  }

  public HexagonPath leftFilled(final int num, final boolean bottom) {
    return doFilled(num, () -> left(), bottom, () -> bottomLeft(), () -> topLeft());
  }

  public HexagonPath leftHop(final int num) {
    return doRepeatedThenPersist(num, () -> left());
  }

  private HexagonPath doRepeated(final int num, final Runnable runner) {
    for (int i = 0; i < num; i++) {
      runner.run();
    }
    return this;
  }

  private HexagonPath doRepeatedThenPersist(final int num, final Runnable runner) {
    final boolean wasEditing = editMode;
    view();
    doRepeated(num, runner);
    persist();
    if (wasEditing) {
      edit();
    }
    return this;
  }

  private HexagonPath doFilled(final int num, final Runnable action, final boolean shift, final Runnable shift1, final Runnable shift2) {
    final ReceptorPoint origin = pointer;
    doRepeated(num, action);
    final ReceptorPoint end = pointer;
    move(origin);
    if (shift) {
      shift1.run();
    } else {
      shift2.run();
    }
    doRepeated(num - 1, action);
    move(end);
    return this;

  }

  public List<ReceptorPoint> points() {
    return new ArrayList<>(points.values());
  }

  public List<Feature> features() {
    return new ArrayList<>(features.values());
  }

  public Map<Integer, Feature> featuresMap() {
    return new HashMap<>(features);
  }

  public static boolean isOffsetted(final ReceptorGridSettings grid, final int id) {
    return (id - 1) % (grid.getHexHor() * 2) >= grid.getHexHor();
  }

  /**
   * Movements that support directional transform
   */

  public HexagonPath up() {
    return rotatedMovement(0, rotation);
  }

  public HexagonPath topRight() {
    return rotatedMovement(1, rotation);
  }

  public HexagonPath bottomRight() {
    return rotatedMovement(2, rotation);
  }

  public HexagonPath down() {
    return rotatedMovement(3, rotation);
  }

  public HexagonPath bottomLeft() {
    return rotatedMovement(4, rotation);
  }

  public HexagonPath topLeft() {
    return rotatedMovement(5, rotation);
  }

  public HexagonPath rotatedMovement(final int start, final int transform) {
    return movements.get(MathUtil.positiveMod(start + transform, 6)).apply(this);
  }

  /**
   * Movements that do what they say on the tin
   */

  public HexagonPath upAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() + grid.getHexHor() * 2);
  }

  public HexagonPath topRightAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() + grid.getHexHor() + (isOffsetted(grid, pointer.getId()) ? 1 : 0));
  }

  public HexagonPath bottomRightAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() - grid.getHexHor() + (isOffsetted(grid, pointer.getId()) ? 1 : 0));
  }

  public HexagonPath downAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() - grid.getHexHor() * 2);
  }

  public HexagonPath downAbsolute(final int num) {
    assert pointer != null : "No pointer to move from.";
    return doRepeated(5, () -> downAbsolute());
  }

  public HexagonPath bottomLeftAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() - grid.getHexHor() + (isOffsetted(grid, pointer.getId()) ? 1 : 0) - 1);
  }

  public HexagonPath topLeftAbsolute() {
    assert pointer != null : "No pointer to move from.";
    return move(pointer.getId() + grid.getHexHor() + (isOffsetted(grid, pointer.getId()) ? 1 : 0) - 1);
  }

  public HexagonPath sequence(final Function<HexagonPath, HexagonPath> path) {
    return path.apply(this);
  }

  public HexagonPath rotateLeft() {
    return rotate(rotation - 1);
  }

  public HexagonPath rotateRight() {
    return rotate(rotation + 1);
  }

  public HexagonPath rotate(final int i) {
    rotation = i;
    return this;
  }

  public Feature createHexagon(final ReceptorPoint receptor) {
    final String hexagonWKT = EasterGeoUtil.createHexagonWkt(receptor, InformationZoomLevel.get());
    final Wkt wkt = new Wkt();
    final WktReadOptions wktOptions = OLFactory.createOptions();
    return wkt.readFeature(hexagonWKT, wktOptions);
  }

  public HexagonPath highlight() {
    highlight = true;
    return this;
  }

  public HexagonPath highlightNext() {
    highlightNext = true;
    highlight = true;
    return this;
  }

  public HexagonPath highlightCurrent() {
    return highlightNext()
        .persist();
  }

  public ReceptorPoint pointer() {
    return pointer;
  }
}
