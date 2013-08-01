package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

/**
 * 
 * @author Noah Sutton-Smolin
 */
//TODO: Class javadoc
public class MotorLink {
	private final Jaguar            motor;
	private       AbstractEncoder   encoder;
	private       MotorSpeedControl spdControl;
	private       boolean           spdControlEnabled = false;
	private       boolean           motorReversed = false;
	
	public MotorLink(Jaguar motor) {this.motor = motor;}
	public MotorLink(Jaguar motor, AbstractEncoder enc) {this(motor); this.encoder = enc;}
	public MotorLink(Jaguar motor, AbstractEncoder enc, MotorSpeedControl spdControl) {this(motor, enc); this.spdControl = spdControl;}
	
	public double getSpeed() {return motor.get();}
	public void setSpeed(double spd) {
		if(spdControlEnabled) {
			DebugLog.log(DebugLog.LVL_WARN, this, "The speed of the motor was set while a controller was active! Disabling speed controller...");
			this.stopSpeedControl();
		}
		
		this.motor.set(reversedCheck(spd));
	}
	protected void setSpeedControlled(double spd) {this.motor.set(spd);}
	
	public void setEncoder(AbstractEncoder encoder) {
		if(encoder != null) DebugLog.log(DebugLog.LVL_INFO, this, "The encoder was changed in the motor!");
		this.encoder = encoder;
	}

	public double getEncoderAngle() {
		if(encoder == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The encoder heading was read, but no encoder was set!"); return -1;}
		return RobotMath.normalizeAngle(encoder.getAngle());
	}
	
	public void setSpeedControl(MotorSpeedControl spdControl) {
		if(spdControlEnabled) DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was set while another was active!");
		if(spdControl != null && spdControlEnabled) this.stopSpeedControl(); this.spdControl = spdControl;
	}
	
	public void stopSpeedControl() {
		if(spdControl == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was stopped when it did not exist!"); return;}
		this.spdControlEnabled = false; spdControl.cancelEvent();
	}
	
	public void startSpeedControl() {
		if(spdControl == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was started when it did not exist!"); return;}
		this.spdControlEnabled = true; spdControl.registerIterableEvent();
	}
	public void setSpeedControlTarget(double d) {this.spdControl.setControlTarget(d);}
	
	public void reverseMotor() {this.motorReversed = !this.motorReversed;}
	public boolean getMotorReversed() {return this.motorReversed;}
	private double reversedCheck(double spd) {return spd * (motorReversed ? -1.0 : 1.0);}
}
