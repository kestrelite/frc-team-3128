package DebugLog;

public class DebugLog {
	static int logDetail = 2;
	public DebugLog() {DebugLog.log(4, this.toString(), "Debug log started.");}
	public DebugLog(int p) {logDetail = p; DebugLog.log(4, this.toString(), "Debug log started.");} 

	public static void setLogLevel(int detail)
	{
		DebugLog.logDetail = detail;
	}
	
	public static void log(int i, String tag, String text) {
		String level = "Unknown";
		if(i <= 0) level = "ERROR";
		if(i == 1) level = "**SEVERE";
		if(i == 2) level = "**WARN";
		if(i == 3) level = "INFO";
		if(i >= 4) level = "DEBUG";
		
		if(i <= DebugLog.logDetail) System.out.println("[" + System.currentTimeMillis() + "] [" + level + "] [" + tag + "] " + text);
	}
}
