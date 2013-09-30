package frc3128.EventManager.EventSequence;

import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class EventSequencer extends Event {
    private static Vector evSeqList = new Vector();
    private Vector autoSequenceList  = new Vector();
    private int    currentEventIndex = 0;
    
    public EventSequencer() {super(); evSeqList.addElement(this);}
    
    /**
     * This will cancel ALL EventSequencers.
     */
    public static void stopAllSequencers() {
        for(int i = 0; i < evSeqList.size(); i++)
            ((EventSequencer) evSeqList.elementAt(i)).stopSequencer();
    }
    
    /**
     * This function will be called automatically by the EventManager.<p><b>Do
     * not invoke it.</b>
     */
    public void execute() {
        if(currentEventIndex == autoSequenceList.size()) {
            this.stopSequencer(); 
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
     * Starts the current EventSequencer.
     */
    public void startSequencer() {
        this.currentEventIndex = 0; 
        this.registerIterableEvent();
    }
    
    /**
     * Stops the current EventSequencer.
     */
    public void stopSequencer() {this.cancelEvent();}
}