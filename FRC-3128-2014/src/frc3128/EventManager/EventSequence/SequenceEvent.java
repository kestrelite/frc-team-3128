package frc3128.EventManager.EventSequence;

import frc3128.EventManager.Event;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public abstract class SequenceEvent extends Event {
    private long    startTime      = -1;
    private boolean eventIsRunning = false;
    
    public SequenceEvent() {super();}
    
    /**
     * Starts the sequence event running. 
     */
    final protected void startSequenceEvent() {
        if(startTime == -1) this.startTime = System.currentTimeMillis();
        this.eventIsRunning = true;
    }
    
    /**
     * Returns whether or not the current SequenceEvent is running.
     * 
     * @return whether the current SequenceEvent is running
     */
    final protected boolean isRunning() {return eventIsRunning;}
    
    /**
     * Finds how long the SequenceEvent ran.
     * 
     * @return the duration, in msec, for how long the SequenceEvent ran
     */
    final protected long getRunTimeMillis() {return (startTime == -1 ? -1 : System.currentTimeMillis() - this.startTime);}

    /**
     * 
     * @return whether the current SequenceEvent's exit condition has been met
     */
    public abstract boolean exitConditionMet();
}