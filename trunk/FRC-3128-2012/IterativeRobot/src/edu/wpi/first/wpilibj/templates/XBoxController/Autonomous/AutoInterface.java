package edu.wpi.first.wpilibj.templates.XBoxController.Autonomous;

public interface AutoInterface {
    public void iterate();
    public double getRawAxis(int axis);
    public boolean getRawButton(int axis);
    public void setLocation(int loc);
}
