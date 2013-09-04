package frc3128.EventManager.EventSequence;

import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class EventSequencer extends Event {
    private Vector autoSequenceList  = new Vector();
    private int    currentEventIndex = 0;
    
    /**
     * This function will be called automatically by the EventManager.<p><b>Do
     * not invoke it.</b>
     */
    public void execute() {
        if(currentEventIndex == autoSequenceList.size()) {
            this.stopSequence(); 
            DebugLog.log(DebugLog.LVL_INFO, this, "Event Sequence complete.");
            return;
        }
        
        SequenceEvent ptr = ((SequenceEvent)autoSequenceList.elementAt(currentEventIndex));
        
        if(!ptr.isRunning()) {
            ptr.startSequenceEvent();
            DebugLog.log(DebugLog.LVL_STREAM, this, "Event " + ptr.getClass().getName() + " started ("+currentEventIndex+").");
        }
        ptr.execute();
        if(ptr.exitConditionMet()) {
            currentEventIndex++; 
            DebugLog.log(DebugLog.LVL_STREAM, this, "Event " + ptr.getClass().getName() + " ended (" + (currentEventIndex - 1) + ").");
        }
    }
    
    /**
     * Adds an event to the current EventSequencer. This event will be added in
     * linear order; it will be called in the sequence it was inserted.
     * 
     * @param seqEvent the event to be queued
     */
    public void addEvent(SequenceEvent seqEvent) {
        this.autoSequenceList.addElement(seqEvent);
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + seqEvent.getClass().getName() + " added to array at index " + (this.autoSequenceList.size()-1));
    }
    
    /**
     * Resets the execution of the current sequence and stops execution.
     */
    public void resetSequence() {this.currentEventIndex = 0; this.cancelEvent();}
    
    /**
     * Starts the current EventSequencer.
     */
    public void startSequence() {this.registerIterableEvent();}
    
    /**
     * Stops the current EventSequencer.
     */
    public void stopSequence() {this.cancelEvent();}
}