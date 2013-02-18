package frc3128;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc3128.EventManager.EventManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initializeRobot();
    }

    public void teleopInit() {
        Global.initializeTeleop();
    }
    
    public void autonomousInit() {
        Global.initializeAuto();
    }
    
    public void disabledInit() {
        Global.initializeDisabled();
    }

    public void teleopPeriodic() {
        EventManager.processEvents();
    }
   
    public void autonomousPeriodic() {
        EventManager.processEvents();
    }
    
    public void disabledPeriodic() {        
        EventManager.processEvents();
    }
}
