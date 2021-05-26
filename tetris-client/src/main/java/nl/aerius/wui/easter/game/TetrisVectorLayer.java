package nl.aerius.wui.easter.game;

import java.util.Collection;
import java.util.List;

import ol.Feature;
import ol.OLFactory;
import ol.layer.Vector;
import ol.layer.VectorLayerOptions;
import ol.style.Fill;
import ol.style.Style;
import ol.style.StyleOptions;

import nl.aerius.wui.util.SchedulerUtil;

public class TetrisVectorLayer extends Vector {
  private final ol.source.Vector vectorSource;

  public TetrisVectorLayer() {
    super(getOptions());

    vectorSource = new ol.source.Vector(OLFactory.createOptions());
    setSource(vectorSource);
  }

  private static VectorLayerOptions getOptions() {
    final StyleOptions defaultStyleOptions = new StyleOptions();
    defaultStyleOptions.setStroke(OLFactory.createStroke(OLFactory.createColor(214, 51, 39, 1), 2));
    defaultStyleOptions.setFill(OLFactory.createFill(OLFactory.createColor(255, 255, 255, 0.4)));

    final Style defaultStyle = new Style(defaultStyleOptions);

    final VectorLayerOptions vectorLayerOptions = OLFactory.createOptions();
    vectorLayerOptions.setUpdateWhileAnimating(true);
    vectorLayerOptions.setUpdateWhileInteracting(true);
    vectorLayerOptions.setStyle(defaultStyle);

    return vectorLayerOptions;
  }

  public void addFeature(final Feature feat) {
    vectorSource.addFeature(feat);
  }

  public void addFeatures(final Collection<Feature> feat) {
    vectorSource.addFeatures(feat.toArray(new Feature[feat.size()]));
  }

  public void addFeatures(final Feature[] feat) {
    vectorSource.addFeatures(feat);
  }

  public void clear() {
    vectorSource.clear(true);
  }

  public void removeFeatures(final Collection<Feature> features) {
    features.stream()
        .forEach(feat -> vectorSource.removeFeature(feat));
  }

  public void removeFeature(final Feature feature) {
    vectorSource.removeFeature(feature);
  }

  public void removeFeaturesFancily(final List<Feature> lst, final int period) {
    final Fill white = OLFactory.createFill(OLFactory.createColor("#fff"));
    final Fill black = OLFactory.createFill(OLFactory.createColor("#000"));
    
    final Style whiteStyle = OLFactory.createStyle(white);
    final Style blackStyle = OLFactory.createStyle(black);

    final int animationFrame = period / 6;

    SchedulerUtil.delay(() -> {
      lst.forEach(feat -> feat.setStyle(whiteStyle));
    }, animationFrame * 1);
    SchedulerUtil.delay(() -> {
      lst.forEach(feat -> feat.setStyle(blackStyle));
    }, animationFrame * 2);
    SchedulerUtil.delay(() -> {
      lst.forEach(feat -> feat.setStyle(whiteStyle));
    }, animationFrame * 3);
    SchedulerUtil.delay(() -> {
      lst.forEach(feat -> feat.setStyle(blackStyle));
    }, animationFrame * 4);
    SchedulerUtil.delay(() -> {
      lst.forEach(feat -> feat.setStyle(whiteStyle));
    }, animationFrame * 5);
    SchedulerUtil.delay(() -> {
      removeFeatures(lst);
    }, (int) (animationFrame * 5.5D));
  }

  public void removeSelf() {
  }
}
