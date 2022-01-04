package nl.aerius.wui.easter.game;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

import elemental2.dom.KeyboardEvent;

import jsinterop.base.Js;

public class TetrisGameControls {
  private final TetrisGameEngine engine;

  public TetrisGameControls(final TetrisGameEngine engine) {
    this.engine = engine;

    DOM.setEventListener(Document.get().<Element>cast(), e -> handleKey(Js.cast(e), e.getKeyCode()));
    // DOM.sinkEvents(Document.get().<Element>cast(), 0x00200); // "keyup"
    DOM.sinkEvents(Document.get().<Element>cast(), 0x00080); // "keydown"
  }

  private void handleKey(final KeyboardEvent e, final int keyCode) {
    if (!engine.isPlaying()) {
      return;
    }

    final Element elem = Js.cast(e.target);
    final String tag = elem.getTagName().toLowerCase();
    if (isInput(tag)) {
      return;
    }

    switch (keyCode) {
    case 37: // Left
    case 65: // A
      engine.moveLeft();
      break;
    case 39: // Right
    case 68: // D
      engine.moveRight();
      break;
    case 40: // Down
    case 83: // S
      engine.moveDown();
      break;
    case 81: // Q
    case 38: // Up
      engine.rotateClockwise();
      break;
    case 69: // E
      engine.roateCounterClockwise();
      break;
    case 16: // Shift
      if (e.location == 1) {
        engine.roateCounterClockwise();
      } else if (e.location == 2) {
        engine.rotateClockwise();
      }
      break;
    case 32: // Space
      engine.drop();
    }
  }

  private boolean isInput(final String tag) {
    return "input".equals(tag) || "textarea".equals(tag);
  }

  public static TetrisGameControls attach(final TetrisGameEngine engine) {
    return new TetrisGameControls(engine);
  }
}
