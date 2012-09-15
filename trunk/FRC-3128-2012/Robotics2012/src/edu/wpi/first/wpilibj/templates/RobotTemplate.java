package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import util.Controller.Controller;
import util.Controller.DriveLayout;
import util.EventManager.EventManager;
import util.EventManager.EventMono;
import util.EventManager.EventTimer;

class test extends EventTimer {

    protected void end() {
        System.out.println("Ended");
    }

    protected void start() {
        System.out.println("Started");
    }
}

class etest implements EventMono {

    boolean first = true;

    public void iterate() {
        System.out.println("Hello");
    }

    public boolean shouldIterate() {
        if (first) {
            first = false;
            return true;
        }
        return false;
    }

    public boolean shouldRemove() {
        return true;
    }
}

class d extends DriveLayout {

    public void dEvent(double x1, double y1, double triggers, double x2, double y2) {
    }

    public void buttonEnable(int button) {
        System.out.println("Button: " + button);
    }

    public void buttonDisable(int button) {
        System.out.println("DButton: " + button);
    }
}

public class RobotTemplate extends IterativeRobot {

    test t = new test();
    etest n = new etest();

    public void robotInit() {
        EventManager.registerEventMono(new Controller(1, new d()));
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        EventManager.registerEventTimer(t, 5);
        EventManager.registerEventMono(n);
    }

    public void teleopContinuous() {
        EventManager.processEvents();
    }
}
