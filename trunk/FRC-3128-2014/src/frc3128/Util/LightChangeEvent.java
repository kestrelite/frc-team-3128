package frc3128.Util;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc3128.EventManager.Event;
import frc3128.HardwareLink.RelayLink;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class LightChangeEvent extends Event {
    private final RelayLink redLights, blueLights;
    
    /**
     * Will change the color of two relays to the colors of your alliance
     * 
     * @param redLights  The red light relays
     * @param blueLights The blue light relays
     */
    public LightChangeEvent(RelayLink redLights, RelayLink blueLights) {
        this.redLights = redLights; this.blueLights = blueLights;
    }

    public void execute() {
        if(Constants.getAlliance().equals(Alliance.kBlue)) {redLights.setOff(); blueLights.setOn();} 
            else if(Constants.getAlliance().equals(Alliance.kRed)) {redLights.setOn(); blueLights.setOff();} 
            else {redLights.setOff(); blueLights.setOff();}
    }
}
