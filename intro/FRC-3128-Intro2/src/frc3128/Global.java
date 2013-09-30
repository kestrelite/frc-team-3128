package frc3128;

import edu.wpi.first.wpilibj.Watchdog;

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
    public static void robotStop() {Watchdog.getInstance().kill();}

    private Global() {}
}