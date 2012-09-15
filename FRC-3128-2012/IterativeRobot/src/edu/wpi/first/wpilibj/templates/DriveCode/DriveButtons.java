package edu.wpi.first.wpilibj.templates.DriveCode;

import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;

public class DriveButtons extends DriveAbstract {

    public void driveHandler(double x1, double y1, double triggers, double x2, double y2) {
    }

    public void buttonPressEnable(int button) {
        switch(button)
        {
            case ButtonMap.Y:
                
                break;
        }
    }

    public void buttonPressDisable(int button) {
    }

    public void releaseLocks() {
    }

    public void resetAll() {
    }

    public void resetDrive() {
    }

    public boolean isMoving() {
        return true;
    }
    
}
