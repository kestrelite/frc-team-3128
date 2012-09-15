package util.Controller;

public abstract class DriveLayout {
    public abstract void dEvent(double x1, double y1, double triggers, double x2, double y2);
    public abstract void buttonEnable(int button);
    public abstract void buttonDisable(int button);
}
