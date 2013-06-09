package frc3128;

public class DebugLog {
    public static final int LVL_STREAM = 5;
    public static final int LVL_DEBUG = 4;
    public static final int LVL_INFO = 3;
    public static final int LVL_WARN = 2;
    public static final int LVL_SEVERE = 1;
    public static final int LVL_ERROR = 0;
    
    private static int logDetail = 2;
    private static int maxTagLength = 0;
    private static final int initTagLength = 32;

    public DebugLog() {DebugLog.log(4, this.toString(), "Debug log started.");}
    public DebugLog(int p) {
        logDetail = p;
        DebugLog.log(4, this.toString(), "Debug log started.");
    }

    public static void setLogLevel(int detail) {
        DebugLog.log(3, "DebugLog", "Log detail set to " + detail);
        DebugLog.logDetail = detail;
    }

    public static void log(int lvl, Object o, String text) {
        String level = "[UNKN]  ";
        if(lvl <= 0) level = "[ERROR" + Math.abs(lvl) + "] ";
        if(lvl == 1) level = "[SEVERE]";
        if(lvl == 2) level = "[**WARN]";
        if(lvl == 3) level = "[INFO]  ";
        if(lvl == 4) level = "[DEBUG] ";
        if(lvl == 5) level = "[STREAM]";
        
        if(o.toString().length() > DebugLog.maxTagLength && lvl <= DebugLog.logDetail)
            DebugLog.maxTagLength = o.toString().length();
        
        if(lvl <= DebugLog.logDetail) {
            System.out.print("[" + System.currentTimeMillis() + "] " + 
                    level + " [" + 
                    (o.toString().substring(0, 4).equals("edu.")?o.toString().substring(initTagLength):o.toString()) + "] ");
            for(int i = 0; i < maxTagLength - o.toString().length(); i++)
                System.out.print(" ");
            System.out.println(text);
        }
    }
}