package frc3128;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc3128.EventManager.EventManager;

public class RobotTemplate extends IterativeRobot {
    public void robotInit() {
        Global.initializeRobot();
    }

    public void disabledInit() {
        Global.initializeDisabled();
    }

    boolean autonomousHasBeenInit = false;
    public void autonomousInit() {
        if(!autonomousHasBeenInit) {Global.initializeAuto(); autonomousHasBeenInit = true;}
    }
    
    boolean teleopHasBeenInit = false;
    public void teleopInit() {
        if(!teleopHasBeenInit) {Global.initializeTeleop(); teleopHasBeenInit = true;}
    }
    
    public void disabledPeriodic() {        
        EventManager.processEvents();
    }

    public void autonomousPeriodic() {
        EventManager.processEvents();
    }

    public void teleopPeriodic() {
        EventManager.processEvents();
    }
}
