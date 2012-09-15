package edu.wpi.first.wpilibj.templates.XBoxController;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ThreadLock;
//import edu.wpi.first.wpilibj.templates.XBoxController.AutoIterable;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;

public class XBoxAutoRec extends Joystick implements EventInterface {

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
//    private AutoIterable auto;

//    private boolean getRawButton(int i) {
//        return auto.getRawButton(i);
//    }

//    private double getRawAxis(int axis) {
//        return auto.getRawAxis(axis);
//    }

    public XBoxAutoRec(int port, DriveAbstract d) throws Exception {
//        this.auto = a;
        super(port);
        this.d = d;
        for (int i = 0; i < 10; i++) {
            buttons[i] = false;
        }
        EventManager.addIterable(this);
        System.out.print("{");
    }

    public void stopEventMonitor() throws Exception {
        this.controllerLock.releaseLock(this);
        d.releaseLocks();
    }

    private double getControlSum() {
        int buttonSum = 0;
        for (int i = 0; i < 10; i++) {
            if (this.getRawButton(i)) {
                buttonSum++;
            }
        }

        return (this.getRawAxis(2) + this.getRawAxis(5)
                + this.getRawAxis(1) + this.getRawAxis(4)
                + this.getRawAxis(3) + buttonSum);
    }
    double threshold = .18;

    public void setThreshold(double thresh) {
        this.threshold = thresh;
    }

    public double sqr(double i) {
        return MathUtils.pow(i, 2);
    }

    private boolean checkThresholding(double x1, double y1) {
        if (sqr(x1) + sqr(y1) < sqr(threshold)) {
            return false;
        }
        return true;
    }

    private void autoOutput() {
        //Y1, Y2, X1, X2, Triggers
        boolean a = this.getRawButton(1);
        boolean s = this.getRawButton(2);
        boolean d = this.getRawButton(3);
        boolean f = this.getRawButton(4);
        boolean g = this.getRawButton(5);
        boolean h = this.getRawButton(6);
        boolean j = this.getRawButton(7);
        boolean k = this.getRawButton(8);
        boolean l = this.getRawButton(9);
        boolean z = this.getRawButton(10);

        int q = a ? 1 : 0;
        int w = s ? 1 : 0;
        int e = d ? 1 : 0;
        int r = f ? 1 : 0;
        int t = g ? 1 : 0;
        int y = h ? 1 : 0;
        int u = j ? 1 : 0;
        int i = k ? 1 : 0;
        int o = l ? 1 : 0;
        int p = z ? 1 : 0;

        System.out.print("{" + this.getRawAxis(2) + ", " + this.getRawAxis(5)
                + this.getRawAxis(1) + ", " + this.getRawAxis(4) + ", "
                + q + ", " + w + ", " + e + ", " + r + ", " + t + ", " + y
                + ", " + u + ", " + i + ", " + o + ", " + p + "}");
    }

    private void findControllerChange() throws Exception {
        boolean somethingWasRun = false;
        //boolean lastWasRest = false;
        if (d == null) {
            throw new Exception("No DriveAbstract Specified!");
        }

        if (y1 != this.getRawAxis(2)) {
            y1 = this.getRawAxis(2);
        }

        if (y2 != this.getRawAxis(5)) {
            y2 = this.getRawAxis(5);
        }

        if (x1 != this.getRawAxis(1)) {
            x1 = this.getRawAxis(1);
        }

        if (x2 != this.getRawAxis(4)) {
            x2 = this.getRawAxis(4);
        }

        if (this.checkThresholding(x1, y1) || this.checkThresholding(x2, y2) || triggers != this.getRawAxis(3)) {
            triggers = this.getRawAxis(3);
            d.driveHandler(x1, y1, triggers, x2, y2);
            somethingWasRun = true;
        }

        if (!somethingWasRun) {
            if (!lastWasReset) {
                d.resetDrive();
                lastWasReset = true;
            }
        } else {
            lastWasReset = false;
        }
        
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
        //auto.iterate();
        this.autoOutput();
        try {
            if (this.getControlSum() != lastSum) {
                this.findControllerChange();
            }
            lastSum = this.getControlSum();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(4);
        return true;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
    }
}
