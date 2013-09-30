package frc3128.Util;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class DebugLog {
    public static final int LVL_STREAM = 5;
    public static final int LVL_DEBUG = 4;
    public static final int LVL_INFO = 3;
    public static final int LVL_WARN = 2;
    public static final int LVL_SEVERE = 1;
    public static final int LVL_ERROR = 0;
    private static int logDetail = Constants.DEBUGLOG_DEFAULT_LOGLEVEL;
    private static int maxTagLength = 0;
    private static final int initTagLength = 32;
    private static int skippedItems = 0;
    private static int totalItems = 0;
    private static int[] totalItemsType = new int[6];
    private static double wastedTimeAll = 0;
    private static double wastedTimeText = 0;
    private static double startTime = -1;

    /**
     * This sets the log detail level. This is used to filter based on severity.
     * Severity is the following: <p>0: Error; 1: Severe; 2: Warning; 3: Info;
     * 4: Debug; 5: Stream
     *
     * @param level the level to be logged.
     */
    public static void setLogLevel(int level) {
        DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "Log detail set to " + level);
        DebugLog.logDetail = level;
    }

    /**
     * Logs a message to the NetBeans console. The log keeps all statements in
     * alignment with the longest message. It will hide any information whose
     * log level is higher than the provided maximum detail. It uses an object
     * (typically a string) to create a tag for the message.
     *
     * @param level the severity level of the message to be logged.
     * @param obj the object (typically String) used to identify the message
     * source.
     * @param text the message to be logged.
     */
    public static void log(int level, Object obj, String text) {
        if(!Constants.DEBUGLOG_ENABLED) return;
        
        long logStartTime = System.currentTimeMillis(); //Diagnostic

        String strLv = "  [UNKN]";
        if(level <= 0) strLv = "[**ERROR" + Math.abs(level) + "]";
        if(level == 1) strLv = "[SEVERE]";
        if(level == 2) strLv = "  [WARN]";
        if(level == 3) strLv = "  [INFO]";
        if(level == 4) strLv = " [DEBUG]";
        if(level == 5) strLv = "[STREAM]";

        if(obj.toString().length() > DebugLog.maxTagLength && level <= DebugLog.logDetail)
            DebugLog.maxTagLength = obj.toString().length();

        if(level <= DebugLog.logDetail) {
            double textStartTime = System.currentTimeMillis(); //Diagnostic

            System.out.print("[" + System.currentTimeMillis() + "] "
                    + strLv + " ["
                    + (obj.toString().substring(0, 4).equals("edu.") ? obj.toString().substring(initTagLength) : obj.toString()) + "] ");
            for (int i = 0; i < maxTagLength - obj.toString().length(); i++)
                System.out.print(" ");
            System.out.println(text);

            DebugLog.wastedTimeText += (System.currentTimeMillis() - textStartTime); //Diagnostic
        } else DebugLog.skippedItems++; //From this line forward, begin diagnostics
        DebugLog.totalItems++;
        DebugLog.totalItemsType[level]++;

        if (DebugLog.totalItems % Constants.DEBUGLOG_INFO_DISPLAYFREQ == 0) {
            int currLogLevel = DebugLog.logDetail;
            DebugLog.logDetail = 3;
            DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "DebugLog has skipped " + skippedItems + ", has displayed " + (totalItems-skippedItems) + " over " + ((System.currentTimeMillis() - DebugLog.startTime) / 1000.0) + " msec.");
            DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "Breaking down by type (0...5): "
                    + "\n\t\tError: " + DebugLog.totalItemsType[0]
                    + "\n\t\tSevere: " + DebugLog.totalItemsType[1]
                    + "\n\t\tWarning: " + DebugLog.totalItemsType[2]
                    + "\n\t\tInfo: " + DebugLog.totalItemsType[3]
                    + "\n\t\tDebug: " + DebugLog.totalItemsType[4]
                    + "\n\t\tStream: " + DebugLog.totalItemsType[5]);
            DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "DebugLog averages " + (totalItems / ((System.currentTimeMillis() - DebugLog.startTime) / 1000.0)) + " messages per second.");
            DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "DebugLog has wasted an approximate total of " + (DebugLog.wastedTimeAll / 1000.0) + " seconds, " + (DebugLog.wastedTimeText / 1000.0) + " of which was spent rendering text.");
            DebugLog.log(DebugLog.LVL_INFO, "DebugLog", "If this is considerably too high, then please consider removing stream messages.");
            DebugLog.logDetail = currLogLevel;
        }
        DebugLog.wastedTimeAll += (System.currentTimeMillis() - logStartTime);
        //End diagnostic code
    }

    private DebugLog() {}
}
