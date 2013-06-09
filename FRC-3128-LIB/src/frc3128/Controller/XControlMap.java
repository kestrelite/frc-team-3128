package frc3128.Controller;

import frc3128.DebugLog;

public class XControlMap {
    public static final int A = 1; 
    public static final int B = 2; 
    public static final int X = 3; 
    public static final int Y = 4;
    public static final int LB = 5;
    public static final int RB = 6;
    public static final int Back = 7;
    public static final int Start = 8;
    public static final int L3 = 9;
    public static final int R3 = 10;
    public static final int x1Axis = 1;
    public static final int y1Axis = 2;
    public static final int trAxis = 3;
    public static final int x2Axis = 4;
    public static final int y2Axis = 5;

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
        default: DebugLog.log(1, "ButtonMap", "A button argument was passed to ButtonMap that does not exist!");
            return "";
        }
    }
}