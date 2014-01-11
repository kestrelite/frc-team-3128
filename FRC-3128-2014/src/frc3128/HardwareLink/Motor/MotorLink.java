package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.DebugLog;

/**
 * @author Noah Sutton-Smolin
 */
public class MotorLink {
    private final Jaguar            jag;
    private       AbstractEncoder   encoder;
    private       MotorSpeedControl spdControl;
    private       boolean           spdControlEnabled = false;
    private       boolean           motorReversed = false;
    private       double            speedScalar = 1;

    public MotorLink(Jaguar jag) {this.jag = jag;}
    public MotorLink(Jaguar jag, double powscl) {this(jag); this.speedScalar = powscl;}
    public MotorLink(Jaguar jag, AbstractEncoder enc) {this(jag); this.encoder = enc;}
    public MotorLink(Jaguar jag, AbstractEncoder enc, double powscl) {this(jag, enc); this.speedScalar = powscl;}
    public MotorLink(Jaguar jag, AbstractEncoder enc, MotorSpeedControl spd) {this(jag, enc); this.spdControl = spd;}
    public MotorLink(Jaguar jag, AbstractEncoder enc, MotorSpeedControl spd, double powscl) {this(jag, enc, spd); this.speedScalar = powscl;}

    protected void setInternalSpeed(double pow) {
        jag.set(pow*speedScalar*(motorReversed?-1.0:1.0));
    }

    public void setSpeedScalar(double powScl) {this.speedScalar = powScl;}
    public double getSpeedScalar(double powScl) {return this.speedScalar;}
    
    public double getSpeed() {return jag.get();}
    public void setSpeed(double pow) {
        if(this.spdControlEnabled) {
            this.spdControl.cancelEvent();
            this.spdControlEnabled = false;
            DebugLog.log(DebugLog.LVL_WARN, this, "The motor power was set from outside the speed controller, so the controller was canceled.");
        }
        setInternalSpeed(pow);
    }
    
    public void reverseMotor() {motorReversed = !motorReversed;}
    
    public void setEncoder(AbstractEncoder enc) {
        if(this.encoder != null) DebugLog.log(DebugLog.LVL_WARN, this, "The encoder has been changed when one already existed.");
        this.encoder = enc;
    }
    
    public void setSpeedController(MotorSpeedControl spdControl) {
        //if(this.spdControl != null) DebugLog.log(DebugLog.LVL_WARN, this, "The speed controller was changed when one already existed.");
        if(this.spdControlEnabled) {
            this.spdControlEnabled = false;
            this.spdControl.cancelEvent();
            DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was changed when one was running.");
        }
    }
    
    public double getEncoderAngle() {
        if(encoder == null) {DebugLog.log(DebugLog.LVL_ERROR, this, "Something attempted to get the encoder value, but no encoder exists."); return -1;}
        return encoder.getAngle();
    }

    public void setControlTarget(double target) {
        if(this.spdControl == null) {
            DebugLog.log(DebugLog.LVL_ERROR, this, "The speed controller's target was set, but none exists.");
            return;
        }
        if(!this.spdControlEnabled) {
            DebugLog.log(DebugLog.LVL_WARN, this, "The speed controller's target was set, but it is not enabled.");
            return;
        }
        
        this.spdControl.setControlTarget(target);
    }
    
    public boolean speedControlRunning() {return this.spdControlEnabled;}
    public void setSpeedControlTarget(double target) {this.spdControl.setControlTarget(target);}
    public void startSpeedControl(double target) {
        this.spdControl.clearControlRun();
        this.spdControl.setControlTarget(target);
        this.spdControl.setControlledMotor(this);
        this.spdControl.registerIterableEvent();
        this.spdControlEnabled = true;
    }
    public void stopSpeedControl() {
        this.spdControl.cancelEvent();
        this.spdControlEnabled = false;
    }
}
