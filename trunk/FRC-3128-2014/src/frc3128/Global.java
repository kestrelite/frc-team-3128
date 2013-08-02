package frc3128;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.MotorSpeedControl;
import frc3128.Util.RobotMath;

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
		testLink.setSpeedControl(new MotorSpeedControl() {
			private double tgtAngle = 0;
			public void setControlTarget(double d) {tgtAngle = d;}

			public double speedTimestep(double dt) {
				int dir = RobotMath.getMotorDirToTarget(this.getLinkedEncoderAngle(), tgtAngle);
			    double pow = (Math.abs(RobotMath.normalizeAngle(Math.abs(this.getLinkedEncoderAngle()- tgtAngle)) - 180)/180.0)*(0.9/1.0);
				return ((pow + 0.1)*dir);
			}

			public void clearControlRun() {}
		});
	}
}