package EventManager;

import java.util.ArrayList;
import DebugLog.*;
import Global.Global;

class IPSCounter extends Event
{
	long firstSystemTime = -1;
	long samples = 0;

	public void execute() {
		if(firstSystemTime == -1) {firstSystemTime = Global.getSystemTimeMillis(); return;}
		int timeDiff = (int) (Global.getSystemTimeMillis() - firstSystemTime);
		samples++;
		if(samples%100000000 == 0) DebugLog.log(4, this.toString(), "Average IPS reading: " + (samples / ((float)(timeDiff/1000.0))) + ", samples: " + samples + ", timeDiff " + timeDiff);
	}
}

public class EventManager {
	private static ArrayList<Event> eventList = new ArrayList<Event>();
	private static ArrayList<Integer> priorityList = new ArrayList<Integer>();
	private static ArrayList<Boolean> singleEventList = new ArrayList<Boolean>();
	private static ArrayList<Boolean> deleteFlag = new ArrayList<Boolean>();
	private static String referenceName = "EventManager";
	private static IPSCounter ipsCounter = new IPSCounter();
	
	public EventManager()
	{
		//EventManager.referenceName = this.toString();
		DebugLog.log(4, EventManager.referenceName, "Event manager started.");
		ipsCounter.registerIterable(1);
	}
	
	private static void insertIntoEvents(Event e, int p, boolean single)
	{
		int insertIndex = -1;
		for(int i = 0; i < priorityList.size(); i++)
		{
			if(priorityList.get(i) <= p) insertIndex = i; break;
		}
		if(insertIndex == -1)
		{
			eventList.add(e);
			priorityList.add(p);
			singleEventList.add(single);
			deleteFlag.add(false);
			//System.out.println("Appending event to bottom of list.");
		}
		else
		{
			eventList.add(insertIndex, e);
			priorityList.add(insertIndex, p);
			singleEventList.add(insertIndex, single);
			deleteFlag.add(insertIndex, false);
			//System.out.println("Adding event at index " + insertIndex);
		}
	}
	
	private static void checkForDuplicates(Event e)
	{
		for(int i = 0; i < eventList.size(); i++)
			if(e.equals(eventList.get(i))) DebugLog.log(2, referenceName, "Event ( ^ ) being registered is a duplicate of another event!");
	}
	
	protected static void addContinuousEvent(Event e, int i) {
		DebugLog.log(3, referenceName, "Adding CONTINUOUS event " + e.toString());
		checkForDuplicates(e);
		insertIntoEvents(e, i, false);
	}
	
	protected static void addSingleEvent(Event e, int i) {
		DebugLog.log(4, referenceName, "Adding SINGLE event " + e.toString());
		checkForDuplicates(e);
		insertIntoEvents(e, i, true);
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
		cleanupEvents();
		
		for(int i = 0; i < eventList.size(); i++)
		{
			Event event = eventList.get(i);
			if(event.shouldRun()) event.execute();
			if(!event.shouldRun())
			{
				DebugLog.log(4, referenceName, "Cancelled event " + event.toString() + " being marked for deletion.");
				deleteFlag.set(i, true);
			}
			if(singleEventList.get(i)) 
			{
				DebugLog.log(4, referenceName, "Marking single event " + event.toString() + " for deletion."); 
				deleteFlag.set(i, true);
			}
		}
	}
	
	private static void cleanupEvents()
	{
		int count = 0;
		for(int i = 0; i < deleteFlag.size(); i++)
		{
			if(deleteFlag.get(i))
			{
				eventList.remove(i);
				priorityList.remove(i);
				singleEventList.remove(i);
				deleteFlag.remove(i);
				i--;
				count++;
			}
		}
		if(count != 0) DebugLog.log(4, referenceName, "cleanupEvents removed " + count + " marked events from the queue.");
	}
	
	protected static void removeEvent(Event e)
	{
		int removedEventCount = 0;
		for(int i = 0; i < eventList.size(); i++)
		{
			if(e.equals(eventList.get(i)))
			{
				DebugLog.log(4, referenceName, "Marking event " + eventList.get(i).toString() + " for deletion.");
				deleteFlag.set(i, true);
				removedEventCount++;
			}
		}
		if(removedEventCount == 0) DebugLog.log(2, referenceName, "removeEvent was called but no event was marked for deletion!");
		if(removedEventCount > 1) DebugLog.log(4, referenceName, "removeEvent was called, and " + removedEventCount + " events were marked for deletion.");
	}
	
	public static void dropEvents()
	{
		DebugLog.log(4, referenceName, "Dropped " + eventList.size() + " events.");
		eventList.clear();
		priorityList.clear();
		singleEventList.clear();
		deleteFlag.clear();
	}
	
	public static void stopIPSCounter()
	{
		EventManager.ipsCounter.cancelEvent();
	}
}
