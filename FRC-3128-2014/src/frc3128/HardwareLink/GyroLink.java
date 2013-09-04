package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class GyroLink {
    private final Gyro gyr;
    
    /**
     * Creates a new linked gyroscope.
     * 
     * @param gyr the gyroscope to be created
     */
    public GyroLink(Gyro gyr) {this.gyr = gyr;}

    /**
     * 
     * @return the current gyroscope angle
     */
    public double getAngle() {return gyr.getAngle();}
    
    /**
     * Resets the stored angle
     */
    public void resetAngle() {gyr.reset();}

}
