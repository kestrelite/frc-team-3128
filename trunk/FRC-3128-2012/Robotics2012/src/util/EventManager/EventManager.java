package util.EventManager;

import java.util.Enumeration;
import java.util.Vector;

public class EventManager {
    
    static Vector EventMono = new Vector();
    static Vector EventTimed = new Vector();
    
    public static void hi() {
        Enumeration e = EventMono.elements();
        e.nextElement();
    }
    
    public static void registerEventMono(EventMono a) {
        EventMono.addElement(a);
    }
    
    public static void registerEventTimer(EventTimer a, int t) {
        a.startWithTime(t);
        EventTimed.addElement(a);
    }
    
    private static void processMonoEvents() {
        Enumeration e = EventMono.elements();
        while (e.hasMoreElements()) {
            EventMono j = (EventMono) e.nextElement();
            if (j.shouldIterate()) {
                j.iterate();
            } else {
                if (j.shouldRemove()) {
                    EventMono.removeElement(j);
                }
            }
        }
    }
    
    private static void processTimedEvents() {
        Enumeration e = EventTimed.elements();
        while (e.hasMoreElements()) {
            EventTimer j = (EventTimer) e.nextElement();
            if (j.isEnabled() && j.durationEnded()) {
                j.end();
                EventTimed.removeElement(j);
            }
        }
    }
    
    public static void processEvents() {
        processTimedEvents();
        processMonoEvents();
    }
}
