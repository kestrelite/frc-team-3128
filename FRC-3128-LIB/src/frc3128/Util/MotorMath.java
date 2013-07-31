package frc3128.Util;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class MotorMath {
	public static double normalizeAngle(double angle) {
		double out = angle;
		while(out < 0) out += 360;
		while(out > 360) out -= 360;
		return out;
	}
	
	private MotorMath() {}
}
