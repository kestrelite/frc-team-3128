package util.Controller;

public class ButtonMap {

    public static final int A = 1;
    public static final int B = 2;
    public static final int X = 3;
    public static final int Y = 4;
    public static final int LB = 5;
    public static final int RB = 6;
    public static final int back = 7;
    public static final int start = 8;
    public static final int L3 = 9;
    public static final int R3 = 10;
    public static final int X1 = 1;
    public static final int Y1 = 2;
    public static final int TR = 3;
    public static final int X2 = 4;
    public static final int Y2 = 5;
    
    public String getBtnString(int btn) {
        switch (btn) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "X";
            case 4:
                return "Y";
            case 5:
                return "LB";
            case 6:
                return "RB";
            case 7:
                return "back";
            case 8:
                return "start";
            case 9:
                return "L3";
            case 10:
                return "R3";
            default:
                return "DNE";
        }
    }
}