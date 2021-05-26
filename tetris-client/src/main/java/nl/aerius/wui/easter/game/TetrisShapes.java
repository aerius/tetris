package nl.aerius.wui.easter.game;

import java.util.ArrayList;
import java.util.List;

import nl.aerius.geo.domain.ReceptorPoint;

public class TetrisShapes {
  public static final TetrisShape HEX = path -> path
      .topRight(5)
      .bottomRight(5)
      .down(5)
      .bottomLeft(5)
      .topLeft(5)
      .up(5);

  public static final TetrisShape FIDGET = path -> path
      .remember()
      .highlightCurrent()
      .up()
      .reset()
      .bottomRight()
      .reset()
      .bottomLeft()
      .reset();

  public static final TetrisShape TANK_LEFT = path -> path
      .remember()
      .highlightCurrent()
      .topLeft()
      .reset()
      .bottomRight()
      .up()
      .reset();

  public static final TetrisShape TANK_RIGHT = path -> path
      .remember()
      .highlightCurrent()
      .topRight()
      .reset()
      .bottomLeft()
      .up()
      .reset();

  public static final TetrisShape L_LEFT = path -> path
      .remember()
      .highlightCurrent()
      .topLeft()
      .reset()
      .topRight()
      .topRight()
      .reset();

  public static final TetrisShape L_RIGHT = path -> path
      .remember()
      .highlightCurrent()
      .topRight()
      .reset()
      .topLeft()
      .topLeft()
      .reset();

  public static final TetrisShape ARCH = path -> path
      .remember()
      .highlightCurrent()
      .topLeft()
      .reset()
      .topRight()
      .up()
      .reset();

  public static final TetrisShape SNAKE_LEFT = path -> path
      .remember()
      .highlightCurrent()
      .down()
      .reset()
      .topRight()
      .up()
      .reset();

  public static final TetrisShape SNAKE_RIGHT = path -> path
      .remember()
      .highlightCurrent()
      .down()
      .reset()
      .topLeft()
      .up()
      .reset();

  public static final TetrisShape SNAKE_STRAIGHT = path -> path
      .remember()
      .highlightCurrent()
      .down()
      .reset()
      .up()
      .up()
      .reset();

  public static final TetrisShape BLOCK = path -> path
      .remember()
      .highlightCurrent()
      .down()
      .topRight()
      .up()
      .reset();

  public static final List<TetrisShape> DEFAULT_SHAPES = new ArrayList<>();
  static {
    DEFAULT_SHAPES.add(FIDGET);
    DEFAULT_SHAPES.add(TANK_LEFT);
    DEFAULT_SHAPES.add(TANK_RIGHT);
    DEFAULT_SHAPES.add(L_LEFT);
    DEFAULT_SHAPES.add(L_RIGHT);
    DEFAULT_SHAPES.add(ARCH);
    DEFAULT_SHAPES.add(SNAKE_LEFT);
    DEFAULT_SHAPES.add(SNAKE_RIGHT);
    DEFAULT_SHAPES.add(SNAKE_STRAIGHT);
    DEFAULT_SHAPES.add(BLOCK);
  }

  public static final HexagonPath arena(final HexagonPath path, final TetrisArena arena) {
    return path
        .view()
        .point(arena.getOrigin())
        .topLeft()
        .edit()
        .style(TetrisStyles.WALL)
        .down(arena.getHeight())
        .rightFilled(arena.getHexWidth(), false)
        .up(arena.getHeight())
        .style(TetrisStyles.WALL_THIN)
        .leftFilled(arena.getHexWidth(), false);
  }

  public static final HexagonPath demoShapes(final HexagonPath path, final ReceptorPoint origin) {
    final TetrisShape hopRight = v -> v
        .view()
        .right(5)
        .edit();
    final TetrisShape hopDown = v -> v
        .view()
        .downAbsolute(5)
        .edit();

    return path
        .point(origin)
        .sequence(TetrisStyles.get(0))
        .highlightCurrent()
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(0))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(1))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(1))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(2))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(2))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(3))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(3))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(4))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(4))
        .highlightCurrent()
        .point(origin)
        .sequence(hopDown)
        .sequence(TetrisStyles.get(0))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(5))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(1))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(6))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(2))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(7))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(3))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(8))
        .highlightCurrent()
        .sequence(hopRight)
        .sequence(TetrisStyles.get(4))
        .sequence(TetrisShapes.DEFAULT_SHAPES.get(9))
        .highlightCurrent();
  }

  public static TetrisShape random() {
    final int rand = (int) (Math.random() * DEFAULT_SHAPES.size());
    return DEFAULT_SHAPES.get(rand);
  }
}
