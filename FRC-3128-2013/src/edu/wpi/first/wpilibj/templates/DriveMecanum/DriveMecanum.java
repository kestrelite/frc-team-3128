package edu.wpi.first.wpilibj.templates.DriveMecanum;
import edu.wpi.first.wpilibj.templates.EventManager.Event;
import edu.wpi.first.wpilibj.templates.ListenerManager.ListenerManager;

class DriveFree extends Event {
    public void execute() {
        
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
        ListenerManager.addListener(dFree, "updateDrive");
        
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