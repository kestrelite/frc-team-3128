package edu.wpi.first.wpilibj.templates.AutoSequencer;

import edu.wpi.first.wpilibj.templates.DebugLog;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import java.util.Vector;

public class Sequencer extends Event {
    private static Vector e_autoSequence = new Vector();
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
}
