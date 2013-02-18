/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    
    public final static Compressor cAir = new Compressor(1, 1, 1, 2);
    public final static Joystick joy = new Joystick(1);
   
    public final static Jaguar mLB = new Jaguar(1, 3);
    public final static Jaguar mRB = new Jaguar(1, 1);
    public final static Jaguar mLF = new Jaguar(1, 2);
    public final static Jaguar mRF = new Jaguar(1, 4);
    public final static Jaguar mTilt = new Jaguar(1, 6);
    public final static Jaguar mShoot = new Jaguar(1, 7); 
   
    public       static Relay camLight = new Relay(1, 1, Relay.Direction.kForward);
    public final static Gyro gTilt = new Gyro(1, 2), gTurn = new Gyro(1,1);

    public final static DoubleSolenoid pHopper = new DoubleSolenoid(1, 1, 2), pst2 = new DoubleSolenoid(1, 3, 4);
    
    public void stopDrive(){
        mLB.set(0);
        mLF.set(0);
        mRB.set(0);
        mRF.set(0);
    }
    
    public void startDrive(){
        if(Math.abs(joy.getRawAxis(2)) < 0.15 && Math.abs(joy.getRawAxis(1)) < 0.15) stopDrive();
        else{
            mLB.set((joy.getRawAxis(2) - joy.getRawAxis(1))/2);
            mLF.set((joy.getRawAxis(2) - joy.getRawAxis(1))/2);
            mRB.set((joy.getRawAxis(2) + joy.getRawAxis(1))/2);
            mRF.set((joy.getRawAxis(2) + joy.getRawAxis(1))/2);
        }
    }
    
//    public void setTiltAngle() {
//        double y2 = -joy.getRawAxis(5);
//        
//        if(y2 > angle) angle = y2;
//        else if(angle < 0) 
//        if(Math.abs(gTilt.getAngle() - angle*35.0) > 1)
//            mTilt.set((gTilt.getAngle() - angle*35.0)/35.0);
//        else
//            mTilt.set(0);
//    }
    
    public void checkButtons() {
        if(joy.getRawButton(1)) pHopper.set(DoubleSolenoid.Value.kReverse);
        else pHopper.set(DoubleSolenoid.Value.kForward);
        if(joy.getRawButton(5)) mTilt.set(0.4);
        else if(joy.getRawButton(6)) mTilt.set(-0.4);
        else mTilt.set(0);
        if(joy.getRawButton(9)) mShoot.set(1.0);
        else mShoot.set(0);
    }
    
    public void robotInit() {
        
        
        cAir.start();
        gTilt.reset(); gTurn.reset();
        camLight.set(Relay.Value.kOn);
        pHopper.set(DoubleSolenoid.Value.kForward);
        stopDrive();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //double angle = 0;
        
        for(;;){
            startDrive();
            checkButtons();
        }
    }
}
