package frc3128.EventManager;

import frc3128.DebugLog;

final class TimerEvent extends Event {
    private Event linkedEvent;
    private long targetTimeMillis = -1;

    public final void setTargetTime(long millis) {
        DebugLog.log(4, this, "Event " + linkedEvent.toString() + " set for " + millis + " msec from now.");
        targetTimeMillis = System.currentTimeMillis() + millis;
        EventManager.addContinuousEvent(this);
    }

    private void destroyTimer() {
        targetTimeMillis = -1;
        EventManager.removeEvent(this);
    }

    public final void execute() {
        if(targetTimeMillis == -1) {
            DebugLog.log(1, this, "Timer set without valid time!");
            this.destroyTimer();
        }

        if(System.currentTimeMillis() > targetTimeMillis) {
            DebugLog.log(4, this, "Running timed event " + this.linkedEvent.toString());
            linkedEvent.execute();
            this.destroyTimer();
        }
    }

    public final void linkEvent(Event e) {this.linkedEvent = e;}
}

public abstract class Event {
    private boolean eventIsCancelled;
    private TimerEvent timerEvent;

    public Event() {}
    
    public Event(boolean isTimerEvent) {
        if(isTimerEvent) {
            this.timerEvent = new TimerEvent();
            timerEvent.linkEvent(this);
        }
    }

    abstract public void execute();

    final public void cancelEvent() {
        eventIsCancelled = true;
        EventManager.removeEvent(this);
        DebugLog.log(4, this, "Event " + this.toString() + " has been cancelled!");
    }

    final public void cancelTimedEvent() {
        timerEvent.cancelEvent();
    }

    final protected boolean shouldRun() {
        return !eventIsCancelled;
    }

    final public void registerSingleEvent() {
        EventManager.addSingleEvent(this);
    }

    final public void registerIterableEvent() {
        EventManager.addContinuousEvent(this);
    }
    
    final public void registerTimedEvent(int delay) {
        try {
            timerEvent.setTargetTime(delay);
        } catch(Exception e) {
            DebugLog.log(2, this, "Timer event called before instantiation! Timer startup delay possible.");
            prepareTimer();
            timerEvent.setTargetTime(delay);
        }
    }

    final public void prepareTimer() {
        this.timerEvent = new TimerEvent();
        timerEvent.linkEvent(this);
    }
}