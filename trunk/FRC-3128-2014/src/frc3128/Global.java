package frc3128;

import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.RbtCommandController;

import frc3128.Util.Connection.Beaglebone;
import frc3128.Util.DebugLog;

public class Global {
    public static RbtCommandController rCont;
    public static void initializeRobot() {
        DebugLog.setLogLevel(DebugLog.LVL_DEBUG);
        rCont = new RbtCommandController();
    }

    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 38");
            }
        }, "rbtCmd-38");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 39");
            }
        }, "rbtCmd-39");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 40");
            }
        }, "rbtCmd-40");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 41");
            }
        }, "rbtCmd-41");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 42");
            }
        }, "rbtCmd-42");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 43");
            }
        }, "rbtCmd-43");
        ListenerManager.addListener(new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "RBT CMD 44");
            }
        }, "rbtCmd-44");
    }

    public static void robotKill() {
        Watchdog.getInstance().kill(); 
    }
    public static void robotStop() {
        EventManager.dropAllEvents();
        ListenerManager.dropAllListeners();
    }
    private Global() {}
}
