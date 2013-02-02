package edu.wpi.first.wpilibj.templates.DriveMecanum;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.ListenerManager.ListenerManager;

class DriveFree extends Event {
    public void execute() {
	double r = Global.getMag(XControl.x1, XControl.y1);
	double th = Global.getTh(XControl.x1, XControl.y1);
	
        Global.mLF = r*Math.sin(th+(Math.PI/4)) + XControl.x2;
	Global.mRF = r*Math.cos(th+(Math.PI/4)) - XControl.x2;
	Global.mLB = r*Math.cos(th+(Math.PI/4)) + XControl.x2;
	Global.mRB = r*Math.sin(th+(Math.PI/4)) - XControl.x2;
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
    
    public DriveMecanum() {
        if(driveIsFree)  ListenerManager.addListener(dFree, "updateDrive");
        if(!driveIsFree) ListenerManager.addListener(dTrack, "updateDrive");
	
        ListenerManager.addListener(new BtnFire(),"buttonADown");
        ListenerManager.addListener(new DriveModeSwitch(), "buttonXDown");
    }
    
    public DriveMecanum(boolean isFree) {
	this.driveIsFree = isFree;
	this.DriveMecanum(); //canyouraedtihs
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