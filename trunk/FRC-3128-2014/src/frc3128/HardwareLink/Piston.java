package frc3128.HardwareLink;

import edu.wpi.first.wpilibj.Solenoid;
import frc3128.Util.DebugLog;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class Piston {
    private final Solenoid solA, solB;
    private boolean inversion = false;
    
    public Piston(Solenoid solA, Solenoid solB) {
        this.solA = solA; this.solB = solB;
    }
    
    public Piston(Solenoid solA, Solenoid solB, boolean solStateA, boolean solStateB) {
        this(solA, solB);
        solA.set(solStateA); solB.set(solStateB);
    }
    
    public void invertPiston() {this.inversion = !inversion;}
    
    public void lockPiston() {
        solA.set(true);
        solB.set(true);
        DebugLog.log(DebugLog.LVL_STREAM, this, this + " set to locked state");
    }

    public void unlockPiston() {
        solA.set(false);
        solB.set(false);
        DebugLog.log(DebugLog.LVL_STREAM, this, this + " set to unlocked state");
    }
    
    public void setPistonOn() {
        solA.set(true ^ this.inversion);
        solB.set(false ^ this.inversion);
        DebugLog.log(DebugLog.LVL_STREAM, this, this + " set to on state");
    }
    
    public void setPistonOff() {
        solA.set(false ^ this.inversion);
        solB.set(true ^ this.inversion);
        DebugLog.log(DebugLog.LVL_STREAM, this, this + " set to off state");
    }
    
    public void setPistonInvert() {
        solA.set(!solA.get());
        solB.set(!solB.get());
        DebugLog.log(DebugLog.LVL_STREAM, this, this + " set to flip-state " + solA.get() + ", " +solB.get());
    }
}
