package nl.aerius.wui.easter;

import com.google.web.bindery.event.shared.EventBus;

import elemental2.dom.HTMLElement;

import nl.aerius.wui.util.SchedulerUtil;

public class EasterUtil {
  public static void attachTo(final HTMLElement element, final EventBus eventBus) {
    new EasterAttachment(element, eventBus);
  }

  private static class EasterAttachment {
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
      if (logoClickCounter == 3) {
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
  }
}
