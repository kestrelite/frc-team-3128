package frc3128;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Watchdog;

public class Global {
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {}
    public static void initializeTeleop() {}

	public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {}
	
	//TODO: Move the following functions to their appropriate mathematics libraries
    public static double getAngleFrom(AnalogChannel c) {
        double voltage = 0, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += c.getVoltage();
            value += c.getValue();
        }
        voltage /= 10; value /= 10;
        return voltage/5.0*360;
    }

	private Global() {}
}