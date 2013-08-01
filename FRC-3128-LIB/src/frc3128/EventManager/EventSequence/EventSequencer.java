package frc3128.EventManager.EventSequence;

import frc3128.Util.DebugLog;
import frc3128.EventManager.Event;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class EventSequencer extends Event {
    private Vector e_autoSequence = new Vector();
    private int currentEventIndex = 0;
    
	/**
	 * This function will be called automatically by the EventManager.<p><b>Do
	 * not invoke it.</b>
	 */
    public void execute() {
        if(currentEventIndex == e_autoSequence.size()) {
            this.stopSequence(); 
            DebugLog.log(DebugLog.LVL_INFO, this, "Event Sequence complete.");
            return;
        }
        
        SequenceEvent ptr = ((SequenceEvent)e_autoSequence.elementAt(currentEventIndex));
        
        if(!ptr.isRunning()) {
            ptr.startSequenceEvent();
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + ptr.getClass().getName() + " started ("+currentEventIndex+").");
        }
        ptr.execute();
        if(ptr.exitConditionMet()) {
            currentEventIndex++; 
            DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + ptr.getClass().getName() + " ended (" + (currentEventIndex - 1) + ").");
            return;
        }
    }
	
	/**
	 * Adds an event to the current EventSequencer. This event will be added in
	 * linear order; it will be called in the sequence it was inserted.
	 * 
	 * @param seqEvent the event to be queued
	 */
    public void addEvent(SequenceEvent seqEvent) {
        this.e_autoSequence.addElement(seqEvent);
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Event " + seqEvent.getClass().getName() + " added to array at index " + (this.e_autoSequence.size()-1));
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