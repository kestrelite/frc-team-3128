package edu.wpi.first.wpilibj.templates.XBoxController;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ThreadLock;

public class XBoxController extends Joystick implements EventInterface {
    public double y1;
    public double y2;
    public double x1;
    public double x2;
    public double triggers;
    public boolean[] buttons = new boolean[10];
    private ButtonMap b = new ButtonMap();
    //private boolean loggingEnabled = false;
    //private boolean playbackEnabled = false;
    DriveAbstract d;
    private double lastSum = 0;
    ThreadLock controllerLock;
    private boolean lastWasReset = false;
    
    public XBoxController(int port, ThreadLock lock) throws Exception {
        super(port);
        this.controllerLock = lock;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
        System.out.println("XBoxController constructed");
    }

    public void startEventMonitor(boolean asRunnable) throws Exception {
        if (asRunnable) {
            //if (!this.controllerLock.getLockIfAvailable(this)) {
            throw new Exception("Could not get controller lock at initialization!");
            //}
            //EventManager.addIterable(this.XBoxThread);
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

    public XBoxController(int port, ThreadLock lock, DriveAbstract d) throws Exception {
        super(port);
        this.controllerLock = lock;
        this.d = d;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
        //if (!controllerLock.getLockIfAvailable(this)) {
        //    throw new Exception("Controller class could not get lock!");
        //}
    }

    
    //!!!!!!!!
    //This is probably the problem with the controls...
    //You can have the same sum even if there is a change
    //I disabled the use of this feature down in event iterable. -Garrison
    private double getControlSum() {
        int buttonSum = 0;
        for (int i = 0; i < 10; i++) {
            if (this.getRawButton(i)) {
                buttonSum += MathUtils.pow(2,i);
            }
        }

        return (this.getRawAxis(2) + this.getRawAxis(5)
                + this.getRawAxis(1) + this.getRawAxis(4)
                + this.getRawAxis(3) + buttonSum);
    }
    //double threshold = .18;

    //public void setThreshold(double thresh) {
    //    this.threshold = thresh;
    //}

    public double sqr(double i) {
        return MathUtils.pow(i, 2);
    }

    //private boolean checkThresholding(double x1, double y1) {
    //    if (sqr(x1) + sqr(y1) < sqr(threshold)) {
    //        return false;
    //    }
    //    return true;
    //}

    private void findControllerChange() throws Exception {
        boolean somethingWasRun = false;
        //boolean lastWasRest = false;
        if (d == null) {
            throw new Exception("No DriveAbstract Specified!");
        }

        boolean chg = false;
        
        if (y1 != this.getRawAxis(2)) {
            y1 = this.getRawAxis(2);
            //System.out.println("Y1: " + y1);
            chg = true;
        }

        if (y2 != this.getRawAxis(5)) {
            y2 = this.getRawAxis(5);
            //System.out.println("Y2: " + y2);
            chg = true;
        }

        if (x1 != this.getRawAxis(1)) {
            x1 = this.getRawAxis(1);
            //System.out.println("X1: " + x1);
            chg = true;
        }

        if (x2 != this.getRawAxis(4)) {
            x2 = this.getRawAxis(4);
            //System.out.println("X2: " + x2);
            chg = true;
        }

        //System.out.println(MathUtils.atan2(x1, y1) * 180 / Math.PI);
        //System.out.println(MathUtils.atan2(y1, x1) * 180 / Math.PI);
        //if(this.checkThresholding(x1, y1) || this.checkThresholding(x2, y2) || triggers != this.getRawAxis(3))
        if(chg)
        {
           triggers = this.getRawAxis(3);
           d.driveHandler(x1, y1, triggers, x2, y2);
           somethingWasRun = true;
        }
        
        if (!somethingWasRun) {
            if (!lastWasReset) {
                if(!d.isMoving())
                {
                    d.resetDrive();
                    lastWasReset = true;
                }
            }
        } else {
            lastWasReset = false;
        }
        //System.out.println("Running drive train handler... ");

        for (int i = 0;
                i < 10; i++) {
            if (buttons[i] != this.getRawButton(i)) {
                buttons[i] = this.getRawButton(i);
                if (buttons[i]) {
                    d.buttonPressEnable(i);
                    //System.out.println("Enable button " + i);
                } else {
                    d.buttonPressDisable(i);
                    //System.out.println("Disable button " + i);
                }
                break;
            }
        }
    }

    public boolean eventIterable() throws Exception {
        try {
            //if (this.getControlSum() != lastSum) {
                this.findControllerChange();
            //}
            //lastSum = this.getControlSum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if(this.loggingEnabled) this.autoOutput();
        //if(this.loggingEnabled || this.playbackEnabled) Thread.sleep(4);
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
    
    public void reset()
    {
        this.d.resetAll();
    }
}
