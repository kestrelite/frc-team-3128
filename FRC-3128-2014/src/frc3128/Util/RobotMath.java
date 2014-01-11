package frc3128.Util;

import com.sun.squawk.util.MathUtils;
import frc3128.HardwareLink.Motor.MotorDir;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class RobotMath {

    /**
     * Limits the angle to between 0 and 359 degrees for all math. All angles
     * should be normalized before use.
     * <p/>
     * @param angle the angle to be normalized
     * <p/>
     * @return the normalized angle on [-179, 180]
     */
    public static double normalizeAngle(double angle) {
        double theta = ((angle % 360) + 360) % 360;
        if (theta > 180) {
            return (theta - 360);
        }
        return theta;
    }

    public static double angleDistance(double angle1, double angle2) {
        return normalizeAngle(angle2) - normalizeAngle(angle1);
    }

    public static double sgn(double n) {
        return Math.abs(n)/n;
    }
    
    /**
     *
     * @param pow motor power
     * <p/>
     * @return whether or not the power is valid
     */
    public static boolean isValidPower(double pow) {
        return (pow >= -1 && pow <= 1);
    }

    /**
     * Makes the provided power into a valid power level.
     * <p/>
     * @param pow power level to convert
     * <p/>
     * @return a properly-limited power level
     */
    public static double makeValidPower(double pow) {
        return (pow < -1 ? -1 : (pow > 1 ? 1 : pow));
    }

    /**
     * Determines the appropriate direction for a motor to turn.
     * <p/>
     * @param currentAngle the current angle of the motor
     * @param targetAngle the target angle of the motor
     * <p/>
     * @return an integer value on {-1,0,1} in MotorDir
     */
    public static int getMotorDirToTarget(double currentAngle, double targetAngle) {
        currentAngle = RobotMath.normalizeAngle(currentAngle);
        targetAngle = RobotMath.normalizeAngle(targetAngle);
        int retDir = 1 * (Math.abs(currentAngle - targetAngle) > 180 ? 1 : -1) * (currentAngle - targetAngle < 0 ? -1 : 1);

        //MotorDir is syntactic sugar; could just return retDir
        if (currentAngle - targetAngle == 0 || currentAngle - targetAngle == 180) {
            return MotorDir.NONE;
        }
        return (retDir == 1 ? MotorDir.CW : MotorDir.CCW);
    }

    public static double dTR(double angle) {
        return Math.PI * angle / 180.0;
    }

    public static double rTD(double angle) {
        return angle * (180.0 / Math.PI);
    }

    private RobotMath() {
    }
}
