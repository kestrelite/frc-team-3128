package frc3128.HardwareLink.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import frc3128.HardwareLink.Encoder.AbstractEncoder;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

/**
 * 
 * @author Noah Sutton-Smolin
 */
//TODO Test MotorLink
public class MotorLink {
	private final Jaguar            motor;
	private       AbstractEncoder   encoder;
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
	 * @return the current speed of the motor
	 */
	public double getSpeed() {return motor.get();}
	
	/**
	 * Sets the speed of the motor. 
	 * 
	 * @param spd the speed to be set
	 */
	public void setSpeed(double spd) {
		if(spd < -1 || spd > 1) spd = (spd < 0 ? -1 : 1);
		
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
