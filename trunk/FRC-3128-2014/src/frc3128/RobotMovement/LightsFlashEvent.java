package frc3128.RobotMovement;

import edu.wpi.first.wpilibj.Relay;
import frc3128.EventManager.Event;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class LightsFlashEvent extends Event {
    private final Relay lights;
    public LightsFlashEvent(Relay lights) {this.lights = lights;}
    
    public void execute() {
        if(System.currentTimeMillis() % 500 < 250)
            this.lights.set(Relay.Value.kOff);
        else
            this.lights.set(Relay.Value.kForward);
    }
}
