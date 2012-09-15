package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;

public class WatchdogMonitor implements EventInterface {

    public boolean eventIterable() throws Exception {
        Watchdog.getInstance().feed();
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
}
