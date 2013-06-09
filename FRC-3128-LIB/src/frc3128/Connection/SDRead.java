package frc3128.Connection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDRead {
	public static void writeToDashboard(String name, String data) {SmartDashboard.putString(name, data);}
	public static void writeToDashboard(String name, double d) {SmartDashboard.putNumber(name, d);}
	public static void writeToDashboard(String name, int d) {SmartDashboard.putNumber(name, d);}
	public static void writeToDashboard(String name, boolean b) {SmartDashboard.putBoolean(name, b);}
	
	private SDRead() {}
}
