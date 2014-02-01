package frc3128.HardwareLink.Controller;

import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Util.DebugLog;

/**
 *
 * @author Yousuf Soliman
 */
public class RbtCommandController extends Event {
    
    /**
     * Instantiates a new RobotCommand controller and starts an Event for updates.
     * <p/>
     * @param port the port of the controller
     */
    public RbtCommandController(int port) {
        this.registerIterableEvent();
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Robot Command Controller added self to event manager!");
    }
    
    public String getButtonKey(String cmd) {
        return "rbtCmd" + "-" + cmd;
    }
    /**
     * This function will be called automatically by the EventManager.<p><b>Do
     * not invoke it.</b>
     */
    public void execute() {
        String lstKey = "";
        ListenerManager.callListener(lstKey);
    }
}