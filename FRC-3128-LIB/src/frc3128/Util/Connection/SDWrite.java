package frc3128.Util.Connection;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * 
 * @author Yousuf Soliman
 */
public class SDWrite {
	/**
	 * Gets an integer value from the SmartDashboard, if it is open and connected.
	 * 
	 * @param table The name of the table to retrieve from
	 * @param key   The key for the data in the table 
	 * @return an integer value from the table if it exists
	 *              <p>Null if it does not
	 */
	public static int getInt(String table, String key) {return (int) NetworkTable.getTable(table).getNumber(key);}

	/**
	 * Gets a double value from the SmartDashboard, if it is open and connected.
	 * 
	 * @param table The name of the table to retrieve from
	 * @param key   The key for the data in the table 
	 * @return a double value from the table if it exists
	 *              <p>Null if it does not
	 */	
	public static double getDouble(String table, String key) {return NetworkTable.getTable(table).getNumber(key);}

	/**
	 * Gets a double value from the SmartDashboard, if it is open and connected.
	 * 
	 * @param table The name of the table to retrieve from
	 * @param key   The key for the data in the table 
	 * @return a double value from the table if it exists
	 *              <p>Null if it does not
	 */		
	public static String getString(String table, String key) {return NetworkTable.getTable(table).getString(key);}

	private SDWrite() {}
}
