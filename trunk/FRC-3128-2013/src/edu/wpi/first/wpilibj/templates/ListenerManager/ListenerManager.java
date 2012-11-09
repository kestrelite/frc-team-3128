package edu.wpi.first.wpilibj.templates.ListenerManager;

import edu.wpi.first.wpilibj.templates.DebugLog;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import java.util.Vector;

public class ListenerManager {
    static Vector event = new Vector();
    static Vector trigger = new Vector();
    static String referenceName = "ListenerManager";

    public static void addListener(Event e, String link) {
        addListener(e, link.hashCode());
    }

    private static void verifyNoDuplicate(Event e, int link) {
        for(int i = 0; i < event.size(); i++)
            if((Event) event.elementAt(i) == e)
                if(((Integer) ListenerManager.trigger.elementAt(i)).intValue() == link)
                    DebugLog.log(2, referenceName, "Duplicate event/trigger pair was added to the ListenerManager!");
    }

    public static void addListener(Event e, int link) {
        verifyNoDuplicate(e, link);
        ListenerManager.event.addElement(e);
        ListenerManager.trigger.addElement(new Integer(link));
    }

    public static void callListener(String link) {
        callListener(link.hashCode());
    }

    public static void callListener(int link) {
        //DebugLog.log(4, referenceName, "Listener trigger has been called: " + link);

        for(int i = 0; i < ListenerManager.trigger.size(); i++)
            if(((Integer) ListenerManager.trigger.elementAt(i)).intValue() == link) {
                DebugLog.log(4, referenceName, "Listener link " + link + " triggered Event " + ((Event) event.elementAt(i)).toString());
                try {
                    ((Event) ListenerManager.event.elementAt(i)).registerEvent();
                } catch(Exception e) {
                    e.printStackTrace();
                    ListenerManager.event.removeElementAt(i);
                    ListenerManager.trigger.removeElementAt(i);
                    DebugLog.log(-2, ((Event) ListenerManager.event.elementAt(i)).toString(), "Error in Listener event: " + e.getMessage());
                }
            }
    }

    public static void dropListeners() {
        ListenerManager.event.removeAllElements();
        ListenerManager.trigger.removeAllElements();
    }
}
