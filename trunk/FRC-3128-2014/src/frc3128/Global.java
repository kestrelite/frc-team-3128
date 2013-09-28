package frc3128;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerConst;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Motor.MotorLink;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class Global {
    public static XControl xControl1 = new XControl(1);
    public static MotorLink mLeft = new MotorLink(new Jaguar(1,1));
    public static MotorLink mRight = new MotorLink(new Jaguar(1,2));
    
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.mLeft.setSpeed(Global.xControl1.y1+Global.xControl1.x1);
                Global.mLeft.setSpeed(Global.xControl1.y1-Global.xControl1.x1);
            }
        }, ListenerConst.UPDATE_JOY1);
    }

    public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {EventManager.dropAllEvents(); ListenerManager.dropAllListeners();}

    private Global() {}
}