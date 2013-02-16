package frc3128;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initializeRobot();
    }
    
    public void disabledInit() {
        Global.initializeDisabled();
    }
    
    public void disabledPeriodic() {
        Watchdog.getInstance().feed();
    }
    
    public void disabledContinuous() {
        EventManager.processEvents();
    }
    
    public void autonomousInit() {
        Global.robotReset();
        Global.initializeAuto();
    }
    
    public void autonomousPeriodic() {
        Watchdog.getInstance().feed();
    }
    
    public void autonomousContinuous() {
        EventManager.processEvents();
    }
    
    public void teleopInit() {
        Global.initializeTeleop();
    }
    
    public void teleopPeriodic() {
        Watchdog.getInstance().feed();        
    }
    
    public void teleopContinuous() {
        EventManager.processEvents();
    }
}
