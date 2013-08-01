package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.DebugLog;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.MotorMath;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class MotorLink {
	private final Jaguar               motor;
	private       AbstractEncoder      encoder;
	private       MotorSpeedControl spdControl;
	private       boolean              spdControlEnabled = false;
	private       boolean              motorReversed = false;
	
	public MotorLink(Jaguar motor) {this.motor = motor;}
	public MotorLink(Jaguar motor, AbstractEncoder enc) {this(motor); this.encoder = enc;}
	public MotorLink(Jaguar motor, MotorSpeedControl spdControl) {this(motor); this.spdControl = spdControl;}
	public MotorLink(Jaguar motor, AbstractEncoder enc, MotorSpeedControl spdControl) {this(motor, spdControl); this.encoder = enc;}
	
	public double getSpeed() {return motor.get();}
	public void setSpeed(double spd) {
		if(spdControlEnabled) {
			DebugLog.log(DebugLog.LVL_WARN, this, "The speed of the motor was set while a controller was active! Disabling speed controller...");
			this.stopSpeedControl();
		}
		
		this.motor.set(reversedCheck(spd));
	}
	
	public void setEncoder(AbstractEncoder encoder) {
		if(encoder != null) DebugLog.log(DebugLog.LVL_INFO, this, "The encoder was changed in the motor!");
		this.encoder = encoder;
	}
	public double getEncoderAngle() {return MotorMath.normalizeAngle(encoder.getAngle());}
	
	public void setSpeedControl(MotorSpeedControl spdControl) {
		if(spdControlEnabled) DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was set while another was active!");
		spdControlEnabled = !spdControlEnabled; this.spdControl = spdControl;
	}
	public void stopSpeedControl() {this.spdControlEnabled = false; spdControl.cancelEvent();}
	public void startSpeedControl() {this.spdControlEnabled = true; spdControl.registerIterableEvent();}
	public void setSpeedControlTarget(double d) {this.spdControl.setTarget(d);}
	public double getControlledValue() {return this.getEncoderAngle();}
	
	public void reverseMotor() {this.motorReversed = !this.motorReversed;}
	public boolean getMotorReversed() {return this.motorReversed;}
	private double reversedCheck(double spd) {return spd * (motorReversed ? -1.0 : 1.0);}
}
