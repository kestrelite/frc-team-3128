package util.EventManager;

public abstract class EventTimer {

    protected boolean enabled = false;
    protected long endTime = 0;

    protected final boolean isEnabled() {
        return enabled;
    }

    protected final boolean durationEnded() {
        if (System.currentTimeMillis() >= endTime) {
            enabled = false;
            return true;
        }
        return false;
    }

    protected abstract void end();

    public final void startWithTime(double t) {
        this.start();
        endTime = (long) (System.currentTimeMillis() + t * 1000);
        enabled = true;
    }

    protected abstract void start();
}
