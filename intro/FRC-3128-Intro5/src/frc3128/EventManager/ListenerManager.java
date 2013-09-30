package frc3128.EventManager;

import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class ListenerManager {
    private static Vector eventList = new Vector();
    private static Vector keyList = new Vector();

    private static void verifyNoDuplicate(Event e, int key) {
        for(int i = 0; i < eventList.size(); i++)
            if((Event) eventList.elementAt(i) == e)
                if(((Integer) ListenerManager.keyList.elementAt(i)).intValue() == key)
                    DebugLog.log(DebugLog.LVL_WARN, "ListenerManager", "Duplicate event/key pair was added to the ListenerManager!");
    }

    /**
     * Adds an Event and key to the Listener system. This has the effect of 
     * triggering the Event once whenever the key is invoked.
     * 
     * @param event the Event to be added to the ListenerManager
     * @param key   the String key with which the Event should be associated
     */
    public static void addListener(Event event, String key) {
        DebugLog.log(DebugLog.LVL_DEBUG, "ListenerManager", "Added " + event.toString() + " to " + key);
        addListener(event, key.hashCode());
    }
    
    /**
     * Adds an Event and key to the Listener system. This has the effect of 
     * triggering the Event once whenever the key is invoked.
     * 
     * @param event the Event to be added to the ListenerManager
     * @param key   the integer key with which the Event should be associated
     */
    public static void addListener(Event event, int key) {
        verifyNoDuplicate(event, key);
        ListenerManager.eventList.addElement(event);
        ListenerManager.keyList.addElement(new Integer(key));
    }

    /**
     * Calls all Events associated with the given key. The Events will be run 
     * once. They will be kept in the ListenerManager's index.
     * 
     * @param key the String key whose associated Events should be run
     */
    public static void callListener(String key) {
        DebugLog.log(DebugLog.LVL_STREAM, "ListenerManager", "Called key " + key + "; forwarded...");
        callListener(key.hashCode());
    }
    
    /**
     * Calls all Events associated with the given key. The Events will be run 
     * once. They will be kept in the ListenerManager's index.
     * 
     * @param key the integer key whose associated Events should be run
     */
    public static void callListener(int key) {
        DebugLog.log(DebugLog.LVL_STREAM, "ListenerManager", "Called key " + key);
        for(int i = 0; i < ListenerManager.keyList.size(); i++) {
            if(((Integer) ListenerManager.keyList.elementAt(i)).intValue() == key) {
                try {
                    ((Event) ListenerManager.eventList.elementAt(i)).registerSingleEvent();
                } catch(Exception e) {
                    e.printStackTrace();
                    ListenerManager.eventList.removeElementAt(i);
                    ListenerManager.keyList.removeElementAt(i);
                    DebugLog.log(DebugLog.LVL_ERROR, ((Event) ListenerManager.eventList.elementAt(i)).toString(), "Error in Listener event: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Removes all Event/key pairs from the Listener table whose Event 
     * matches the one provided. This Event will no longer be called until 
     * it is re-added.
     * 
     * @param event The Event to be dropped 
     */
    public static void dropEvent(Event event) {
        int n = 0;
        while(n != -1) {
            n = ListenerManager.eventList.lastIndexOf(event);
            if(n == -1) break;
            ListenerManager.eventList.removeElementAt(n);
            ListenerManager.keyList.removeElementAt(n);
            DebugLog.log(DebugLog.LVL_DEBUG, "ListenerManager", "Listener key number " + n + " sliced from event " + event.toString());
        }
    }

    /**
     * Removes all Event/key pairs from the listener table whose class type is
     * of the one provided. This is considerably slower than dropping an Event,
     * so should be used sparingly.
     * 
     * @param c The class to be dropped.
     */
    public static void dropEventType(Class c) {
        for(int i = 0; i < ListenerManager.eventList.size(); i++)
            if(((Event)ListenerManager.eventList.elementAt(i)).getClass().equals(c)) {
                ListenerManager.dropEvent((Event)ListenerManager.eventList.elementAt(i));
            }
        DebugLog.log(DebugLog.LVL_INFO, "ListenerManager", "Dropping event class " + c.getName());
    }

    /**
     * Drops all Event/key pairs with the given key.
     * 
     * @param key the String key to be dropped from the table.
     */
    public static void dropListener(String key) {ListenerManager.dropListener(key.hashCode());}
    
    /**
     * Drops all Event/key pairs with the given key.
     * 
     * @param key the integer key to be dropped from the table.
     */
    public static void dropListener(int key) {
        DebugLog.log(DebugLog.LVL_INFO, "ListenerManager", "Dropping key " + key);
        for(int i = 0; i < ListenerManager.keyList.size(); i++) {
            if(i > ListenerManager.keyList.size()) return;
            if(((Integer)ListenerManager.keyList.elementAt(i)).intValue() == key) {
                DebugLog.log(DebugLog.LVL_DEBUG, "ListenerManager", "Listener dropped: " + ListenerManager.eventList.elementAt(i).getClass().getName() + " ("+key+").");
                ListenerManager.eventList.removeElementAt(i);
                ListenerManager.keyList.removeElementAt(i);
            }
        }
    }

    /**
     * Clears the entire Listener table.
     */
    public static void dropAllListeners() {
        DebugLog.log(DebugLog.LVL_INFO, "ListenerManager", "Dropped ALL " + ListenerManager.eventList.size() + " listeners.");
        ListenerManager.eventList.removeAllElements();
        ListenerManager.keyList.removeAllElements();
    }
    
    private ListenerManager() {}
}