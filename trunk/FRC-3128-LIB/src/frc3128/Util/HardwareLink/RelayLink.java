package frc3128.Util.HardwareLink;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class RelayLink {
	private Relay rel;
	
	public RelayLink(int portA, int portB, Direction dir) {
		rel = new Relay(portA, portB, dir);
	}
}
