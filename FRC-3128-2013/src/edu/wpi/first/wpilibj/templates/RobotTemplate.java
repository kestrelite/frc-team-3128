package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initializeRobot();
    }
    
    public void disabledInit() {
        
    }
    
    public void disabledPeriodic() {
        
    }
    
    public void disabledContinuous() {
        EventManager.processEvents();
    }
    
    public void autonomousInit() {
        Global.initializeAuto();
    }
    
    public void autonomousPeriodic() {

    }
    
    public void autonomousContinuous() {
        EventManager.processEvents();
    }
    
    public void teleopInit() {
        Global.initializeTeleop();
    }
    
    public void teleopPeriodic() {
        
    }
    
    public void teleopContinuous() {
        EventManager.processEvents();
    }
}
