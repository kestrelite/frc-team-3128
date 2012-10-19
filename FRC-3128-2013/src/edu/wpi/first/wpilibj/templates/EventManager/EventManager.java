package edu.wpi.first.wpilibj.templates.EventManager;

import edu.wpi.first.wpilibj.templates.DebugLog;
import edu.wpi.first.wpilibj.templates.Global;
import java.util.Vector;

class IPSCounter extends Event {
    long firstSystemTime = -1;
    long samples = 0;

    public void execute() {
        if(firstSystemTime == -1) {
            firstSystemTime = Global.getSystemTimeMillis();
            return;
        }
        int timeDiff = (int) (Global.getSystemTimeMillis() - firstSystemTime);
        samples++;
        if(samples % 200000 == 0)
            DebugLog.log(4, referenceName, "Average IPS reading: " + (samples / ((float) (timeDiff / 1000.0))) + ", samples: " + samples + ", timeDiff " + timeDiff);
    }
}

public class EventManager {
    private static Vector e_eventList = new Vector();
    private static Vector i_priorityList = new Vector();
    private static Vector b_singleEventList = new Vector();
    private static Vector b_deleteFlag = new Vector();
    private static String referenceName = "EventManager";
    private static IPSCounter ipsCounter = new IPSCounter();

    private static boolean eventProcessingPaused = false;
    private static final boolean duplicateEventCheck = true;
    
    public EventManager() {
        DebugLog.log(4, EventManager.referenceName, "Event manager started.");
    }

    private static void insertIntoEvents(Event e, int p, boolean single) {
        int insertIndex = -1;
        for(int i = 0; i < i_priorityList.size(); i++) {
            if(((Integer) i_priorityList.elementAt(i)).intValue() <= p)
                insertIndex = i;
            break;
        }
        if(insertIndex == -1) {
            e_eventList.addElement(e);
            i_priorityList.addElement(new Integer(p));
            b_singleEventList.addElement((single ? Boolean.TRUE : Boolean.FALSE));
            b_deleteFlag.addElement(Boolean.FALSE);
        } else {
            e_eventList.insertElementAt(e, insertIndex);
            i_priorityList.insertElementAt(new Integer(p), insertIndex);
            b_singleEventList.insertElementAt((single ? Boolean.TRUE : Boolean.FALSE), insertIndex);
            b_deleteFlag.insertElementAt(Boolean.FALSE, insertIndex);
        }
    }

    private static void checkForDuplicates(Event e) {
        if(!EventManager.duplicateEventCheck) return;
        for(int i = 0; i < e_eventList.size(); i++)
            if(e.equals((Event) e_eventList.elementAt(i)))
                DebugLog.log(2, referenceName, "Event ( ^ ) being registered is a duplicate of another event!");
    }

    protected static void addContinuousEvent(Event e, int i) {
        DebugLog.log(3, referenceName, "Adding CONTINUOUS event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, i, false);
    }

    protected static void addSingleEvent(Event e, int i) {
        DebugLog.log(4, referenceName, "Adding SINGLE event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, i, true);
    }

    protected static void addSingleEvent(Event e) {
        DebugLog.log(4, referenceName, "Adding SINGLE event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, 5, true);
    }

    protected static void addContinuousEvent(Event e) {
        DebugLog.log(3, referenceName, "Adding CONTINUOUS event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, 5, false);
    }

    public static void processEvents() {
        if(EventManager.eventProcessingPaused) return;
        cleanupEvents();

        for(int i = 0; i < e_eventList.size(); i++) {
            Event event = (Event) e_eventList.elementAt(i);
            if(event.shouldRun())
                event.execute();
            if(!event.shouldRun()) {
                DebugLog.log(4, referenceName, "Cancelled event " + event.toString() + " being marked for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
            }
            if(((Boolean) b_singleEventList.elementAt(i)).booleanValue()) {
                DebugLog.log(4, referenceName, "Marking single event " + event.toString() + " for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
            }
        }
    }

    private static void cleanupEvents() {
        int count = 0;
        for(int i = 0; i < b_deleteFlag.size(); i++)
            if(((Boolean) b_deleteFlag.elementAt(i)).booleanValue()) {
                e_eventList.removeElementAt(i);
                i_priorityList.removeElementAt(i);
                b_singleEventList.removeElementAt(i);
                b_deleteFlag.removeElementAt(i);
                i--;
                count++;
            }
        if(count != 0)
            DebugLog.log(4, referenceName, "cleanupEvents removed " + count + " marked events from the queue.");
    }

    protected static void removeEvent(Event e) {
        int removedEventCount = 0;
        for(int i = 0; i < e_eventList.size(); i++)
            if(e.equals((Event) e_eventList.elementAt(i))) {
                DebugLog.log(4, referenceName, "Marking event " + ((Event) e_eventList.elementAt(i)).toString() + " for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
                removedEventCount++;
            }
        if(removedEventCount == 0)
            DebugLog.log(2, referenceName, "removeEvent was called but no event was marked for deletion!");
        if(removedEventCount > 1)
            DebugLog.log(4, referenceName, "removeEvent was called, and " + removedEventCount + " events were marked for deletion.");
    }

    public static void dropEvents() {
        DebugLog.log(4, referenceName, "Dropped " + e_eventList.size() + " events.");
        e_eventList.removeAllElements();
        i_priorityList.removeAllElements();
        b_singleEventList.removeAllElements();
        b_deleteFlag.removeAllElements();
    }

    public static void stopIPSCounter() {
        EventManager.ipsCounter.cancelEvent();
    }
    
    public static void startIPSCounter() {
        EventManager.ipsCounter.registerIterable();
    }
    
    public static void toggleEventProcessing() {
        EventManager.eventProcessingPaused = !EventManager.eventProcessingPaused;
    }
}
