package frc3128.HardwareLink.Pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
//TODO Test pneumatics systems
public class PneumaticsManager {
    private   static Compressor c;
    protected static Vector dualSolenoidList = new Vector();

    /**
     * Sets up and initialized the PneumaticsManager.
     * 
     * @param comp The compressor to be initialized 
     */
    public PneumaticsManager(Compressor comp) {
        PneumaticsManager.c = comp;
        PneumaticsManager.c.stop();
    }
    
    /**
     * Adds a piston to the DualSolenoid list
     * 
     * @param solA      the first solenoid
     * @param solB      the second solenoid
     * @param solAState the first solenoid's initial state
     * @param solBState the second solenoid's initial state
     * @return a PistonID for referencing the given piston
     */
    public static PistonID addPiston(Solenoid solA, Solenoid solB, boolean solAState, boolean solBState) {
        dualSolenoidList.addElement(new DualSolenoid(solA, solB, solAState, solBState));
        return new PistonID(dualSolenoidList.size() - 1);
    }

    /**
     * Sets a piston to the locked state (ignores inversion).
     * 
     * @param id the PistonID to be set
     */
    public static void setPistonStateLocked(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).lockPiston();
    }

    /**
     * Sets a piston to the unlocked state (ignores inversion).
     * 
     * @param id the PistonID to be set
     */
    public static void setPistonStateUnlocked(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).unlockPiston();
    }

    /**
     * Sets a piston to the "on" state ("off" if inverted).
     * 
     * @param id the PistonID to be set
     */
    public static void setPistonStateOn(PistonID id) {
        if(!id.getInversion()) 
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOn();
        else
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOff();
    }

    /**
     * Sets a piston to the "off" state ("on" if inverted).
     * 
     * @param id the PistonID to be set
     */
    public static void setPistonStateOff(PistonID id) {
        if(!id.getInversion())
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOff();
        else
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOn();
    }

    /**
     * Sets a piston to its inverted state (ignores inversion).
     * 
     * @param id the PistonID to be inverted
     */
    public static void setPistonInvertState(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonInverted();
    }
    
    /**
     * Starts the compressor.
     */
    public static void setCompressorStateOn() {PneumaticsManager.c.start();}

    /**
     * Stops the compressor.
     */
    public static void setCompressorStateOff() {PneumaticsManager.c.stop();}

    /**
     * 
     * @return whether the compressor is on or off
     */
    public static boolean getCompressorState() {
        return (PneumaticsManager.c.enabled() ? true : false);
    }
    
    /**
     * Locks all pistons.
     */
    public static void lockAllPistons() {
        for(int i = 0; i < dualSolenoidList.size(); i++)
            ((DualSolenoid) dualSolenoidList.elementAt(i)).lockPiston();
    }
}