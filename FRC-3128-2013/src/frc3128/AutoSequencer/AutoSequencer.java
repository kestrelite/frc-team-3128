package frc3128.AutoSequencer;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import java.util.Vector;

public class AutoSequencer extends Event {
    private Vector e_autoSequence = new Vector();
    private int currentEventIndex = 0;
    
    public void execute() {
        if(currentEventIndex == e_autoSequence.size()) {
            this.cancelEvent(); 
            DebugLog.log(3, referenceName, "Autonomous Sequence complete.");
            return;
        }
        
        AutoEvent ptr = ((AutoEvent)e_autoSequence.elementAt(currentEventIndex));
        
        if(!ptr.isRunning()) {
            ptr.startAutoEvent();
            DebugLog.log(4, referenceName, "Event " + ptr.getClass().getName() + " started ("+currentEventIndex+").");
        }
        ptr.execute();
        if(ptr.exitConditionMet()) {
            currentEventIndex++; 
            DebugLog.log(4, referenceName, "Event " + ptr.getClass().getName() + " ended (" + (currentEventIndex - 1) + ").");
            return;
        }
    }
    
    public void addEvent(AutoEvent e) {
        this.e_autoSequence.addElement(e);
        DebugLog.log(4, referenceName, "Event " + e.getClass().getName() + " added to array at index " + (this.e_autoSequence.size()-1));
    }
    
    public void startAutonomous() {
        this.registerIterableEvent();
    }
}
