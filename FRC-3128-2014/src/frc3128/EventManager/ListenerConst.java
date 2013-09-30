package frc3128.EventManager;

import frc3128.HardwareLink.Controller.XControlMap;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class ListenerConst {
    public static final String UPDATE_JOY1 = "updateJoy1";
    public static final String UPDATE_JOY2 = "updateJoy2";
    public static final String UPDATE_TRIGGERS = "updateTriggers";
    public static final String UPDATE_DRIVE = "updateDrive";
    
    public static final String UPDATE_ATK_JOY = "updateAtkJoy";
    public static final String UPDATE_ATK_THROTTLE = "updateAtkThrottle";
    
    /**
     * Builds a string for the AttackPad controller listeners.
     * 
     * @param controllerPort the port number of the controller
     * @param buttonNum      the button number pressed
     * @param pressed        true if down, false if up
     * @return a listener for ListenerManager which AttackControl will trigger
     */
    public static String getAtkCtrlListenerKey(int controllerPort, int buttonNum, boolean pressed) {
        return "button" + controllerPort + "-" + buttonNum + (pressed?"Down":"Up");
    }
    
    /**
     * Builds a string for the XBox controller listeners.
     * 
     * @param controllerPort the port number of the controller
     * @param buttonNum the button number pressed
     * @param pressed true if down, false if up
     * @return a listener for ListenerManager which XControl will trigger
     */
    public static String getXCtrlListenerKey(int controllerPort, int buttonNum, boolean pressed) {
        return "button" + controllerPort + "-" + XControlMap.getBtnString(buttonNum) + (pressed?"Down":"Up");
    }
    
    private ListenerConst() {}
}
