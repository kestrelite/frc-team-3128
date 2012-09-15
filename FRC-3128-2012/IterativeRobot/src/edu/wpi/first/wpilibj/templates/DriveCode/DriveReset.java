package edu.wpi.first.wpilibj.templates.DriveCode;

import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;
import edu.wpi.first.wpilibj.templates.g;

public class DriveReset extends DriveAbstract {

    MotorController mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn, mBAD1, mBAD2;
    double l = 23.0;
    double w = 19.5;
    //Gyro rotGyro;

    public DriveReset(MotorController mLB, MotorController mLBTurn,
            MotorController mRB, MotorController mRBTurn,
            MotorController mLF, MotorController mLFTurn,
            MotorController mRF, MotorController mRFTurn,
            MotorController mBAD1, MotorController mBAD2) throws Exception {
        this.mLB = mLB;
        this.mRB = mRB;
        this.mLF = mLF;
        this.mRF = mRF;
        this.mLBTurn = mLBTurn;
        this.mRBTurn = mRBTurn;
        this.mLFTurn = mLFTurn;
        this.mRFTurn = mRFTurn;
        this.mBAD1 = mBAD1;
        this.mBAD2 = mBAD2;
    }

    public void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
        print();
    }

    private void print()
    {
        //System.out.println("mLB: " + mLBTurn.getEncVal());
        //System.out.println("mRB: " + mRBTurn.getEncVal());
        //System.out.println("mLF: " + mLFTurn.getEncVal());
        //System.out.println("mRF: " + mRFTurn.getEncVal());
        //System.out.println("mRF: " + mRFTurn.getEncoder().getRaw());
        System.out.println("Gyr: " + g.rotGyro.getAngle());
    }
    
    public void buttonPressEnable(int button) {
        switch (button) {
            case ButtonMap.A:
                mLBTurn.set(1);
                break;
            case ButtonMap.B:
                mRBTurn.set(1);
                break;
            case ButtonMap.X:
                mLFTurn.set(1);
                break;
            case ButtonMap.Y:
                mRFTurn.set(1);
                break;
            case ButtonMap.RB:
                mRBTurn.getEncoder().reset();
                mRFTurn.getEncoder().reset();
                mLFTurn.getEncoder().reset();
                mLBTurn.getEncoder().reset();
                break;
            default:
                break;
        }
    }

    public void buttonPressDisable(int button) {
        switch (button) {
            case ButtonMap.A:
                mLBTurn.set(0);
                break;
            case ButtonMap.B:
                mRBTurn.set(0);
                break;
            case ButtonMap.X:
                mLFTurn.set(0);
                break;
            case ButtonMap.Y:
                mRFTurn.set(0);
                break;
            default:
                break;

        }
    }

    public boolean isMoving() {
        return false;
    }

    public void releaseLocks() {
    }

    public void resetAll() {
    }

    public void resetDrive() {
    }
}