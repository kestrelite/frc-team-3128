package frc3128;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Motor.MotorLink;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class Global {
    public static MotorLink motor = new MotorLink(new Jaguar(1,1));
    
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {
        motor.setSpeed(0.2);
    }

    public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {EventManager.dropAllEvents(); ListenerManager.dropAllListeners();}

    private Global() {}
}