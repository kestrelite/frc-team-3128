package frc3128;

public class DebugLog {
    private static int logDetail = 2;
    private static int maxTagLength = 0;
    private static final int initTagLength = 32;

    public DebugLog() {
        DebugLog.log(4, this.toString(), "Debug log started.");
    }

    public DebugLog(int p) {
        logDetail = p;
        DebugLog.log(4, this.toString(), "Debug log started.");
    }

    public static void setLogLevel(int detail) {
        DebugLog.logDetail = detail;
    }

    public static void log(int lvl, String tag, String text) {
        String level = "[UNKN]  ";
        if(lvl <= 0) level = "[ERROR" + Math.abs(lvl) + "] ";
        if(lvl == 1) level = "[SEVERE]";
        if(lvl == 2) level = "[**WARN]";
        if(lvl == 3) level = "[INFO]  ";
        if(lvl == 4) level = "[DEBUG] ";
        if(lvl == 5) level = "[STREAM]";
        
        if(tag.length() > DebugLog.maxTagLength && lvl <= DebugLog.logDetail)
            DebugLog.maxTagLength = tag.length();
        
        if(lvl <= DebugLog.logDetail) {
            System.out.print("[" + System.currentTimeMillis() + "] " + 
                    level + " [" + 
                    (tag.substring(0, 4).equals("edu.")?tag.substring(initTagLength):tag) + "] ");
            for(int i = 0; i < maxTagLength - tag.length(); i++)
                System.out.print(" ");
            System.out.println(text);
        }
    }
}
