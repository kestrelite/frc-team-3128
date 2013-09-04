package frc3128.HardwareLink.Controller;

import frc3128.Util.DebugLog;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class XControlMap {
    public static final int A = 1; 
    public static final int B = 2; 
    public static final int X = 3; 
    public static final int Y = 4;
    public static final int LB = 5;
    public static final int RB = 6;
    public static final int BACK = 7;
    public static final int START = 8;
    public static final int L3 = 9;
    public static final int R3 = 10;
    public static final int X1_AXIS = 1;
    public static final int Y1_AXIS = 2;
    public static final int TRIGGER_AXIS = 3;
    public static final int X2_AXIS = 4;
    public static final int Y2_AXIS = 5;

    /**
     * Gets the String associated with a button keypress. Used primarily for 
     * Listener triggers, but also useful for debug outputs.
     * 
     * @param btn the integer key pressed
     * @return the associated String
     */
    public static String getBtnString(int btn) {
        switch(btn) {
        case 1:  return "A";
        case 2:  return "B";
        case 3:  return "X";
        case 4:  return "Y";
        case 5:  return "LB";
        case 6:  return "RB";
        case 7:  return "Back";
        case 8:  return "Start";
        case 9:  return "L3";
        case 10: return "R3";
        default: DebugLog.log(DebugLog.LVL_WARN, "ButtonMap", "A button argument was passed to ButtonMap that does not exist!");
            return "";
        }
    }
    
    private XControlMap() {}
}