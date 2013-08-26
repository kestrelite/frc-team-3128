package frc3128.Util.Connection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author Yousuf Soliman
 */
public class SDWrite {
	/**
	 * Writes String data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name the key for which the data will be written.
	 * @param data the String data to be written.
	 */
	public static void writeToDashboard(String name, String data) {SmartDashboard.putString(name, data);}
	
	/**
	 * Writes double data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name the key for which the data will be written.
	 * @param data the double data to be written.
	 */
	public static void writeToDashboard(String name, double d) {SmartDashboard.putNumber(name, d);}
	
	/**
	 * Writes integer data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name the key for which the data will be written.
	 * @param data the integer data to be written.
	 */
	public static void writeToDashboard(String name, int d) {SmartDashboard.putNumber(name, d);}
	
	/**
	 * Writes boolean data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name the key for which the data will be written.
	 * @param data the boolean data to be written.
	 */
	public static void writeToDashboard(String name, boolean b) {SmartDashboard.putBoolean(name, b);}
	
	private SDWrite() {}
}
