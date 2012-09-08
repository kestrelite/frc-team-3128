package Global;
import DebugLog.DebugLog;
import EventManager.Event;
import EventManager.EventManager;
import ListenerManager.*;

class TestEvent1 extends Event
{
	public void execute() {
		DebugLog.log(3, this.toString(), "Hello!");
	}
}

class ExitProgramTimer extends Event
{
	public void execute() {
		DebugLog.log(3, this.toString(), "Shutdown enabled.");
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
		new DebugLog(3);
		DebugLog.log(4, referenceName, "Initializing Event Manager...");
		new EventManager();
		//EventManager.addSingleEvent(TestEvent1);
		//EventManager.removeEvent(TestEvent1);
		
		ListenerManager.addListener(testEvent1, "sayHello");
		
		helloListenerTrigger.prepareTimer();
		exitProgramTimer.prepareTimer();
		
		exitProgramTimer.registerTimedEvent(5000);
		helloListenerTrigger.registerTimedEvent(3000);
	}
}
