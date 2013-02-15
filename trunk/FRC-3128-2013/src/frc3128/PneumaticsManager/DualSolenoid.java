package frc3128.PneumaticsManager;

import edu.wpi.first.wpilibj.Solenoid;

class DualSolenoid {
    private Solenoid solA;
    private Solenoid solB;

    protected DualSolenoid(Solenoid solA, Solenoid solB) {
        this.solA = solA;
        this.solB = solB;
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
}