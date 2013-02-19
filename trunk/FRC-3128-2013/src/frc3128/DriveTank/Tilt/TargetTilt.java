package frc3128.DriveTank.Tilt;

import com.sun.squawk.util.MathUtils;
import frc3128.Connection.Connection;
import frc3128.DriveTank.DriveTank;
import frc3128.EventManager.Event;

public class TargetTilt extends Event {
    double thShift = 9.0;
    double targetTh = 0;
    public void execute() {
        targetTh = ((targetTh > 45) ? 23 : 180*(MathUtils.atan2(92, Connection.distToGoal))/(Math.PI)+thShift);
        DriveTank.tLock.lockTo(-targetTh);
    }
}