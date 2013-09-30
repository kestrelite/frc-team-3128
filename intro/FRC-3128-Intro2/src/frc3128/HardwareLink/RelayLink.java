package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class RelayLink {
    private final Relay rel;
    
    /**
     * Instantiates a new relay.
     * 
     * @param rel the relay to be linked
     */
    public RelayLink(Relay rel) {this.rel = rel;}
    
    /**
     * Sets the relay on.
     */
    public void setOn() {rel.setDirection(Relay.Direction.kForward);}

    /**
     * Sets the relay off.
     */
    public void setOff() {rel.setDirection(Relay.Direction.kReverse);}
}
