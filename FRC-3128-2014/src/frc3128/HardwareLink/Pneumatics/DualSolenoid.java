package frc3128.HardwareLink.Pneumatics;

import edu.wpi.first.wpilibj.Solenoid;
import frc3128.Util.DebugLog;

/**
 * 
 * @author Noah Sutton-Smolin
 */
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
        solA.set(true);
        solB.set(false);
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to off-state");
    }
    
    /**
     * Sets B to !B and A to !A, inverting the piston's current state.
     */
    protected void setPistonInverted() {
        solA.set(!solA.get());
        solB.set(!solB.get());
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to flip-state " + solA.get() + ", " +solB.get());
    }
}