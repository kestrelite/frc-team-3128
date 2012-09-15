package edu.wpi.first.wpilibj.templates.XBoxController;

import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.ThreadLock;

public abstract class DriveAbstract {

    public static final ButtonMap b = new ButtonMap();

    public abstract void driveHandler(double x1, double y1, double triggers, double x2, double y2);

    public abstract void buttonPressEnable(int button);

    public abstract void buttonPressDisable(int button);

    public abstract void releaseLocks();

    public abstract void resetAll();

    public abstract void resetDrive();
    
    public abstract boolean isMoving();
}
