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
		DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to lock-state");
    }

    protected void unlockPiston() {
        solA.set(false);
        solB.set(false);
		DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to unlock-state");
    }

    protected void setPistonOn() {
        solA.set(true);
        solB.set(false);
		DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to on-state");
    }

    protected void setPistonOff() {
        solA.set(true);
        solB.set(false);
		DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to off-state");
    }
    
    protected void setPistonInverted() {
        solA.set(!solA.get());
        solB.set(!solB.get());
        DebugLog.log(DebugLog.LVL_STREAM, "DualSolenoid", "DualSolenoid set to flip-state " + solA.get() + ", " +solB.get());
    }
}