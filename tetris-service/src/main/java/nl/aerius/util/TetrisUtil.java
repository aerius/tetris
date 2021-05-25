package nl.aerius.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TetrisUtil {
  private static final int[] TETRIS_SCORES = new int[] { 40, 100, 300, 1200 };

  public static int calculateScore(final int[] scoreComposition) {
    final AtomicInteger clears = new AtomicInteger();

    return Arrays.stream(scoreComposition)
        .sequential()
        .map(size -> IntStream.range(0, size)
            .map(v -> calculateIncrement(clears.incrementAndGet(), size))
            .sum())
        .sum();
  }

  private static int calculateIncrement(final int clears, final int size) {
    return TETRIS_SCORES[size - 1] * getLevel(clears);
  }

  private static int getLevel(final int clears) {
    return clears / 10 + 1;
  }
}
