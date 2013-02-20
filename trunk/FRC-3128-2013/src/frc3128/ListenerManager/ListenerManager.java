package frc3128.ListenerManager;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import java.util.Vector;

public class ListenerManager {
    private static Vector event = new Vector();
    private static Vector trigger = new Vector();
    private static String referenceName = "ListenerManager";

    public static void addListener(Event e, String link) {
        DebugLog.log(4, "ListenerManager", "Added " + e.toString() + " to " + link);
        addListener(e, link.hashCode());
    }

    private static void verifyNoDuplicate(Event e, int link) {
        for(int i = 0; i < event.size(); i++)
            if((Event) event.elementAt(i) == e)
                if(((Integer) ListenerManager.trigger.elementAt(i)).intValue() == link)
                    DebugLog.log(1, referenceName, "Duplicate event/trigger pair was added to the ListenerManager!");
    }

    public static void addListener(Event e, int link) {
        verifyNoDuplicate(e, link);
        ListenerManager.event.addElement(e);
        ListenerManager.trigger.addElement(new Integer(link));
    }

    public static void callListener(String link) {
        callListener(link.hashCode());
    }

    public static void dropEvent(Event e) {
        int n = 0;
        while(n != -1) {
            n = ListenerManager.event.lastIndexOf(e);
            if(n == -1) break;
            ListenerManager.event.removeElementAt(n);
            ListenerManager.trigger.removeElementAt(n);
            DebugLog.log(4, referenceName, "Listener link number " + n + " sliced from event " + e.toString());
        }
    }
    
    public static boolean dropEvent(Class c) {
        boolean eventDropped = false;
        for(int i = 0; i < ListenerManager.event.size(); i++) {
            if(((Event)ListenerManager.event.elementAt(i)).getClass().equals(c)) {
                ListenerManager.dropEvent((Event)ListenerManager.event.elementAt(i));
                eventDropped = true;
            }
        }
        return eventDropped;
    }
    
    public static void callListener(int link) {
        for(int i = 0; i < ListenerManager.trigger.size(); i++) {
            if(((Integer) ListenerManager.trigger.elementAt(i)).intValue() == link) {
                try {
                    ((Event) ListenerManager.event.elementAt(i)).execute();
                } catch(Exception e) {
                    e.printStackTrace();
                    ListenerManager.event.removeElementAt(i);
                    ListenerManager.trigger.removeElementAt(i);
                    DebugLog.log(-2, ((Event) ListenerManager.event.elementAt(i)).toString(), "Error in Listener event: " + e.getMessage());
                }
            }
        }
    }

    public static void dropListeners() {
        ListenerManager.event.removeAllElements();
        ListenerManager.trigger.removeAllElements();
    }
}