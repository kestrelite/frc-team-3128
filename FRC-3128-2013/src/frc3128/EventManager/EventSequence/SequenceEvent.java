package frc3128.EventManager.EventSequence;

import frc3128.EventManager.Event;

public abstract class SequenceEvent extends Event {
    private long startTime = -1;
    private boolean eventIsRunning = false;
    
    final protected void startAutoEvent() {
        if(startTime == -1) this.startTime = System.currentTimeMillis();
        this.eventIsRunning = true;
    }
    
    final protected boolean isRunning() {
        return eventIsRunning;
    }
        
    final protected long getRunTimeMillis() {
        return System.currentTimeMillis() - this.startTime;
    }
    
    public abstract boolean exitConditionMet();
}