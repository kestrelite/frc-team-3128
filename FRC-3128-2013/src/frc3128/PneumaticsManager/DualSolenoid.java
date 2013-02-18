package frc3128.PneumaticsManager;

import edu.wpi.first.wpilibj.Solenoid;
import frc3128.DebugLog;

class DualSolenoid {
    private Solenoid solA;
    private Solenoid solB;

    protected DualSolenoid(Solenoid solA, Solenoid solB, boolean solAState, boolean solBState) {
        this.solA = solA;
        this.solB = solB;
        solA.set(solAState);
        solB.set(solBState);
    }

    protected void lockPiston() {
        solA.set(true);
        solB.set(true);
    }

    protected void unlockPiston() {
        solA.set(false);
        solB.set(false);
    }

    protected void setPistonOn() {
        solA.set(true);
        solB.set(false);
    }

    protected void setPistonOff() {
        solA.set(true);
        solB.set(false);
    }
    
    protected void setPistonInverted() {
        solA.set(!solA.get());
        solB.set(!solB.get());
        DebugLog.log(4, "DualSolenoid", "Solenoid set to " + solA.get() + ", " +solB.get());
    }
}