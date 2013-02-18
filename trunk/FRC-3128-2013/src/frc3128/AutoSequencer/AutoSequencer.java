package frc3128.AutoSequencer;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import java.util.Vector;

public class AutoSequencer extends Event {
    private Vector e_autoSequence = new Vector();
    private int currentEventIndex = 0;
    
    public void execute() {
        AutoEvent ptr = ((AutoEvent)e_autoSequence.elementAt(currentEventIndex));
        
        if(currentEventIndex == e_autoSequence.size()) {
            this.cancelEvent(); 
            DebugLog.log(3, referenceName, "Autonomous Sequence complete.");
            return;
        }
        if(ptr.exitConditionMet()) {currentEventIndex++; return;}
        
        ptr.startIntrTimer();
        ptr.registerSingleEvent();
        DebugLog.log(3, referenceName, "Event " + ptr.getClass().getName() + " executed ("+currentEventIndex+").");
    }
    
    public void addEvent(AutoEvent e) {
        this.e_autoSequence.addElement(e);
    }
    
    public void startAutonomous() {
        this.registerIterableEvent();
    }
}
