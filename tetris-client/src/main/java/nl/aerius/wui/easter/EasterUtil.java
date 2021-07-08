package nl.aerius.wui.easter;

import com.google.web.bindery.event.shared.EventBus;

import elemental2.dom.HTMLElement;

import nl.aerius.wui.util.SchedulerUtil;

public class EasterUtil {
  public static EasterAttachment attachTo(final HTMLElement element, final EventBus eventBus) {
    return new EasterAttachment(element, eventBus);
  }

  public static class EasterAttachment {
    private static final int LIMIT = 3;

    private final EventBus eventBus;

    private boolean logoClickCounterResetScheduled;
    int logoClickCounter = 0;

    public EasterAttachment(final HTMLElement element, final EventBus eventBus) {
      this.eventBus = eventBus;
      element.onclick = e -> {
        onClick();
        return null;
      };
    }

    private void onClick() {
      logoClickCounter++;
      if (logoClickCounter == LIMIT) {
        logoClickCounter = 0;
        eventBus.fireEvent(new ActivateEasterEggCommand());
        logoClickCounterResetScheduled = false;
        return;
      }

      if (logoClickCounterResetScheduled) {
        return;
      }

      logoClickCounterResetScheduled = true;
      SchedulerUtil.delay(() -> {
        if (!logoClickCounterResetScheduled) {
          return;
        }

        eventBus.fireEvent(new DeactivateEasterEggCommand());
        logoClickCounter = 0;
        logoClickCounterResetScheduled = false;
      }, 500);
    }

    public int getCount() {
      return Math.max(0, LIMIT - logoClickCounter);
    }
  }
}
