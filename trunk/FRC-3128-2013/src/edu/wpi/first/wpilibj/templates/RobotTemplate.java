package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initialize();
    }
    
    public void disabledInit() {
        
    }
    
    public void autonomousPeriodic() {

    }
    
    public void teleopInit() {
        Global.initializeTeleop();
    }
    
    public void teleopPeriodic() {
        
    }
    
    public void disabledContinuous() {
        EventManager.processEvents();
    }
}
