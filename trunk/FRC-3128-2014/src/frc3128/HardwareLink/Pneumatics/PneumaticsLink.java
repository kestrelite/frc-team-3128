package frc3128.HardwareLink.Pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import frc3128.Util.DebugLog;
import java.util.Vector;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class PneumaticsLink {
    private   static Compressor c;
    protected static Vector dualSolenoidList = new Vector();

    /**
     * Sets up and initialized the PneumaticsManager.
     * 
     * @param comp The compressor to be initialized 
     */
    public PneumaticsLink(Compressor comp) {
        PneumaticsLink.c = comp;
        PneumaticsLink.c.stop();
    }
    
    /**
     * Sets the compressor. Do not call this unless needed.
     *
     * @param comp The compressor to be used
     */
    public void setCompressor(Compressor comp) {
        if(PneumaticsLink.c != null) DebugLog.log(DebugLog.LVL_SEVERE, "PneumaticsLink", "Pneumatic compressor was changed!");
        PneumaticsLink.c = comp;
        PneumaticsLink.c.stop();
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
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonInvertState();
    }
    
    /**
     * Starts the compressor.
     */
    public static void setCompressorStateOn() {PneumaticsLink.c.start();}

    /**
     * Stops the compressor.
     */
    public static void setCompressorStateOff() {PneumaticsLink.c.stop();}

    /**
     * 
     * @return whether the compressor is on or off
     */
    public static boolean getCompressorState() {
        return (PneumaticsLink.c.enabled() ? true : false);
    }
    
    /**
     * Locks all pistons.
     */
    public static void lockAllPistons() {
        for(int i = 0; i < dualSolenoidList.size(); i++)
            ((DualSolenoid) dualSolenoidList.elementAt(i)).lockPiston();
    }
}

class DualSolenoid {
    private Solenoid solA;
    private Solenoid solB;

    /**
     * Links two solenoids together, typically a Festo valve.
     * 
     * @param solA      the first solenoid valve
     * @param solB      the second solenoid valve
     * @param solAState the initial solenoid state for the first valve
     * @param solBState the initial solenoid state for the second valve
     */
    protected DualSolenoid(Solenoid solA, Solenoid solB, boolean solAState, boolean solBState) {
        this.solA = solA;
        this.solB = solB;
        solA.set(solAState);
        solB.set(solBState);
    }

    /**
     * Sets both solenoid states to "true"; locks the piston in place.
     */
    protected void lockPiston() {
        solA.set(true);
        solB.set(true);
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to lock-state");
    }

    /**
     * Sets both solenoid states to "false"; also typically locks the piston in 
     * place.
     */
    protected void unlockPiston() {
        solA.set(false);
        solB.set(false);
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to unlock-state");
    }

    /**
     * Sets A to true and B to false; generically called "on".
     */
    protected void setPistonOn() {
        solA.set(true);
        solB.set(false);
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to on-state");
    }

    /**
     * Sets B to true and A to false; generically called "off".
     */
    protected void setPistonOff() {
        solB.set(true);
        solA.set(false);
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to off-state");
    }
    
    /**
     * Sets B to !B and A to !A, inverting the piston's current state.
     */
    protected void setPistonInvertState() {
        solA.set(!solA.get());
        solB.set(!solB.get());
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to flip-state " + solA.get() + ", " +solB.get());
    }
}