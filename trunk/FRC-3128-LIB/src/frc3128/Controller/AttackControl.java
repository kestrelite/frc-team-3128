package frc3128.Controller;

import edu.wpi.first.wpilibj.Joystick;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class AttackControl extends Event {
    public double x, y, throttle;
	public Joystick aControl;
    private final int controlID;
    private boolean[] buttonsPressed = {false, false, false, false, false, false, false, false, false, false, false};
	
	/**
	 * Instantiates a new AttackPad controller and starts an Event for updates.
	 * 
	 * @param port The port of the controller
	 */
    public AttackControl(int port) {
        aControl = new Joystick(port);
        controlID = port;
        this.registerIterableEvent();
        DebugLog.log(4, this, "AttackControl added self to event manager!");
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
        
        if(updateJoy) ListenerManager.callListener("updateAtkJoy");
        if(updateThrottle) ListenerManager.callListener("updateAtkThrottle");
        if(updateJoy || updateThrottle) ListenerManager.callListener("updateDrive");
        
        for(int i = 1; i < 11; i++) {
            if(buttonsPressed[i] != aControl.getRawButton(i)) {
                ListenerManager.callListener("button" + this.controlID + (i) + (aControl.getRawButton(i) ? "Down" : "Up"));
                DebugLog.log(4, this, "Button " + this.controlID + (i) + (aControl.getRawButton(i)==true?" pressed.":" released."));
            }
            buttonsPressed[i] = aControl.getRawButton(i);
        }
    }
}
