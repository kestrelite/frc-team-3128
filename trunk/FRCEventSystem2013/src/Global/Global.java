package Global;
import DebugLog.DebugLog;
import EventManager.Event;
import EventManager.EventManager;

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

public class Global {
	public final static String referenceName = "Global.Global@stref01";
	public final static Event TestEvent1 = new TestEvent1();
	public       static boolean shutdown = false;
	
	public Global() {}

	public static long getSystemTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	public static void initialize()
	{
		new DebugLog(4);
		DebugLog.log(4, "Global.Global@stref01", "Initializing Event Manager...");
		new EventManager();
		//EventManager.addSingleEvent(TestEvent1);
		//EventManager.removeEvent(TestEvent1);
		
		ExitProgramTimer exitProgramTimer = new ExitProgramTimer();
		exitProgramTimer.prepareTimer();
		exitProgramTimer.registerTimedEvent(20000);
	}
}
