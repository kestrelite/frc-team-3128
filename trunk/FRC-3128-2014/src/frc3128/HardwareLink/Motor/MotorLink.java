package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Talon;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.DebugLog;

/**
 * @author Noah Sutton-Smolin
 */
public class MotorLink {

    private final Talon talon;
    private AbstractEncoder encoder;
    private MotorControl spdControl;
    private boolean spdControlEnabled = false;
    private boolean motorReversed = false;
    private double speedScalar = 1;

    public MotorLink(Talon talon) {
        this.talon = talon;
    }

    public MotorLink(Talon talon, double powscl) {
        this(talon);
        this.speedScalar = powscl;
    }

    public MotorLink(Talon talon, AbstractEncoder enc) {
        this(talon);
        this.encoder = enc;
    }

    public MotorLink(Talon talon, AbstractEncoder enc, double powscl) {
        this(talon, enc);
        this.speedScalar = powscl;
    }

    public MotorLink(Talon talon, AbstractEncoder enc, MotorControl spd) {
        this(talon, enc);
        this.spdControl = spd;
    }

    public MotorLink(Talon talon, AbstractEncoder enc, MotorControl spd, double powscl) {
        this(talon, enc, spd);
        this.speedScalar = powscl;
    }

    protected void setInternalSpeed(double pow) {
        talon.pidWrite(pow * speedScalar * (motorReversed ? -1.0 : 1.0));
    }

    public void setSpeedScalar(double powScl) {
        this.speedScalar = powScl;
    }

    public double getSpeedScalar(double powScl) {
        return this.speedScalar;
    }

    public double getSpeed() {
        return talon.get();
    }

    public void setSpeed(double pow) {
        if (this.spdControlEnabled) {
            this.spdControl.cancelEvent();
            this.spdControlEnabled = false;
            DebugLog.log(DebugLog.LVL_WARN, this, "The motor power was set from outside the speed controller, so the controller was canceled.");
        }
        setInternalSpeed(pow);
    }

    public void reverseMotor() {
        motorReversed = !motorReversed;
    }

    public void setEncoder(AbstractEncoder enc) {
        if (this.encoder != null) {
            DebugLog.log(DebugLog.LVL_WARN, this, "The encoder has been changed when one already existed.");
        }
        this.encoder = enc;
    }

    public void setSpeedController(MotorControl spdControl) {
        //if(this.spdControl != null) DebugLog.log(DebugLog.LVL_WARN, this, "The speed controller was changed when one already existed.");
        if (this.spdControlEnabled) {
            this.spdControlEnabled = false;
            this.spdControl.cancelEvent();
            DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was changed when one was running.");
        }
    }

    public double getEncoderAngle() {
        if (encoder == null) {
            DebugLog.log(DebugLog.LVL_ERROR, this, "Something attempted to get the encoder value, but no encoder exists.");
            return -1;
        }
        return encoder.getAngle();
    }

    public void setControlTarget(double target) {
        if (this.spdControl == null) {
            DebugLog.log(DebugLog.LVL_ERROR, this, "The speed controller's target was set, but none exists.");
            return;
        }
        if (!this.spdControlEnabled) {
            DebugLog.log(DebugLog.LVL_WARN, this, "The speed controller's target was set, but it is not enabled.");
            return;
        }

        this.spdControl.setControlTarget(target);
    }

    public boolean speedControlRunning() {
        return this.spdControlEnabled;
    }

    public void setSpeedControlTarget(double target) {
        this.spdControl.setControlTarget(target);
    }

    public void startControl(double target) {
        this.spdControl.clearControlRun();
        this.spdControl.setControlTarget(target);
        this.spdControl.setControlledMotor(this);
        this.spdControl.registerIterableEvent();
        this.spdControlEnabled = true;
    }

    public void stopSpeedControl() {
        DebugLog.log(DebugLog.LVL_DEBUG, this, "Talon GET: "+this.talon.get());
        this.talon.set(0);
        this.spdControl.cancelEvent();
        this.spdControlEnabled = false;
    }
}
