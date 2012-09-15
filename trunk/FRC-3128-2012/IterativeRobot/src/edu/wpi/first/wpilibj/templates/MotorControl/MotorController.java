package edu.wpi.first.wpilibj.templates.MotorControl;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ThreadLock;
import edu.wpi.first.wpilibj.templates.g;

public class MotorController extends Jaguar implements EventInterface {

    EncoderController e;
    ThreadLock motorLock;
    private double updateablePower = 0;
    private double updateableAngle = 0;
    private boolean iterableIsActive = false;
    double targetAngle;
    private double encoderPolarity = 1.0;
    private double motorPolarity = 1.0;

    public void reverseMotor() {
        motorPolarity *= -1;

    }

    public void reverseEncoder() {
        encoderPolarity *= -1;
    }

    public MotorController(int location, ThreadLock l) throws Exception {
        super(location);
        motorLock = l;
    }

    public MotorController(int locationA, int locationB, ThreadLock l) throws Exception {
        super(locationA, locationB);
        motorLock = l;
    }

    public MotorController(int location, EncoderController e, ThreadLock l) throws Exception {
        super(location);
        this.e = e;
        motorLock = l;
    }

    public MotorController(int locationA, int locationB, EncoderController e, ThreadLock l) throws Exception {
        super(locationA, locationB);
        this.e = e;
        motorLock = l;
    }

    private void getMotorLock() throws Exception {
        if (!motorLock.getLockIfAvailable(this)) {
            throw new Exception("Unable to lock motor!");
        }
    }

    private void releaseMotorLock() throws Exception {
        motorLock.releaseLock(this);
    }

    public void set(double j) {
        try {
            if (j != updateablePower) {
                updateablePower = j;
                this.eventRegisterSelf();
                //System.out.println("MEvent Registered");
            }
        } catch (Exception ex) {
        }
    }

    public void startIterable() throws Exception {
        EventManager.addIterable(this);
        this.iterableIsActive = true;
    }

    public void endIterable() throws Exception {
        EventManager.removeIterable(this);
        this.iterableIsActive = false;
    }

    public void setTargetAngle(double targetAngle) {
        if (!iterableIsActive) {
            throw new Error("Cannot set target angle: iterable inactive");
        }
        if (this.e == null) {
            throw new Error("Encoder not assigned");
        }
        this.updateableAngle = targetAngle;
    }
    
    public void setTargetAngleX(double targetAngle) {
        if (!iterableIsActive) {
            throw new Error("Cannot set target angle: iterable inactive");
        }
        if (this.e == null) {
            throw new Error("Encoder not assigned");
        }
        this.updateableAngle = targetAngle;
    }


    private double sqr(double a) {
        return MathUtils.pow(a, 2);
    }

    private double sigmoid(double n) {

        //return (1.0/350.0)*(n)+.5;
        return 1;
        //double f = -(1.0 / 46.0) * (n - 90.0);
        //double q = MathUtils.pow(Math.E, f);
        //q += 1.0;
        //return (1.0 / q) + 0.15;
    }

    private double getNewPowerFromTarget() {

        if (e == null) {
            throw new Error("No encoder present");
        }
        double ca = g.normalize(e.getAngle());
        double ta = this.updateableAngle;
        if(Math.abs(ta-ca) > 2)
        {
            int dir = -1;
            if(ta-ca < 0) dir = 1;
            if(Math.abs(ta-ca) > 100) return 1*dir;
            return sigmoid(g.normalize(Math.abs(ta-ca)))*((double)dir);
        }
        return 0;
    }

    public EncoderController getEncoder() {return this.e;}
    
    public void setGearRatio(double gearRatio) {
        e.setGearRatio(gearRatio);
    }

    public boolean eventIterable() throws Exception {

        this.set(getNewPowerFromTarget() * this.motorPolarity);

        return true;
    }

    public double getEncVal() {
        if (this.e == null) {
            throw new Error("Enc not instantiated on motor " + this);
        }
        return this.e.getAngle();
    }

    public void eventProcessor() throws Exception {
        super.set(updateablePower * this.motorPolarity);
    }

    public void eventRegisterSelf() throws Exception {
        EventManager.addEventTrigger(this);
    }
}
