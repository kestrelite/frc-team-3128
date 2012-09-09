package edu.wpi.first.wpilibj.templates.Controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.DebugLog;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.ListenerManager.ListenerManager;

public class Controller extends Event {
    private Joystick xControl;
    private boolean controllerBound = false;
    public double x1, y1, x2, y2, triggers;
    private boolean[] buttonsPressed = {false, false, false, false, false, false, false, false, false, false};

    public void execute() {
        if(!controllerBound) {
            DebugLog.log(2, this.toString(), "Controller checks called before controller bind! Ignoring controller checks.");
            return;
        }

        try {
            boolean updateJoy1 = false;
            boolean updateJoy2 = false;
            boolean updateTriggers = false;

            if(x1 != xControl.getRawAxis(1))
                updateJoy1 = true;
            if(x2 != xControl.getRawAxis(4))
                updateJoy2 = true;
            if(y1 != xControl.getRawAxis(2))
                updateJoy1 = true;
            if(y2 != xControl.getRawAxis(5))
                updateJoy2 = true;
            if(triggers != xControl.getRawAxis(3))
                updateTriggers = true;

            x1 = xControl.getRawAxis(1);
            x2 = xControl.getRawAxis(4);
            y1 = xControl.getRawAxis(2);
            y2 = xControl.getRawAxis(5);

            if(updateJoy1)
                ListenerManager.callListener("updateJoy1");
            if(updateJoy2)
                ListenerManager.callListener("updateJoy2");
            if(updateTriggers)
                ListenerManager.callListener("updateTriggers");
            if(updateJoy1 || updateJoy2 || updateTriggers)
                ListenerManager.callListener("updateDrive");

            for(int i = 1; i <= 10; i++) {
                if(buttonsPressed[i] != xControl.getRawButton(i))
                    ListenerManager.callListener("button" + ButtonMap.getBtnString(i) + (xControl.getRawButton(i) ? "Down" : "Up"));
                buttonsPressed[i] = xControl.getRawButton(i);
            }
        } catch(NullPointerException e) {
            DebugLog.log(1, this.toString(), "Event was executed before controller was bound! Cancelling this event!");
            this.cancelEvent();
        }
    }

    public void bindToController(int port) {
        xControl = new Joystick(port);
    }
}
