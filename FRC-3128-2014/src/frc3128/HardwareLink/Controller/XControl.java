package frc3128.HardwareLink.Controller;

import edu.wpi.first.wpilibj.Joystick;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerConst;
import frc3128.EventManager.ListenerManager;
import frc3128.Util.DebugLog;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class XControl extends Event {
    private Joystick xControl;
    private final int port;
    public double x1, y1, x2, y2, triggers;
    private boolean[] buttonsPressed = {false, false, false, false, false, false, false, false, false, false, false};
    
    /**
     * Instantiates a new XBox controller and starts an Event for updates.
     * 
     * @param port the port of the controller
     */
    public XControl(int port) {
        this.port = port;
        xControl = new Joystick(port);
        this.registerIterableEvent();
        DebugLog.log(DebugLog.LVL_DEBUG, this, "XBox Controller added self to event manager!");
    }

    /**
     * This function will be called automatically by the EventManager.<p><b>Do
     * not invoke it.</b>
     */
    public void execute() {
        boolean updateJoy1 = false;
        boolean updateJoy2 = false;
        boolean updateTriggers = false;

        if(x1 != xControl.getRawAxis(XControlMap.X1_AXIS)) updateJoy1 = true;
        if(x2 != xControl.getRawAxis(XControlMap.X2_AXIS)) updateJoy2 = true;
        if(y1 != xControl.getRawAxis(XControlMap.Y1_AXIS)) updateJoy1 = true;
        if(y2 != xControl.getRawAxis(XControlMap.Y2_AXIS)) updateJoy2 = true;
        if(triggers != xControl.getRawAxis(XControlMap.TRIGGER_AXIS)) updateTriggers = true;

        x1 = xControl.getRawAxis(XControlMap.X1_AXIS);
        x2 = xControl.getRawAxis(XControlMap.X2_AXIS);
        y1 = xControl.getRawAxis(XControlMap.Y1_AXIS);
        y2 = xControl.getRawAxis(XControlMap.Y2_AXIS);

        if(updateJoy1) ListenerManager.callListener("updateJoy1");
        if(updateJoy2) ListenerManager.callListener("updateJoy2");
        if(updateTriggers) ListenerManager.callListener("updateTriggers");
        if(updateJoy1 || updateJoy2 || updateTriggers) ListenerManager.callListener("updateDrive");

        for(int i = 1; i < 11; i++) {
            if(buttonsPressed[i] != xControl.getRawButton(i)) {
                ListenerManager.callListener(ListenerConst.getXCtrlListenerKey(port, i, (xControl.getRawButton(i) ? true : false)));
                DebugLog.log(DebugLog.LVL_STREAM, this, "Button " + XControlMap.getBtnString(i) + (xControl.getRawButton(i)==true?" pressed.":" released."));
            }
            buttonsPressed[i] = xControl.getRawButton(i);
        }
    }
}