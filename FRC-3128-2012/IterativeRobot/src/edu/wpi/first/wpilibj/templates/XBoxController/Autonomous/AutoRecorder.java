package edu.wpi.first.wpilibj.templates.XBoxController.Autonomous;

import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;

public class AutoRecorder implements EventInterface {

    private static double y1, y2, x1, x2, triggers;
    private static boolean[] buttons;
    private boolean hasBeenInstantiated = false;
    
    public AutoRecorder() {
        if(hasBeenInstantiated) throw new Error("One AutoRecorder per class, please.");
        System.out.println("{");
    }

    public static void update(double y1, double y2, double x1, double x2, double triggers, boolean[] buttons) {
        AutoRecorder.y1 = y1;
        AutoRecorder.y2 = y2;
        AutoRecorder.x1 = x1;
        AutoRecorder.x2 = x2;
        AutoRecorder.triggers = triggers;
        AutoRecorder.buttons = buttons;
    }

    public boolean eventIterable() throws Exception {
        System.out.print("{" + AutoRecorder.x1 + ", " + AutoRecorder.y1 + ", " 
                + AutoRecorder.triggers + ", " + AutoRecorder.x2 + ", " + AutoRecorder.y2);
        for (int i = 0; i < AutoRecorder.buttons.length; i++) {
            if (AutoRecorder.buttons[i]) {
                System.out.print(", " + 1.0);
            }
            if (!AutoRecorder.buttons[i]) {
                System.out.print(", " + 0.0);
            }
        }
        System.out.println("},");
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
}
