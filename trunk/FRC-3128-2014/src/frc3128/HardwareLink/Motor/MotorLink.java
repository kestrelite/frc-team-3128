package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class MotorLink {
	private final Jaguar            motor;
	private       AbstractEncoder   encoder;
	private       MotorSpeedControl spdControl;
	private       boolean           spdControlEnabled = false;
	private       boolean           motorReversed = false;
	private       double            powerScalar = 1;
	
	/**
	 * 
	 * @param motor the linked motor
	 */
	public MotorLink(Jaguar motor) {this.motor = motor;}
	
	/**
	 * 
	 * @param motor the linked motor
	 * @param enc the linked encoder
	 */
	public MotorLink(Jaguar motor, AbstractEncoder enc) {this(motor); this.encoder = enc;}
	
	/**
	 * 
	 * @param motor the linked motor
	 * @param enc the linked encoder
	 * @param spdControl the linked speed controller
	 */
	public MotorLink(Jaguar motor, AbstractEncoder enc, MotorSpeedControl spdControl) {this(motor, enc); this.spdControl = spdControl;}
	
	/**
	 * 
	 * @return the current speed of the motor
	 */
	public double getSpeed() {return motor.get();}
	
	/**
	 * Sets the speed of the motor. 
	 * 
	 * @param spd the speed to be set
	 */
	public void setSpeed(double spd) {
		if(spdControlEnabled) {
			DebugLog.log(DebugLog.LVL_WARN, this, "The speed of the motor was set while a controller was active! Disabling speed controller...");
			this.stopSpeedControl();
		}
		
		this.motor.set(reversedCheck(spd)*this.powerScalar);
	}
	protected void setSpeedControlled(double spd) {this.motor.set(spd);}
	
	/**
	 * Sets and enables an encoder to be linked with a motor.
	 * 
	 * @param encoder the Encoder to be linked
	 */
	public void setEncoder(AbstractEncoder encoder) {
		if(encoder != null) DebugLog.log(DebugLog.LVL_INFO, this, "The encoder was changed in the motor!");
		this.encoder = encoder;
	}

	/**
	 * 
	 * @return the current encoder angle
	 */
	public double getEncoderAngle() {
		if(encoder == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The encoder heading was read, but no encoder was set!"); return -1;}
		return RobotMath.normalizeAngle(encoder.getAngle());
	}
	
	/**
	 * Clears, then sets a speed controller for the motor.
	 * 
	 * @param spdControl the speed controller to be enabled on the motor
	 */
	public void setSpeedControl(MotorSpeedControl spdControl) {
		spdControl.clearControlRun();
		if(spdControlEnabled) {
			DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was set while another was active!");
			this.motor.set(0); this.stopSpeedControl();
		}
		this.spdControl = spdControl;
	}
	
	/**
	 * Stops the running speed control.
	 */
	public void stopSpeedControl() {
		this.motor.set(0); this.spdControl.clearControlRun();
		if(!this.spdControlEnabled) DebugLog.log(DebugLog.LVL_WARN, this, "The speed controller was disabled when it was not active!");
		if(spdControl == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was stopped when it did not exist!"); return;}
		this.spdControlEnabled = false; spdControl.cancelEvent();
	}
	
	/**
	 * Enables the speed controller on the current motor.
	 */
	public void startSpeedControl() {
		this.spdControl.clearControlRun();
		if(spdControl == null) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The speed controller was started when it did not exist!"); return;}
		this.spdControlEnabled = true; spdControl.registerIterableEvent();
	}
	
	/**
	 * Sets the speed controller's target - will be used however the speed
	 * controller has defined it to be.
	 * 
	 * @param val the value to be sent to the speed controller 
	 */
	public void setSpeedControlTarget(double val) {this.spdControl.setControlTarget(val);}
	
	/**
	 * Inverts the motor; clockwise becomes counterclockwise and vice versa.
	 */
	public void reverseMotor() {this.motorReversed = !this.motorReversed;}

	/**
	 * Sets the power scalar for the motor
	 * 
	 * @param pwrScale the power scalar from 0 to 1 to be used for speeds
	 */
	public void setPowerScalar(double pwrScale) {
		if(pwrScale < 0 || pwrScale > 1) {DebugLog.log(DebugLog.LVL_SEVERE, this, "The power scalar was set to an illegal value: " + pwrScale + "; scalar not updated!"); return;}
		this.powerScalar = pwrScale;
	}
	
	/**
	 * 
	 * @return the current power scalar
	 */
	public double getPowerScalar() {return this.powerScalar;}

	
	/**
	 * 
	 * @return whether or not the motor is reversed
	 */
	public boolean getMotorReversed() {return this.motorReversed;}
	private double reversedCheck(double spd) {return spd * (motorReversed ? -1.0 : 1.0);}
}
