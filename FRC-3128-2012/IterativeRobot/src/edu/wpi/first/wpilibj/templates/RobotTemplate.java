package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;

public class RobotTemplate extends IterativeRobot {
    
    public boolean instantiated = false;
    public final String driveIsSwerve = "dSWERVE";
    public final String driveIsTank = "dTANK";
    public final String driveIsReset = "dRESET";
    public final String driveIsEmpty = "dEMPTY";
    public final String driveIsAuto = "dAUTO";
    
    public final boolean pneumaticsDisabled = false;
    public final boolean pneumaticsEnabled = true;
    
    public void disabledInit() {
        if (instantiated) {
            g.xControl.reset();
        } else {
            try {
                g.instantiate(driveIsTank, pneumaticsEnabled);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            instantiated = true;
        }
    }
    
    public void disabledContinuous()
    {
        //g.mLBTurn.setTargetAngle(0);
        //g.mRBTurn.setTargetAngle(0);
        //g.mRFTurn.setTargetAngle(0);
        //g.mLFTurn.setTargetAngle(0);
        //try {
        //    EventManager.runNextIteration();
        //} catch (Exception ex) {
        //    ex.printStackTrace();
        //}
    }

    public void teleopInit() {
        try {
            //EventManager.eraseAllEvents();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void autonomousPeriodic() {
    }

    public void teleopContinuous() {
        try {
            EventManager.runNextIteration();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Error("Uncaught exception!");
        }
    }
}
