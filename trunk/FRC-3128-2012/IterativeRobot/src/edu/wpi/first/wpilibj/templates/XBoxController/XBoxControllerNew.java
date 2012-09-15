package edu.wpi.first.wpilibj.templates.XBoxController;

import edu.wpi.first.wpilibj.templates.XBoxController.Autonomous.AutoRecorder;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ThreadLock;
import edu.wpi.first.wpilibj.templates.XBoxController.Autonomous.AutoInterface;

public class XBoxControllerNew extends Joystick implements EventInterface {

    public double y1;
    public double y2;
    public double x1;
    public double x2;
    public double triggers;
    public boolean[] buttons = new boolean[10];
    private ButtonMap b = new ButtonMap();
    DriveAbstract d;
    private double lastSum = 0;
    ThreadLock controllerLock;
    private boolean lastWasReset = false;
    AutoRecorder autonomousRecord = null;
    AutoInterface autonomousPlayback = null;

    public void startEventMonitor(boolean asRunnable) throws Exception {
        if (asRunnable) {
            throw new Exception("Do not run this in a thread.");
        } else {
            if (!this.controllerLock.getLockIfAvailable(this)) {
                throw new Exception("Could not get controller lock at initialization!");
            }
            EventManager.addIterable(this);
        }
    }

    public void stopEventMonitor() throws Exception {
        this.controllerLock.releaseLock(this);
        d.releaseLocks();
    }

    public XBoxControllerNew(int port, ThreadLock lock, DriveAbstract d) throws Exception {
        super(port);
        this.controllerLock = lock;
        this.d = d;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
    }

    public XBoxControllerNew(int port, ThreadLock lock, DriveAbstract d, AutoRecorder a) throws Exception {
        super(port);
        this.controllerLock = lock;
        this.d = d;
        this.autonomousRecord = a;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
    }

    public XBoxControllerNew(int port, ThreadLock lock, DriveAbstract d, AutoInterface a) throws Exception {
        super(port);
        this.controllerLock = lock;
        this.d = d;
        this.autonomousPlayback = a;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
    }

    private double getControlSum() {
        int buttonSum = 0;
        for (int i = 0; i < 10; i++) {
            if (this.getButton(i)) {
                buttonSum += MathUtils.pow(2, i);
            }
        }

        return (this.getAxis(2) + this.getAxis(5)
                + this.getAxis(1) + this.getAxis(4)
                + this.getAxis(3) + buttonSum);
    }

    public double sqr(double i) {
        return MathUtils.pow(i, 2);
    }

    private double getAxis(int axis) {
        if (this.autonomousPlayback != null) {
            return this.autonomousPlayback.getRawAxis(axis);
        }
        return super.getRawAxis(axis);
    }

    private boolean getButton(int btn) {
        if (this.autonomousPlayback != null) {
            return this.autonomousPlayback.getRawButton(btn);
        }
        return super.getRawButton(btn);
    }

    private void findControllerChange() throws Exception {
        boolean somethingWasRun = false;
        if (d == null) {
            throw new Exception("No DriveAbstract Specified!");
        }

        boolean chg = false;

        if (y1 != this.getAxis(2)) {
            y1 = this.getAxis(2);
            chg = true;
        }

        if (y2 != this.getAxis(5)) {
            y2 = this.getAxis(5);
            chg = true;
        }

        if (x1 != this.getAxis(1)) {
            x1 = this.getAxis(1);
            chg = true;
        }

        if (x2 != this.getAxis(4)) {
            x2 = this.getAxis(4);
            chg = true;
        }

        if (chg) {
            triggers = this.getAxis(3);
            d.driveHandler(x1, y1, triggers, x2, y2);
            somethingWasRun = true;
        }

        if (!somethingWasRun) {
            if (!lastWasReset) {
                if (!d.isMoving()) {
                    d.resetDrive();
                    lastWasReset = true;
                }
            }
        } else {
            lastWasReset = false;
        }

        for (int i = 0;
                i < 10; i++) {
            if (buttons[i] != this.getButton(i)) {
                buttons[i] = this.getButton(i);
                if (buttons[i]) {
                    d.buttonPressEnable(i);
                } else {
                    d.buttonPressDisable(i);
                }
                break;
            }
        }
    }

    public boolean eventIterable() throws Exception {
        //So much trying, not enough doing.
        try {
            try {
                this.autonomousRecord.update(y1, y2, x1, x2, triggers, buttons);
                this.autonomousRecord.eventIterable();
            } catch (NullPointerException e) {
                try {
                    this.autonomousPlayback.iterate();
                } catch (NullPointerException e2) {
                }
            }
            //if (this.getControlSum() != lastSum) {
            this.findControllerChange();
            //}
            //lastSum = this.getControlSum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }

    public void reset() {
        this.d.resetAll();
    }
}
