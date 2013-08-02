package frc3128;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class Global {
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {}

	public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {EventManager.dropAllEvents(); ListenerManager.dropAllListeners();}
	
	private Global() {
		MotorLink testLink = new MotorLink(new Jaguar(1,2));
		testLink.setEncoder(new MagneticPotEncoder(1, 2));
		testLink.setSpeedControl(new LinearAngleTarget(0.1));
		testLink.setSpeedControlTarget(50);
	}
}