package edu.wpi.first.wpilibj.templates.DriveCode;

import edu.wpi.first.wpilibj.templates.CatapultHandler;
import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticManager;
import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;
import edu.wpi.first.wpilibj.templates.g;

public class DriveTank extends DriveAbstract {

    MotorController mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn, mBAD1, mBAD2;

    public DriveTank(MotorController mLB, MotorController mLBTurn,
            MotorController mRB, MotorController mRBTurn,
            MotorController mLF, MotorController mLFTurn,
            MotorController mRF, MotorController mRFTurn,
            MotorController mBAD1, MotorController mBAD2) throws Exception {
        this.mLB = mLB;
        this.mRB = mRB;
        this.mLF = mLF;
        this.mRF = mRF;
        this.mBAD1 = mBAD1;
        this.mBAD2 = mBAD2;
    }
    private static final int threshold = 25;

    public void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
        y1 *= -1;

        double tmpLeft, tmpRight;

        if (Math.abs(y1 * 100) < .2 && Math.abs(x1 * 100) > .25) {
            tmpLeft = -1 * (Math.abs(x1) / x1);
            tmpRight = 1 * (Math.abs(x1) / x1);
        } else {
            if (Math.abs(y1 * 100) > threshold) {
                tmpLeft = y1;
                tmpRight = y1;
            } else {
                tmpLeft = 0.0;
                tmpRight = 0.0;
            }
        }

        tmpLeft += x1 / 2.0;
        tmpRight -= x1 / 2.0;

        double max = tmpLeft;
        if (tmpRight > max) {
            max = tmpRight;
        }
        if (max > 1) {
            tmpRight /= max;
            tmpLeft /= max;
        }

        //System.out.println(x1 + ", " + y1 + ", " + tmpLeft + ", " + tmpRight);

        this.mLB.set(-tmpRight);
        this.mRB.set(tmpRight);
        this.mLF.set(tmpLeft);
        this.mRF.set(-tmpLeft);
    }

     public void buttonPressEnable(int button) {
        //System.out.println("[RBT]: PRESSED: " + button);
        if (button == ButtonMap.X) {
            try {
                PneumaticManager.getSolenoid(0).extend();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        
        if (button == ButtonMap.B) {
            g.rotGyro.reset();
        }

        if (button == ButtonMap.A) {
            if (this.lockWheels) {
                this.unlockWheels();
            } else {
                this.lockWheels();
                //this.mLBTurn.setTargetAngle(45);
                //this.mLFTurn.setTargetAngle(-45);
                 //this.mRBTurn.setTargetAngle(-45);
                //this.mRFTurn.setTargetAngle(45);
            }
        }
        if (button == ButtonMap.RB) {
            try {
                PneumaticManager.getSolenoid(1).retract();
                //g.catapultTimer.enableTargetTime(CatapultHandler.determineTime());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void lockWheels() {
        lockWheels = true;
        System.out.println("Wheels LOCKED");
    }

    private void unlockWheels() {
        lockWheels = false;
        System.out.println("Wheels UNLOCKED");
    }

    public void buttonPressDisable(int button) {
        //System.out.println("[RBT]: RELEASED: " + button);
        if (button == ButtonMap.X) {
            try {
                PneumaticManager.getSolenoid(0).retract();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (button == ButtonMap.RB) {
            try {
                PneumaticManager.getSolenoid(1).extend();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private boolean lockWheels = false;

    public boolean isMoving() {
        double total = Math.abs(mLB.get()) + Math.abs(mLF.get())
                + Math.abs(mRB.get()) + Math.abs(mRF.get());
        if (total > .05) {
            return true;
        }
        return false;
    }

    public void releaseLocks() {
    }

    public void resetAll() {
    }

    public void resetDrive() {
    }
}