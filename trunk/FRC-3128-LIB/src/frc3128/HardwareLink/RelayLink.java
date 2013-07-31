package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class RelayLink {
	private Relay rel;
	
	/**
	 * Instantiates a new relay.
	 * 
	 * @param portA the first port to be used
	 * @param portB the second port to be used
	 * @param dir the initial direction of the relay
	 */
	public RelayLink(int portA, int portB, Direction dir) {
		rel = new Relay(portA, portB, dir);
	}
	
	/**
	 * Sets the relay on.
	 */
	public void setOn() {rel.setDirection(Relay.Direction.kForward);}

	/**
	 * Sets the relay off.
	 */
	public void setOff() {rel.setDirection(Relay.Direction.kReverse);}
}
