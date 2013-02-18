package frc3128;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.ListenerManager.ListenerManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initializeRobot();
    }
   
    public void disabledInit() {
        Global.initializeDisabled();
    }
    
    public void disabledPeriodic() {        
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }
    
    public void autonomousInit() {
        Global.robotReset();
        Global.initializeAuto();
    }
    
    public void autonomousPeriodic() {
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }
    
    public void teleopInit() {
        Global.initializeTeleop();
    }
    
    public void teleopPeriodic() {
        //System.out.println(System.currentTimeMillis());
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }
}
