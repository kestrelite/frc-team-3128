/*package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

public class FRC3128 extends IterativeRobot {
    public void robotInit() {
        Global.initialize();
    }

    public void autonomousInit() {
        DebugLog.log(3, this.toString(), "Autonomous init");
        Global.initializeAuto();
    }

    public void autonomousContinuous() {
        EventManager.processEvents();
    }

    public void teleopInit() {
        DebugLog.log(3, this.toString(), "Teleop init");
        Global.initializeTeleop();
    }

    public void teleopContinuous() {
        EventManager.processEvents();
    }
}*/