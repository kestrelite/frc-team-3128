package frc3128.Connection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDRead {
	/**
	 * Writes String data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name The key for which the data will be written.
	 * @param data The String data to be written.
	 */
	public static void writeToDashboard(String name, String data) {SmartDashboard.putString(name, data);}
	
	/**
	 * Writes double data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name The key for which the data will be written.
	 * @param data The double data to be written.
	 */
	public static void writeToDashboard(String name, double d) {SmartDashboard.putNumber(name, d);}
	
	/**
	 * Writes integer data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name The key for which the data will be written.
	 * @param data The integer data to be written.
	 */
	public static void writeToDashboard(String name, int d) {SmartDashboard.putNumber(name, d);}
	
	/**
	 * Writes boolean data to the SmartDashboard, if it is open and connected.
	 * 
	 * @param name The key for which the data will be written.
	 * @param data The boolean data to be written.
	 */
	public static void writeToDashboard(String name, boolean b) {SmartDashboard.putBoolean(name, b);}
	
	private SDRead() {}
}
