package frc3128.HardwareLink.Motor;

import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;

/**
 *
 * @author Noah Sutton-Smolin
 */
public abstract class MotorControl extends Event {

    private long lastRuntime = 0;
    private MotorLink controlledMotor = null;

    public MotorControl() {
    }

    protected void setControlledMotor(MotorLink m) {
        controlledMotor = m;
    }

    public abstract void setControlTarget(double val);

    public abstract double speedControlStep(double dt);

    public abstract void clearControlRun();

    public abstract boolean isComplete();

    /**
     *
     * @return the last runtime in system clock milliseconds
     */
    public final long getLastRuntime() {
        return lastRuntime;
    }

    /**
     *
     * @return how long ago the event was last called (used for dT)
     */
    public final long getLastRuntimeDist() {
        return System.currentTimeMillis() - lastRuntime;
    }

    /**
     *
     * @return the encoder value of the controlled motor
     */
    public double getLinkedEncoderAngle() {
        return this.controlledMotor.getEncoderAngle();
    }

    /**
     *
     * @return the linked motor's speed
     */
    public double getLinkedMotorSpeed() {
        return this.controlledMotor.getSpeed();
    }

    public final void execute() {
        lastRuntime = System.currentTimeMillis();
        if (this.isComplete()) {
            this.controlledMotor.setInternalSpeed(0);
        }
        this.controlledMotor.setInternalSpeed(this.speedControlStep(this.getLastRuntimeDist()));
    }
}
