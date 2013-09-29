package frc3128.HardwareLink.Encoder;

import edu.wpi.first.wpilibj.Encoder;

/**

 @author Noah Sutton-Smolin
 */
public class OpticalDiskEncoder extends AbstractEncoder {
    private final Encoder enc;
    
    public OpticalDiskEncoder(Encoder e) {this.enc = e;}
    
    public double getAngle() {
        return enc.getRaw()/4.0;
    }

    public double getRawValue() {
        return enc.getRaw();
    }
}
