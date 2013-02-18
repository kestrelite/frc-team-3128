package frc3128.AutoSequencer;

import frc3128.EventManager.Event;

public abstract class AutoEvent extends Event {
    private long startTime = -1;
    
    final protected void startIntrTimer() {
        if(startTime == -1) this.startTime = System.currentTimeMillis();
    }
        
    final protected long getRunTimeMillis() {
        return System.currentTimeMillis() - this.startTime;
    }
    
    public abstract boolean exitConditionMet();
}