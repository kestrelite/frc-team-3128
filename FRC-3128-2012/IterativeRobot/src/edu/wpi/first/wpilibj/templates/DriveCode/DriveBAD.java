package edu.wpi.first.wpilibj.templates.DriveCode;

import edu.wpi.first.wpilibj.templates.CatapultHandler;
import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticManager;
import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;
import edu.wpi.first.wpilibj.templates.g;

public class DriveBAD extends DriveAbstract {

    private MotorController mBAD1, mBAD2;

    public DriveBAD(MotorController mBAD1, MotorController mBAD2) {
        this.mBAD1 = mBAD1;
        this.mBAD2 = mBAD2;
    }

    public void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
    }

    public void buttonPressEnable(int button) {
        switch (button) {
            case ButtonMap.A:
                mBAD1.set(.9);
                mBAD2.set(.9);
                //System.out.println("Thingy");
                break;
            case ButtonMap.B:
                mBAD1.set(.4);
                mBAD2.set(.4);
        }
        if (button == ButtonMap.Y) {
            try {
                PneumaticManager.getSolenoid(1).retract();
                //g.catapultTimer.enableTargetTime(CatapultHandler.determineTime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //System.out.println("Ena: " + button);
    }

    public void buttonPressDisable(int button) {
        if (button == ButtonMap.A || button == ButtonMap.B) {
            this.mBAD1.set(0);
            this.mBAD2.set(0);
        }
        if (button == ButtonMap.Y) {
            try {
                PneumaticManager.getSolenoid(1).extend();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //System.out.println("Dis: " + button);
    }

    public void releaseLocks() {
    }

    public void resetAll() {
    }

    public void resetDrive() {
    }

    public boolean isMoving() {
        return false;
    }
}
