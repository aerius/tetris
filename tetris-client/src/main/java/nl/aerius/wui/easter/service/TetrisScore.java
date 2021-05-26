package nl.aerius.wui.easter.service;

import static jsinterop.annotations.JsPackage.GLOBAL;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(namespace = GLOBAL, name = "Object", isNative = true)
public class TetrisScore {
  @JsProperty public String name;
  @JsProperty public int score;
  @JsProperty public String date;
}
