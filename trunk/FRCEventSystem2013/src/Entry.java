import DebugLog.DebugLog;
import EventManager.*;
import Global.Global;

public class Entry {
	public static void main(String[] args)
	{
		Global.initialize();
		DebugLog.log(3, "Main", "Program started.");
		while(!Global.shutdown)
			EventManager.processEvents();
		DebugLog.log(3, "Main", "Program exit successful.");
	}
}
