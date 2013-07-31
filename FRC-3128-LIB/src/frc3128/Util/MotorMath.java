package frc3128.Util;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class MotorMath {
	/**
	 * Gets the approximated angle from a magnetic encoder. It uses values which
	 * have been estimated to high accuracy from extensive tests. Unless need be
	 * , do not modify these values.
	 * 
	 * @param channel The analog channel to be read from
	 * @return The approximate angle from 0 to 360 degrees of the encoder
	 */
	public static double getAngleFrom(AnalogChannel channel) {
        double voltage = 0, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += channel.getVoltage();
            value += channel.getValue();
        }
        voltage /= 10; value /= 10;
        return voltage/5.0*360;
    }
	
	private MotorMath() {}
}
