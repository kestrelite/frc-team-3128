package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class GyroLink {
	private final Gyro gyr;
	
	/**
	 * Creates a new GyroLink.
	 * 
	 * @param gyr The gyro to be linked
	 */
	public GyroLink(Gyro gyr) {this.gyr = gyr;}
	
	public double getAngle() {return gyr.getAngle();}
}
