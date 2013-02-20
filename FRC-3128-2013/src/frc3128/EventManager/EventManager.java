package frc3128.EventManager;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.DebugLog;
import java.util.Vector;

class WatchdogEvent extends Event {
    public void execute() {
        Watchdog.getInstance().feed();
    }
}

public class EventManager {
    private static Vector e_eventList = new Vector();
    private static Vector i_priorityList = new Vector();
    private static Vector b_singleEventList = new Vector();
    private static Vector b_deleteFlag = new Vector();
    private static String referenceName = "EventManager";
    private static WatchdogEvent emDebugger = new WatchdogEvent();
    private static boolean eventProcessingPaused = false;
    private static final boolean duplicateEventCheck = false;

    public EventManager() {
        DebugLog.log(4, EventManager.referenceName, "Event manager started.");
        (new WatchdogEvent()).registerIterableEvent();
        if(!duplicateEventCheck)
            DebugLog.log(4, EventManager.referenceName, "Event Manager is NOT checking for duplicate events");
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
        if(EventManager.eventProcessingPaused)
            return;
        cleanupEvents();

        for(int i = 0; i < e_eventList.size(); i++) {
            Event event = (Event) e_eventList.elementAt(i);
            if(event.shouldRun()) {
                try{
                    DebugLog.log(5, referenceName, "Running event " + event.referenceName); 
                    event.execute();
                } catch(Exception e) {
                    e.printStackTrace();
                    DebugLog.log(-3, event.referenceName, "Error in event: " + e.getMessage());
                    b_deleteFlag.setElementAt(Boolean.TRUE, i);
                }
            }
            if(!event.shouldRun()) {
                DebugLog.log(5, referenceName, "Cancelled event " + event.toString() + " being marked for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
            }
            if(((Boolean) b_singleEventList.elementAt(i)).booleanValue()) {
                DebugLog.log(5, referenceName, "Marking single event " + event.toString() + " for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
            }
        }
    }

    protected static void cleanupEvents() {
        int count = 0;
        for(int i = 0; i < b_deleteFlag.size(); i++)
            if(((Boolean) b_deleteFlag.elementAt(i)).booleanValue() || !((Event)e_eventList.elementAt(i)).shouldRun()) {
                e_eventList.removeElementAt(i);
                i_priorityList.removeElementAt(i);
                b_singleEventList.removeElementAt(i);
                b_deleteFlag.removeElementAt(i);
                i--;
                count++;
            }
    }

    protected static void removeEvent(Event e) {
        int removedEventCount = 0;
        for(int i = 0; i < e_eventList.size(); i++)
            if(e.equals((Event) e_eventList.elementAt(i))) {
                DebugLog.log(5, referenceName, "By request, marking event " + ((Event) e_eventList.elementAt(i)).toString() + " for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
                removedEventCount++;
            }
        if(removedEventCount == 0)
            DebugLog.log(2, referenceName, "removeEvent was called but no event was marked for deletion!");
        if(removedEventCount > 1)
            DebugLog.log(5, referenceName, "removeEvent was called, and " + removedEventCount + " events were marked for deletion.");
    }

    public static void dropEvents() {
        DebugLog.log(4, referenceName, "Dropped " + e_eventList.size() + " events.");
        e_eventList.removeAllElements();
        i_priorityList.removeAllElements();
        b_singleEventList.removeAllElements();
        b_deleteFlag.removeAllElements();
    }

    public static void startWatchdog() {
        EventManager.emDebugger.registerIterableEvent();
    }
    
    public static void stopWatchdog() {
        EventManager.emDebugger.cancelEvent();
    }
    
    public static void toggleEventProcessing() {
        EventManager.eventProcessingPaused = !EventManager.eventProcessingPaused;
    }
}