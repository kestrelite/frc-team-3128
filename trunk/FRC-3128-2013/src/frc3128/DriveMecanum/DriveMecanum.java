package frc3128.DriveMecanum;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

class DriveFree extends Event {
    public void execute() {
	double r = Global.getMag(Global.xControl1.x1, Global.xControl1.y1);
	double th = Global.getTh(Global.xControl1.x1, Global.xControl1.y1);
	
        Global.mLF.set(r*Math.sin(th+(Math.PI/4)) + Global.xControl1.x2);
	Global.mRF.set(r*Math.cos(th+(Math.PI/4)) - Global.xControl1.x2);
	Global.mLB.set(r*Math.cos(th+(Math.PI/4)) + Global.xControl1.x2);
	Global.mRB.set(r*Math.sin(th+(Math.PI/4)) - Global.xControl1.x2);
    }
}

class DriveTracking extends Event {
    public void execute() {
        
    }
}

class BtnFire extends Event {
    public void execute() {
        
    }
}

class DriveModeSwitch extends Event {
    public void execute() {
        DriveMecanum.switchDriveMode();
    }
}

public class DriveMecanum {
    private static DriveTracking dTrack = new DriveTracking();
    private static DriveFree     dFree  = new DriveFree();
    protected static boolean driveIsFree = false;
        
    public DriveMecanum(boolean isFree) {
	driveIsFree = isFree;
        if(driveIsFree)  ListenerManager.addListener(dFree, "updateDrive");
        if(!driveIsFree) ListenerManager.addListener(dTrack, "updateDrive");
	
        ListenerManager.addListener(new BtnFire(),"buttonADown");
        ListenerManager.addListener(new DriveModeSwitch(), "buttonXDown");
    }
    
    protected static void switchDriveMode() {
        if(driveIsFree) {
            ListenerManager.dropEvent(dFree);
            ListenerManager.addListener(dTrack, "updateDrive");
        } else {
            ListenerManager.dropEvent(dTrack);
            ListenerManager.addListener(dFree, "updateDrive");
        }
        driveIsFree = !driveIsFree;
    }
}