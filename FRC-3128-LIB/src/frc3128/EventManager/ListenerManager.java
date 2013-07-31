package frc3128.EventManager;

import frc3128.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class ListenerManager {
    private static Vector event = new Vector();
    private static Vector key = new Vector();
    private static String referenceName = "ListenerManager";

    private static void verifyNoDuplicate(Event e, int link) {
        for(int i = 0; i < event.size(); i++)
            if((Event) event.elementAt(i) == e)
                if(((Integer) ListenerManager.key.elementAt(i)).intValue() == link)
                    DebugLog.log(DebugLog.LVL_SEVERE, referenceName, "Duplicate event/key pair was added to the ListenerManager!");
    }

	/**
	 * Adds an Event and key to the Listener system. This has the effect of 
	 * triggering the Event once whenever the key is invoked.
	 * 
	 * @param event The Event to be added to the ListenerManager
	 * @param key   The String key with which the Event should be associated
	 */
    public static void addListener(Event event, String key) {
        DebugLog.log(DebugLog.LVL_DEBUG, "ListenerManager", "Added " + event.toString() + " to " + key);
        addListener(event, key.hashCode());
    }
    
	/**
	 * Adds an Event and key to the Listener system. This has the effect of 
	 * triggering the Event once whenever the key is invoked.
	 * 
	 * @param event The Event to be added to the ListenerManager
	 * @param key   The integer key with which the Event should be associated
	 */
    public static void addListener(Event event, int key) {
        verifyNoDuplicate(event, key);
        ListenerManager.event.addElement(event);
        ListenerManager.key.addElement(new Integer(key));
    }

	/**
	 * Calls all Events associated with the given key. The Events will be run 
	 * once. They will be kept in the ListenerManager's index.
	 * 
	 * @param key The String key whose associated Events should be run
	 */
    public static void callListener(String key) {callListener(key.hashCode());}
    
	/**
	 * Calls all Events associated with the given key. The Events will be run 
	 * once. They will be kept in the ListenerManager's index.
	 * 
	 * @param key The integer key whose associated Events should be run
	 */
	public static void callListener(int link) {
        for(int i = 0; i < ListenerManager.key.size(); i++) {
            if(((Integer) ListenerManager.key.elementAt(i)).intValue() == link) {
                try {
                    ((Event) ListenerManager.event.elementAt(i)).registerSingleEvent();
                } catch(Exception e) {
                    e.printStackTrace();
                    ListenerManager.event.removeElementAt(i);
                    ListenerManager.key.removeElementAt(i);
                    DebugLog.log(DebugLog.LVL_ERROR, ((Event) ListenerManager.event.elementAt(i)).toString(), "Error in Listener event: " + e.getMessage());
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
            n = ListenerManager.event.lastIndexOf(event);
            if(n == -1) break;
            ListenerManager.event.removeElementAt(n);
            ListenerManager.key.removeElementAt(n);
            DebugLog.log(DebugLog.LVL_DEBUG, referenceName, "Listener link number " + n + " sliced from event " + event.toString());
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
        for(int i = 0; i < ListenerManager.event.size(); i++)
            if(((Event)ListenerManager.event.elementAt(i)).getClass().equals(c)) {
                ListenerManager.dropEvent((Event)ListenerManager.event.elementAt(i));
            }
        DebugLog.log(DebugLog.LVL_INFO, referenceName, "Dropping event class " + c.getName());
    }

	/**
	 * Drops all Event/key pairs with the given key.
	 * 
	 * @param key The String key to be dropped from the table.
	 */
	public static void dropListener(String key) {ListenerManager.dropListener(key.hashCode());}
	
	/**
	 * Drops all Event/key pairs with the given key.
	 * 
	 * @param key The integer key to be dropped from the table.
	 */
    public static void dropListener(int key) {
        DebugLog.log(3, referenceName, "Dropping link " + key);
        for(int i = 0; i < ListenerManager.key.size(); i++) {
            if(i > ListenerManager.key.size()) return;
            if(((Integer)ListenerManager.key.elementAt(i)).intValue() == key) {
                DebugLog.log(DebugLog.LVL_DEBUG, referenceName, "Listener dropped: " + ListenerManager.event.elementAt(i).getClass().getName() + " ("+key+").");
                ListenerManager.event.removeElementAt(i);
                ListenerManager.key.removeElementAt(i);
            }
        }
    }

	/**
	 * Clears the entire Listener table.
	 */
    public static void dropAllListeners() {
        DebugLog.log(DebugLog.LVL_INFO, "ListenerManager", "Dropped ALL " + ListenerManager.event.size() + " listeners.");
        ListenerManager.event.removeAllElements();
        ListenerManager.key.removeAllElements();
    }
	
	private ListenerManager() {}
}