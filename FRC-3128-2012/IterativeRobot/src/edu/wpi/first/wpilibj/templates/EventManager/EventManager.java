package edu.wpi.first.wpilibj.templates.EventManager;

public class EventManager {

    private static EventInterfaceList iterativeObjects = new EventInterfaceList(10);
    private static EventInterfaceList eventTriggers = new EventInterfaceList(10);
    private static ThreadList iterativeThreads = new ThreadList(10);

    public synchronized static void runNextIteration() throws Exception {
        EventManager.processIterableEvents();
        EventManager.processEventStack();
    }

    public synchronized static void addIterable(EventInterface o) throws Exception {
        if (!iterativeObjects.existsEventTemplate(o)) {
            iterativeObjects.add(o);
            System.out.println("Event added: " + iterativeObjects.getIndex(iterativeObjects.nextPointer() - 1));
        } else {
            throw new Error("Pre-existing iterable event!");
        }
    }

    public synchronized static void removeIterable(EventInterface o) throws Exception {
        iterativeObjects.removeEventTemplate(o);
        eventTriggers.removeEventTemplate(o);
    }

    public synchronized static void addEventTrigger(EventInterface o) throws Exception {
        eventTriggers.removeEventTemplate(o);
        eventTriggers.add(o);
    }

    public synchronized static void processEventStack() throws Exception {
        for (int i = eventTriggers.nextPointer() - 1; i >= 0; i--) {
            if (eventTriggers.getIndex(i) != null) {
                eventTriggers.getIndex(i).eventProcessor();
            }
            eventTriggers.removeIndex(i);
        }
    }

    public synchronized static void processIterableEvents() throws Exception {
        for (int i = 0; i < iterativeObjects.nextPointer(); i++) {
            synchronized (iterativeObjects) {
                synchronized (iterativeObjects.getIndex(i)) {

                    boolean j = iterativeObjects.getIndex(i).eventIterable();

                    if (!j) {
                        iterativeObjects.removeIndex(i);
                        System.out.println("RBT: WARN: Removing event not intended as iterable!");
                        System.out.println("RBT: ----: " + iterativeObjects.getIndex(i));
                    }
                }
            }
        }
    }
}