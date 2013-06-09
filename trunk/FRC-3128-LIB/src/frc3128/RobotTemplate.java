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

    boolean autonomousHasBeenInit = false;
    public void autonomousInit() {
        if(!autonomousHasBeenInit) {
            Global.initializeAuto(); 
            autonomousHasBeenInit = true; 
            teleopHasBeenInit = false;
        }
    }
    
    boolean teleopHasBeenInit = false;
    public void teleopInit() {
        if(!teleopHasBeenInit) {
            Global.initializeTeleop(); 
            teleopHasBeenInit = true; 
            autonomousHasBeenInit = false;
        }
    }
    
    public void disabledPeriodic() {        
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }

    public void autonomousPeriodic() {
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }

    public void teleopPeriodic() {
        Watchdog.getInstance().feed();
        EventManager.processEvents();
    }
}