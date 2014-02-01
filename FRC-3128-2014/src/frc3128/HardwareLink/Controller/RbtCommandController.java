package frc3128.HardwareLink.Controller;

import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.Util.Connection.Beaglebone;
import frc3128.Util.Connection.RobotCommand;
import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 *
 * @author Yousuf Soliman
 */
public class RbtCommandController extends Event {
    private Beaglebone b;
    public RobotCommand Command;
    /**
     * Instantiates a new RobotCommand controller and starts an Event for
     * updates.
     * <p/>
     * @param port the port of the controller
     */
    public RbtCommandController() {
        b = new Beaglebone();
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
        byte cmd; RobotCommand tmp = b.getCmd(); if(tmp != null) {cmd = tmp._opcode; this.Command = tmp;} else {return;}
        ListenerManager.callListener("rbtCmd-"+cmd);
    }
}