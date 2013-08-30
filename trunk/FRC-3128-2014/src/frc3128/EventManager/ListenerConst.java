package frc3128.EventManager;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class ListenerConst {
	public static final String BTN_A_DOWN = "buttonADown";
	public static final String BTN_B_DOWN = "buttonBDown";
	public static final String BTN_X_DOWN = "buttonXDown";
	public static final String BTN_Y_DOWN = "buttonYDown";
	public static final String BTN_LB_DOWN = "buttonLBDown";
	public static final String BTN_RB_DOWN = "buttonRBDown";
	public static final String BTN_START_DOWN = "buttonStartDown";
	public static final String BTN_BACK_DOWN = "buttonBackDown";
	public static final String BTN_L3_DOWN = "buttonL3Down";
	public static final String BTN_R3_DOWN = "buttonR3Down";
	public static final String BTN_A_UP = "buttonAUp";
	public static final String BTN_B_UP = "buttonBUp";
	public static final String BTN_X_UP = "buttonXUp";
	public static final String BTN_Y_UP = "buttonYUp";
	public static final String BTN_LB_UP = "buttonLBUp";
	public static final String BTN_RB_UP = "buttonRBUp";
	public static final String BTN_START_UP = "buttonStartUp";
	public static final String BTN_BACK_UP = "buttonBackUp";
	public static final String BTN_L3_UP = "buttonL3Up";
	public static final String BTN_R3_UP = "buttonR3Up";

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
	
	private ListenerConst() {}
}
