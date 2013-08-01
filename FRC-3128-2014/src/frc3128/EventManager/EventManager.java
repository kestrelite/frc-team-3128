package frc3128.EventManager;

import frc3128.Util.Constants;
import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */

//Several untested modifications; rollback to revision 122 if it fails, though I don't know why it would
public class EventManager {
    private static Vector e_eventList = new Vector();
    private static Vector b_singleEventList = new Vector();
    private static Vector b_deleteFlag = new Vector();
    private static boolean eventProcessingPaused = false;
	
    private static void insertIntoEvents(Event e, boolean single) {
        e_eventList.addElement(e);
        b_singleEventList.addElement((single ? Boolean.TRUE : Boolean.FALSE));
        b_deleteFlag.addElement(Boolean.FALSE);
    }

    private static void checkForDuplicates(Event e) {
        if(!Constants.EVENT_DUPLICATE_CHECKS) return;
        for(int i = 0; i < e_eventList.size(); i++)
            if(e.equals((Event) e_eventList.elementAt(i)))
                DebugLog.log(DebugLog.LVL_WARN, "EventManager", "Event ( ^ ) being registered is a duplicate of another event!");
    }

    protected static void addSingleEvent(Event e) {
        DebugLog.log(4, "EventManager", "Adding SINGLE event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, true);
    }

    protected static void addContinuousEvent(Event e) {
        DebugLog.log(3, "EventManager", "Adding CONTINUOUS event " + e.toString());
        checkForDuplicates(e);
        insertIntoEvents(e, false);
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
                    DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Running event " + event.toString()); 
                    event.execute();
                } catch(Exception e) {
                    e.printStackTrace();
                    DebugLog.log(DebugLog.LVL_ERROR, event.toString(), "Uncaught exception in event: " + e.getMessage());
					e.printStackTrace();
                    b_deleteFlag.setElementAt(Boolean.TRUE, i);
                }
            }
            if(!event.shouldRun()) {
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Cancelled event " + event.toString() + " being marked for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
            }
            if(((Boolean) b_singleEventList.elementAt(i)).booleanValue()) {
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Marking single event " + event.toString() + " for deletion.");
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
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "By request, marking event " + ((Event) e_eventList.elementAt(i)).toString() + " for deletion.");
                b_deleteFlag.setElementAt(Boolean.TRUE, i);
                removedEventCount++;
            }
        if(removedEventCount == 0)
            DebugLog.log(DebugLog.LVL_WARN, "EventManager", "removeEvent was called but no event was marked for deletion!");
        if(removedEventCount > 1)
            DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "removeEvent was called, and " + removedEventCount + " events were marked for deletion.");
    }
	
	/**
	 * Removes all events from the queue; clears any running tasks.
	 */
    public static void dropAllEvents() {
        DebugLog.log(DebugLog.LVL_INFO, "EventManager", "Dropped ALL " + e_eventList.size() + " events.");
        e_eventList.removeAllElements();
        b_singleEventList.removeAllElements();
        b_deleteFlag.removeAllElements();
    }

	/**
	 * Temporarily enables or disables event processing.
	 */
    public static void toggleEventProcessing() {EventManager.eventProcessingPaused = !EventManager.eventProcessingPaused;}

	private EventManager() {}
}