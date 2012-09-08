package ListenerManager;

import java.util.ArrayList;

import DebugLog.DebugLog;
import EventManager.Event;

public class ListenerManager {
	static ArrayList<Event> event = new ArrayList<Event>();
	static ArrayList<Integer> trigger = new ArrayList<Integer>();
	static String referenceName = "ListenerManager";
	
	public static void addListener(Event e, String link)
	{
		addListener(e, link.hashCode());
	}
	
	private static void verifyNoDuplicate(Event e, int link)
	{
		for(int i = 0; i < event.size(); i++)
			if(event.get(i) == e) 
				if(ListenerManager.trigger.get(i) == link) 
					DebugLog.log(2, referenceName, "Duplicate event/trigger pair was added to the ListenerManager!");
	}
	
	public static void addListener(Event e, int link)
	{
		verifyNoDuplicate(e, link);
		ListenerManager.event.add(e);
		ListenerManager.trigger.add(link);
	}
	
	public static void callListener(String link)
	{
		callListener(link.hashCode());
	}
	
	public static void callListener(int link)
	{
		DebugLog.log(4, referenceName, "Listener trigger has been called: " + link);
		
		for(int i = 0; i < ListenerManager.trigger.size(); i++)
			if(ListenerManager.trigger.get(i) == link) 
			{
				DebugLog.log(4, referenceName, "Listener link " + link + " triggered Event " + event.get(i).toString());
				ListenerManager.event.get(i).execute();
			}
	}
	
	public static void dropListeners()
	{
		ListenerManager.event.clear();
		ListenerManager.trigger.clear();
	}
}
