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
public class AttackControl extends Event {
    public  double    x, y, throttle;
    public  Joystick  aControl;
    private final int controlID;
    private boolean[] buttonsPressed = {false, false, false, false, false, false, false, false, false, false, false};
    
    /**
     * Instantiates a new AttackPad controller and starts an Event for updates.
     * 
     * @param port the port of the controller
     */
    public AttackControl(int port) {
        aControl = new Joystick(port);
        controlID = port;
        this.registerIterableEvent();
        DebugLog.log(DebugLog.LVL_DEBUG, this, "AttackControl added self to event manager!");
    }
    
    /**
     * This function will be called automatically by the EventManager.<p><b>Do
     * not invoke it.</b>
     */
    public void execute() {
        boolean updateJoy = false, updateThrottle = false;
        
        if(x != aControl.getAxis(Joystick.AxisType.kX)) updateJoy = true;
        if(y != aControl.getAxis(Joystick.AxisType.kY)) updateJoy = true;
        if(throttle != aControl.getAxis(Joystick.AxisType.kThrottle)) updateThrottle = true;
        
        x = aControl.getAxis(Joystick.AxisType.kX);
        y = aControl.getAxis(Joystick.AxisType.kY);
        throttle = aControl.getAxis(Joystick.AxisType.kThrottle);
        
        if(updateJoy) ListenerManager.callListener(ListenerConst.UPDATE_ATK_JOY);
        if(updateThrottle) ListenerManager.callListener(ListenerConst.UPDATE_ATK_THROTTLE);
        if(updateJoy || updateThrottle) ListenerManager.callListener(ListenerConst.UPDATE_DRIVE);
        
        for(int i = 1; i < 11; i++) {
            if(buttonsPressed[i] != aControl.getRawButton(i)) {
                ListenerManager.callListener(ListenerConst.getAtkCtrlListenerKey(this.controlID, i, aControl.getRawButton(i)));
                DebugLog.log(DebugLog.LVL_STREAM, this, "Button " + (this.controlID + "-" + i) + (aControl.getRawButton(i)==true?" pressed.":" released."));
            }
            buttonsPressed[i] = aControl.getRawButton(i);
        }
    }
}
