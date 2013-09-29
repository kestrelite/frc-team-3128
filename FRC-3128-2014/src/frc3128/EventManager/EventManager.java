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
    private static Vector  eventList               = new Vector();
    private static Vector  singleEventListFlag     = new Vector();
    private static Vector  deleteFlag              = new Vector();
    private static boolean eventProcessingDisabled = false;
    private static long    iteration               = 0;
    
    private static void insertIntoEvents(Event event, boolean single) {
        eventList.addElement(event);
        singleEventListFlag.addElement(single ? Boolean.TRUE : Boolean.FALSE);
        deleteFlag.addElement(Boolean.FALSE);
    }

    private static void checkForDuplicates(Event event) {
        if(!Constants.EVENT_DUPLICATE_CHECKS) return;
        for(int i = 0; i < eventList.size(); i++)
            if(event.equals((Event) eventList.elementAt(i)) && !(deleteFlag.elementAt(i) == Boolean.TRUE))
                DebugLog.log(DebugLog.LVL_WARN, "EventManager", "Event ( ^ ) being registered is a duplicate of another event!");
    }

    protected static void addSingleEvent(Event event) {
        DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Adding SINGLE event " + event.toString());
        checkForDuplicates(event);
        insertIntoEvents(event, true);
    }

    protected static void addContinuousEvent(Event event) {
        DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Adding CONTINUOUS event " + event.toString());
        checkForDuplicates(event);
        insertIntoEvents(event, false);
    }

    /**
     * Processes the Vector of events and executes them in their order of
     * insertion.
     */
    public static void processEvents() {
        if(EventManager.eventProcessingDisabled) return; iteration++;
        if(iteration % Constants.EVENT_CLEANUP_AFTER_ITER == 0) cleanupEvents();

        int deletedEvents = 0;
        for(int i = 0; i < eventList.size(); i++) {
            if(((Boolean) deleteFlag.elementAt(i)).booleanValue()) {deletedEvents++; continue;}
            
            Event event = (Event) eventList.elementAt(i);
            if(((Boolean) singleEventListFlag.elementAt(i)).booleanValue()) {
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Marking single event " + event.toString() + " for deletion.");
                deleteFlag.setElementAt(Boolean.TRUE, i); deletedEvents++;
            }
            if(event.shouldRun()) {
                try{
                    DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Running event " + event.toString()); 
                    event.execute();
                } catch(Exception e) {
                    e.printStackTrace();
                    DebugLog.log(DebugLog.LVL_ERROR, event.toString(), "Uncaught exception in event: " + e.getMessage());
                    e.printStackTrace();
                    DebugLog.log(DebugLog.LVL_ERROR, event.toString(), "Due to error, deleting event: " + event.toString());
                    deleteFlag.setElementAt(Boolean.TRUE, i); deletedEvents++;
                }
            }
            if(!event.shouldRun()) {
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Cancelled event " + event.toString() + " being marked for deletion.");
                deleteFlag.setElementAt(Boolean.TRUE, i); deletedEvents++;
            }
        }
        if(deletedEvents >= Constants.EVENT_CLEANUP_AFTER_DELEV) {
            DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Running cleanupEvents from " + deletedEvents + 
                    " deleted events (>=" + Constants.EVENT_CLEANUP_AFTER_DELEV + ").");
            cleanupEvents();
        }
    }

    protected static void cleanupEvents() {
        int count = 0;
        for(int i = 0; i < deleteFlag.size(); i++)
            if(((Boolean) deleteFlag.elementAt(i)).booleanValue() || !((Event)eventList.elementAt(i)).shouldRun()) {
                eventList.removeElementAt(i);
                singleEventListFlag.removeElementAt(i);
                deleteFlag.removeElementAt(i);
                i--; count++;
            }
        if(count != 0) DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "Cleaned up events, total: " + count);
    }

    protected static void removeEvent(Event event) {
        int removedEventCount = 0;
        for(int i = 0; i < eventList.size(); i++)
            if(event.equals((Event) eventList.elementAt(i))) {
                DebugLog.log(DebugLog.LVL_STREAM, "EventManager", "By request, marking event " + ((Event) eventList.elementAt(i)).toString() + " for deletion.");
                deleteFlag.setElementAt(Boolean.TRUE, i);
                removedEventCount++;
            }
        if(removedEventCount == 0)
            DebugLog.log(DebugLog.LVL_WARN, "EventManager", "removeEvent was called but no event was marked for deletion!");
        if(removedEventCount > 1)
            DebugLog.log(DebugLog.LVL_INFO, "EventManager", "removeEvent was called, and " + removedEventCount + " events were marked for deletion.");
    }

    /**
     * Removes all events from the queue; clears any running tasks.
     */
    public static void dropAllEvents() {
        DebugLog.log(DebugLog.LVL_INFO, "EventManager", "Dropped ALL " + eventList.size() + " events.");
        eventList.removeAllElements();
        singleEventListFlag.removeAllElements();
        deleteFlag.removeAllElements();
    }

    /**
     * Determines whether or not an event currently exists in the stack.
     *
     * @return whether the event exists
     */
    public static boolean containsEvent(Event e) {
        for(int i = 0; i < eventList.size(); i++)
            if(eventList.elementAt(i).equals(e)) return true;
        return false;
    }
    
    /**
     * Temporarily enables or disables event processing.
     */
    public static void toggleEventProcessing() {EventManager.eventProcessingDisabled = !EventManager.eventProcessingDisabled;}
    
    /**
     * Disables event processing.
     */
    public static void disableEventProcessing() {EventManager.eventProcessingDisabled = true;}
    
    /**
     * Enables event processing.
     */
    public static void enableEventProcessing() {EventManager.eventProcessingDisabled = false;}
    
    private EventManager() {}
}