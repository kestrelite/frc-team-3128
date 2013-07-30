package frc3128.EventManager;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
class WatchdogEvent extends Event {
    public void execute() {Watchdog.getInstance().feed();}
}

public class EventManager {
    private static Vector e_eventList = new Vector();
    //private static Vector i_priorityList = new Vector();
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

	//Untested; rollback to revision 122 if it fails, though I don't know why it would
    private static void insertIntoEvents(Event e, int p, boolean single) {
        e_eventList.addElement(e);
        b_singleEventList.addElement((single ? Boolean.TRUE : Boolean.FALSE));
        b_deleteFlag.addElement(Boolean.FALSE);
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

	/**
	 * Processes the Vector of events and executes them in their order of
	 * insertion.
	 */
    public static void processEvents() {
        if(EventManager.eventProcessingPaused) return;
        cleanupEvents();

        for(int i = 0; i < e_eventList.size(); i++) {
            Event event = (Event) e_eventList.elementAt(i);
            if(event.shouldRun()) {
                try{
                    DebugLog.log(5, referenceName, "Running event " + event.toString()); 
                    event.execute();
                } catch(Exception e) {
                    e.printStackTrace();
                    DebugLog.log(-3, event.toString(), "Uncaught exception in event: " + e.getMessage());
					e.printStackTrace();
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
	
	/**
	 * Removes all events from the queue; clears any running tasks.
	 */
    public static void dropAllEvents() {
        DebugLog.log(3, referenceName, "Dropped ALL " + e_eventList.size() + " events.");
        e_eventList.removeAllElements();
        b_singleEventList.removeAllElements();
        b_deleteFlag.removeAllElements();
    }

	/**
	 * Enables the Watchdog event. It is recommended to feed the watchdog in the 
	 * main robot class, though this is functional.
	 */
    public static void startWatchdog() {EventManager.emDebugger.registerIterableEvent();}
	/**
	 * Stops the Watchdog event.
	 */
    public static void stopWatchdog() {EventManager.emDebugger.cancelEvent();}
    
	/**
	 * Temporarily enables or disables event processing.
	 */
    public static void toggleEventProcessing() {EventManager.eventProcessingPaused = !EventManager.eventProcessingPaused;}
}