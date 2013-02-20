package frc3128.AutoTarget;

import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

public class AutoFire extends SequenceEvent {
    public boolean exitConditionMet() {
        return (this.getRunTimeMillis() > 2750);
    }

    private long lastTime = -1;
    public void execute() {
        if(lastTime < 1250  && this.getRunTimeMillis() >=  1250) PneumaticsManager.setPistonInvertState(Global.pstFire);
        if(lastTime < 2250 && this.getRunTimeMillis() >= 2250) PneumaticsManager.setPistonInvertState(Global.pstFire);
        lastTime = this.getRunTimeMillis();
    }
}