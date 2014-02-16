package frc3128.EventManager.EventSequence;

import frc3128.EventManager.Event;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public abstract class SequenceEvent extends Event {
    private long    startTime      = -1;
    private boolean eventIsRunning = false;
    private EventSequencer eventSequencer;
    
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
     * Set the internal EventSequencer for the SequenceEvent
     * 
     * @param es the linked EventSequencer
     */
    final protected void setEventSequencer(EventSequencer es) {this.eventSequencer = es;}
    
    /**
     * Gets the linked EventSequencer.
     * 
     * @return the linked EventSequencer
     */
    final public EventSequencer getEventSequencer() {return this.eventSequencer;}
    
    /**
     * 
     * @return whether the current SequenceEvent's exit condition has been met
     */
    public abstract boolean exitConditionMet();
}