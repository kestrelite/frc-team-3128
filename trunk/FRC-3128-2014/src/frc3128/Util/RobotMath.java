package frc3128.Util;

import frc3128.HardwareLink.Motor.MotorDir;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class RobotMath {
	/**
	 * Limits the angle to between 0 and 360 degrees for all math. All angles
	 * should be normalized before use.
	 * 
	 * @param angle the angle to be normalized
	 * @return the normalized angle on [0,360]
	 */
	public static double normalizeAngle(double angle) {
		double out = angle;
		while(out < 0) out += 360;
		while(out > 360) out -= 360;
		return out;
	}
	
	/**
	 * Determines the appropriate direction for a motor to turn.
	 * 
	 * @param currentAngle the current angle of the motor
	 * @param targetAngle the target angle of the motor
	 * @return an integer value on {-1,0,1} in MotorDir
	 */
	public static int getMotorDirToTarget(double currentAngle, double targetAngle) {
		currentAngle = RobotMath.normalizeAngle(currentAngle);
		targetAngle = RobotMath.normalizeAngle(targetAngle);
		int retDir = 1 * (Math.abs(currentAngle - targetAngle) > 180 ? 1 : -1) * (currentAngle - targetAngle < 0 ? -1 : 1);
		
		//MotorDir is syntactic sugar; could just return retDir
		if(currentAngle - targetAngle == 0 || currentAngle - targetAngle == 180) return MotorDir.EITHER;
		return (retDir == 1 ? MotorDir.CW : MotorDir.CCW); 
	}
	
	private RobotMath() {}
}
