package frc3128.EventManager;

import frc3128.DebugLog;

/**
 * 
 *  @author Noah Sutton-Smolin
 */
final class TimerEvent extends Event {
    private Event linkedEvent;
    private long targetTimeMillis = -1;
    public final void setTargetTime(long millis) {
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + linkedEvent.toString() + " set for " + millis + " msec from now.");
        targetTimeMillis = System.currentTimeMillis() + millis;
        EventManager.addContinuousEvent(this);
    }

    private void destroyTimer() {
        targetTimeMillis = -1;
        EventManager.removeEvent(this);
    }

    public final void execute() {
        if(targetTimeMillis == -1) {
            DebugLog.log(DebugLog.LVL_SEVERE, this, "Timer set without valid time!");
            this.destroyTimer();
        }

        if(System.currentTimeMillis() > targetTimeMillis) {
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Running timed event " + this.linkedEvent.toString());
            linkedEvent.registerSingleEvent();
            this.destroyTimer();
        }
    }

    public final void linkEvent(Event e) {this.linkedEvent = e;}
}

public abstract class Event {
    private boolean eventIsCancelled;
    private TimerEvent timerEvent = null;
	private long lastRuntime = -1;
	
    public Event() {}
    
	/**
	 * @param isTimerEvent states whether or not the event is a timed one. If 
	 * it is, then the linked TimerEvent is created at instantiation.
	 */
	public Event(boolean isTimerEvent) {
        if(isTimerEvent) {
            this.timerEvent = new TimerEvent();
            timerEvent.linkEvent(this);
        }
    }

	/**
	 * This function is where the event is actually run. 
	 */
    public abstract void execute();
	
	/**
	 * Cancels the event if it is running. If the event is not running, this 
	 * does nothing.
	 * <p>
	 * Note: This does NOT cancel a timed event.
	 */
    final public void cancelEvent() {
        eventIsCancelled = true;
        EventManager.removeEvent(this);
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + this.toString() + " has been cancelled!");
    }

	/**
	 * Cancels the event's timer if it is running. If the timer is not running,
	 * this does nothing.
	 * <p>
	 * Note: This does NOT cancel the event's execution.
	 */
    final public void cancelTimedEvent() {timerEvent.cancelEvent();}
    final protected boolean shouldRun() {return !eventIsCancelled;}
	
	/**
	 * Adds the event to the EventManager's queue as a single-run event. This 
	 * event will be run once, and then removed from the queue.
	 */
	final public void registerSingleEvent() {EventManager.addSingleEvent(this);}
	
	/**
	 * Adds the event to the EventManager's queue as an iterable event. This
	 * event will not be deleted from the queue once it has been run; it will
	 * be run every iteration. It must be canceled explicitly.
	 */
    final public void registerIterableEvent() {EventManager.addContinuousEvent(this);}

	/**
	 * Starts the event's linked TimerEvent. The TimerEvent is an iterable
	 * event. Each iteration the TimerEvent will check to see if the time has 
	 * expired; if it has, it will run the current event as a SingleEvent.
	 * <p>
	 * Note: If the timer has not been created, then it will be created 
	 * when this method is first called. It is recommended that you call 
	 * Event.prepareTimer() before executing this method.
	 * 
	 * @param msec the time in milliseconds after which the event will execute.
	 */
    final public void registerTimedEvent(int msec) {
        try {
            timerEvent.setTargetTime(msec);
        } catch(Exception e) {
            DebugLog.log(DebugLog.LVL_WARN, this, "Timer event called before instantiation! Timer startup delay possible.");
            prepareTimer();
            timerEvent.setTargetTime(msec);
        }
    }

	/**
	 * Creates and links an instance of the TimerEvent. The TimerEvent must be
	 * created if Event.registerTimedEvent() is to be called. It is recommended
	 * that you call this function before invoking registerTimedEvent().
	 * <p>
	 * You may also pass "true" to the constructor during instantiation to 
	 * create the TimerEvent.
	 */
    final public void prepareTimer() {
        if(this.timerEvent != null) return;
		this.timerEvent = new TimerEvent();
        timerEvent.linkEvent(this);
    }
}