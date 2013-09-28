package frc3128.HardwareLink.Encoder;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Noah Sutton-Smolin
 */
//TODO Test MagneticPotEncoder
public class MagneticPotEncoder extends AbstractEncoder {
    private AnalogChannel enc;
    
    public MagneticPotEncoder(int a, int b) {
        enc = new AnalogChannel(a, b);
    }
    
    /**
     * Gets the approximated angle from a magnetic encoder. It uses values which
     * have been estimated to high accuracy from extensive tests. Unless need be
     * , do not modify these values.
     * 
     * @return the approximate angle from 0 to 360 degrees of the encoder
     */
    public double getAngle() {
        double voltage = 0;//, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += enc.getVoltage();
            //value += enc.getValue();
        }
        voltage /= 10; //value /= 10;
        return voltage/5.0*360;
    }

    /**
     * 
     * @return the raw voltage of the encoder
     */
    public double getRawValue() {return enc.getVoltage();}
}