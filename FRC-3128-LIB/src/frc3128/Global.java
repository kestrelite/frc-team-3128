package frc3128;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.PneumaticsManager.PneumaticsManager;

public class Global {
    public static void initializeRobot() {
        PneumaticsManager.setCompressorStateOn();
        DebugLog.setLogLevel(3);
        DebugLog.log(3, Global.class, "ROBOT INITIALIZATION COMPLETE");
    }

    public static void initializeDisabled() {
        PneumaticsManager.setCompressorStateOn();
    }

    public static void initializeAuto() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
	}
	
    public static void initializeTeleop() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
    }

    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Watchdog.getInstance().kill();
    }
    
    public static void robotStop() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
    }
    
    public static double getAngleFrom(AnalogChannel c) {
        double voltage = 0, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += c.getVoltage();
            value += c.getValue();
        }
        voltage /= 10; value /= 10;
        return voltage/5.0*360;
    }
    
    public static double signum(double a) {
        if(a == 0) return 0;
        return Math.abs(a)/a;
    }
	
	private Global() {}
}