package frc3128.RobotMovement;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.HardwareLink.RelayLink;
import frc3128.Util.LightChangeEvent;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class LightsFlashEvent extends Event {
    private final RelayLink lights;
    private final boolean reverse;
    private final int delay;
    public LightsFlashEvent(RelayLink lights) {this(lights, false);}
    public LightsFlashEvent(RelayLink lights, boolean reverse) {this(lights, reverse, 500);}
    public LightsFlashEvent(RelayLink lights, boolean reverse, int delay) {this.lights = lights; this.reverse = reverse; this.delay = delay;}
    
    public void execute() {
        if((System.currentTimeMillis() % delay < (delay / 2)) ^ reverse)
            this.lights.setOff();
        else
            this.lights.setOn();
    }
    
    public void cancelCustom() {
        new LightChangeEvent(Global.redLights, Global.blueLights).registerSingleEvent();
        this.cancelEvent();
    }
}
