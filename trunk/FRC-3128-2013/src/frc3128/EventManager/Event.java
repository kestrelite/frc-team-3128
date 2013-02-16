package frc3128.EventManager;

import frc3128.DebugLog;

final class TimerEvent extends Event {
    private Event linkedEvent;
    private long targetTimeMillis = -1;
    private String linkedReferenceName;

    public final void setTargetTime(long millis) {
        //DebugLog.log(4, referenceName, "Event " + linkedEvent.toString() + " set for " + millis + " msec from now.");
        targetTimeMillis = System.currentTimeMillis() + millis;
        EventManager.addContinuousEvent(this);
    }

    private void destroyTimer() {
        targetTimeMillis = -1;
        EventManager.removeEvent(this);
    }

    public final void execute() {
        if(targetTimeMillis == -1) {
            DebugLog.log(1, referenceName, "Timer set without valid time!");
            this.destroyTimer();
        }

        if(System.currentTimeMillis() > targetTimeMillis) {
            //DebugLog.log(4, referenceName, "Running timed event " + linkedReferenceName);
            linkedEvent.execute();
            this.destroyTimer();
        }
    }

    final public void linkEvent(Event e) {
        this.linkedEvent = e;
        this.linkedReferenceName = this.linkedEvent.toString();
    }
}

public abstract class Event {
    private boolean eventIsCancelled;
    private TimerEvent timerEvent;
    protected String referenceName;

    public Event() {
        referenceName = this.toString();
    }
    
    public Event(boolean isTimerEvent) {
        referenceName = this.toString();
        if(isTimerEvent) {
            this.timerEvent = new TimerEvent();
            timerEvent.linkEvent(this);
        }
    }

    abstract public void execute();

    final public void cancelEvent() {
        eventIsCancelled = true;
        //DebugLog.log(4, referenceName, "Event " + referenceName + " has been cancelled!");
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
    
    final public void registerTimedEvent(int time) {
        try {
            timerEvent.setTargetTime(time);
        } catch(Exception e) {
            DebugLog.log(2, referenceName, "Timer event called before instantiation!");
            prepareTimer();
            timerEvent.setTargetTime(time);
        }
    }

    final public void prepareTimer() {
        this.timerEvent = new TimerEvent();
        timerEvent.linkEvent(this);
    }
}
