package frc3128.HardwareLink.Encoder;

import edu.wpi.first.wpilibj.Gyro;
import frc3128.HardwareLink.GyroLink;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class GyroEncoder extends AbstractEncoder {
	private GyroLink gyr;
	
	/**
	 * Creates a gyroscope encoder.
	 * 
	 * @param gyr The gyroscope acting as an encoder.
	 */
	public GyroEncoder(Gyro gyr) {this.gyr = new GyroLink(gyr);}
	
	public double getAngle() {return gyr.getAngle();}
	public double getRawValue() {return -1;}
}
