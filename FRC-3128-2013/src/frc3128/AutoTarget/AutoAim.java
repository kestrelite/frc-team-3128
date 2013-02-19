package frc3128.AutoTarget;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc3128.DriveTank.TurnToCenter;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Tilt.TargetTilt;

public class AutoAim extends Event {
    EventSequencer aAim = new EventSequencer();
    public void execute() {
        aAim.addEvent(new AutoTurn());
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
        (new TargetTilt()).registerIterableEvent();
    }
    
}