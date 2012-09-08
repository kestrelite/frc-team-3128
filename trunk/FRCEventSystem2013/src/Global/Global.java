package Global;
import DebugLog.DebugLog;
import EventManager.Event;
import EventManager.EventManager;
import ListenerManager.*;

class TestEvent1 extends Event
{
	public void execute() {
		DebugLog.log(3, referenceName, "Hello!");
	}
}

class ExitProgramTimer extends Event
{
	public void execute() {
		DebugLog.log(3, referenceName, "Shutdown enabled.");
		Global.shutdown = true;
	}
}

class HelloListenerTrigger extends Event
{
	public void execute() {
		ListenerManager.callListener("sayHello");
	}
}

public class Global {
	public final static String referenceName = "Global";
	public final static Event testEvent1 = new TestEvent1();
	public final static Event exitProgramTimer = new ExitProgramTimer();
	public final static Event helloListenerTrigger = new HelloListenerTrigger();
	public       static boolean shutdown = false;
	
	public Global() {}

	public static long getSystemTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	public static void initialize()
	{
		new DebugLog(4);
		DebugLog.log(4, referenceName, "Initializing Event Manager...");
		new EventManager();
		
		testEvent1.registerEvent();
		
		ListenerManager.addListener(testEvent1, "sayHello");
		
		helloListenerTrigger.prepareTimer();
		exitProgramTimer.prepareTimer();
		
		exitProgramTimer.registerTimedEvent(20000);
		helloListenerTrigger.registerTimedEvent(3000);
	}
}
