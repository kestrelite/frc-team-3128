package edu.wpi.first.wpilibj.templates.EventManager;

public class EventTimer implements EventInterface {

    private double targetTime;
    private boolean active = false;
    private EventInterface e;

    public EventTimer(EventInterface e) {
        this.e = e;
    }

    public EventInterface getInterface() {
        return e;
    }

    public void enableTargetTime(double time) throws Exception {
        if (!active) {
            EventManager.addIterable(this);
            targetTime = System.currentTimeMillis() + time * 1000.0;
            active = true;
        } else {
            System.out.println("[RBT]: Logging event timer while already active!");
        }
    }

    public boolean eventIterable() throws Exception {
        if (active) {
            if (System.currentTimeMillis() > targetTime) {
                e.eventRegisterSelf();
                active = false;
                EventManager.removeIterable(this);
            }
        }
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
}
