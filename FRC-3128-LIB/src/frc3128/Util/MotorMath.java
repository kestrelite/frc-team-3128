package frc3128.Util;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class MotorMath {
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
	
	private MotorMath() {}
}
