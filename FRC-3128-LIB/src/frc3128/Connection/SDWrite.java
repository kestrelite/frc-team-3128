package frc3128.Connection;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class SDWFff {
	private static boolean isConnected = false;
	
	public int getInt(String table, String key) {return (int) NetworkTable.getTable(table).getNumber(key);}
	public double getDouble(String table, String key) {return NetworkTable.getTable(table).getNumber(key);}
	public String getString(String table, String key) {return NetworkTable.getTable(table).getString(key);}
}
