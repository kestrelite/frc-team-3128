package frc3128.Controller;

import edu.wpi.first.wpilibj.Joystick;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.ListenerManager.ListenerManager;

public class XControl extends Event {
    private Joystick xControl;
    public double x1, y1, x2, y2, triggers;
    private boolean[] buttonsPressed = {false, false, false, false, false, false, false, false, false, false, false};
    
    public XControl(int port) {
        super();
        xControl = new Joystick(port);
        this.registerIterableEvent();
        DebugLog.log(4, referenceName, "Controller added self to event manager!");
    }
    
    public void execute() {
        try {
            boolean updateJoy1 = false;
            boolean updateJoy2 = false;
            boolean updateTriggers = false;

            if(x1 != xControl.getRawAxis(XControlMap.x1Axis))       updateJoy1 = true;
            if(x2 != xControl.getRawAxis(XControlMap.x2Axis))       updateJoy2 = true;
            if(y1 != xControl.getRawAxis(XControlMap.y1Axis))       updateJoy1 = true;
            if(y2 != xControl.getRawAxis(XControlMap.y2Axis))       updateJoy2 = true;
            if(triggers != xControl.getRawAxis(XControlMap.trAxis)) updateTriggers = true;

            x1 = xControl.getRawAxis(XControlMap.x1Axis);
            x2 = xControl.getRawAxis(XControlMap.x2Axis);
            y1 = xControl.getRawAxis(XControlMap.y1Axis);
            y2 = xControl.getRawAxis(XControlMap.y2Axis);

            if(updateJoy1) ListenerManager.callListener("updateJoy1");
            if(updateJoy2) ListenerManager.callListener("updateJoy2");
            if(updateTriggers) ListenerManager.callListener("updateTriggers");
            if(updateJoy1 || updateJoy2 || updateTriggers) ListenerManager.callListener("updateDrive");

            for(int i = 1; i < 11; i++) {
                if(buttonsPressed[i] != xControl.getRawButton(i))
                    ListenerManager.callListener("button" + XControlMap.getBtnString(i) + (xControl.getRawButton(i) ? "Down" : "Up"));
                buttonsPressed[i] = xControl.getRawButton(i);
            }
        } catch(NullPointerException e) {
            DebugLog.log(1, this.toString(), "Event was executed before controller was bound! Cancelling this event!");
            this.cancelEvent();
        }
    }

    //public void bindToController(int port) {
    //    xControl = new Joystick(port);
    //}
}
