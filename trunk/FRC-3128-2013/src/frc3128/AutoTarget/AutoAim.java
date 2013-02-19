package frc3128.AutoTarget;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.Targeting.TiltTarget;
import frc3128.Targeting.TurnToCenter;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;
import frc3128.PneumaticsManager.PneumaticsManager;

public class AutoAim extends Event {
    EventSequencer aAim = new EventSequencer();
    public void execute() {
        aAim.addEvent(new AutoTurn());
        //aAim.addEvent(new AutoTilt());
        //aAim.addEvent(new AutoFire());
        aAim.startSequence();
    }
}

class AutoTurn extends SequenceEvent {
    double thresh = 5.0;
    
    public boolean exitConditionMet() {
        return (NetworkTable.getTable("camera").getNumber("xoffset") < thresh ? true : false);
    }

    public void execute() {
        (new TurnToCenter()).registerIterableEvent();
    }
}

class AutoTilt extends SequenceEvent {
    public boolean exitConditionMet() {
        return true;
    }

    public void execute() {
        (new TiltTarget()).registerIterableEvent();
    }
}

class AutoFire extends SequenceEvent {
    public boolean exitConditionMet() {
        return (this.getRunTimeMillis() > 1750);
    }

    private long lastTime = -1;
    public void execute() {
        if(lastTime < 750  && this.getRunTimeMillis() >=  750) PneumaticsManager.setPistonInvertState(Global.pstFire);
        if(lastTime < 1500 && this.getRunTimeMillis() >= 1500) PneumaticsManager.setPistonInvertState(Global.pstFire);
        lastTime = this.getRunTimeMillis();
    }
}