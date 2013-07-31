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
	 * @param portA the first port of the gyro
	 * @param portB the second port of the gyro
	 */
	public GyroLink(int portA, int portB) {gyr = new Gyro(portA, portB);}
	
	public double getAngle() {return gyr.getAngle();}
}
