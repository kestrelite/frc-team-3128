package frc3128.DriveTank;

import edu.wpi.first.wpilibj.Gyro;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

class Drive extends Event {

    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;

        if (Math.abs(y) < 0.15) {
            y = 0;
        }
        if (Math.abs(x) < 0.15) {
            x = 0;
        }

        DebugLog.log(4, referenceName, "Y: " + y + ", X:" + x);
        Global.mLB.set(((y - (x / 1.5)) / 1.3));
        Global.mRB.set((-(y + (x / 1.5))));
        Global.mLF.set(((y - (x / 1.5)) / 1.3));
        Global.mRF.set((-(y + (x / 1.5))));
    }
}

class BtnFire extends Event {
    public void execute() {
        System.out.println("Button A Pressed!");
    }
}

//mTilt
//TiltGyro
class AdjustTilt extends Event {
    
    double angle;
    final char thresh = 0; //I assumed the tilt values will be negative
    
    public double storeAngle() {
        
        if(angle <= 0 || angle >= 0){
            for(char q = 1; q <= 50; ++q){
                double gyr = Global.TiltGyro.getAngle();
                angle = ((angle + gyr)/q);
            }
            if(angle > 0)angle = zeroSet();//Assuming platform is completely forward at zero; moving back gives negative values
        }
        else{angle = zeroSet();}//Initializes Angle if Nan
        return angle;
    }
    
    public double zeroSet() {
        angle = 0;
        return angle;
    }
    
   
    public double gyroAngle() {
        if(storeAngle() < thresh) return thresh; //limiting motor movement
        return storeAngle();
        
    }
    
    public void setTiltAngle(double desire) {
        while(gyroAngle() > desire){                     //Motor speed decreases with distance to travel
        Global.mTilt.set(((desire * 1.5) - gyroAngle())*.01);  //Still must be properly mapped.
            DebugLog.log(4, referenceName, "Tilt Speed: " + ((desire * 1.5) - gyroAngle()));
        }
        
        while(gyroAngle() < desire){                     //Motor speed decreases with distance to travel
        Global.mTilt.set(((desire * 1.5) + gyroAngle())*.01);  //Still must be properly mapped.
            DebugLog.log(4, referenceName, "Tilt Speed: " + ((desire * 1.5) + gyroAngle()));
        }
        
        if(gyroAngle() == desire){                   
        Global.mTilt.set(0);  //Stop when at desired angle
            DebugLog.log(4, referenceName, "Tilt Alligned");
        }
    }
    
    public void execute() {
        DebugLog.log(4, referenceName, "Angle: " + gyroAngle());
        setTiltAngle(Global.xControl1.y2);
        
    }
}

public class DriveTank {

    private static Drive drive = new Drive();

    public DriveTank() {
        ListenerManager.addListener(drive, "updateDrive");
        ListenerManager.addListener(new BtnFire(), "buttonADown");
        ListenerManager.addListener(new AdjustTilt(), "updateJoy2");
    }
}