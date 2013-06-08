package frc3128.AutoTarget;

import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

public class AutoFire extends SequenceEvent {
    public boolean exitConditionMet() {
        return (this.getRunTimeMillis() > 11000);
    }

    private long execTime = -1;
    public void execute() {
        if(execTime == -1) execTime = this.getRunTimeMillis() + 3000;
        if(this.getRunTimeMillis() > execTime) {
            PneumaticsManager.setPistonInvertState(Global.pstFire);
            execTime = this.getRunTimeMillis() + 3000;
        }
    }
}