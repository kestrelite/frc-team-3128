package frc3128.Targeting;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;

/*public class TiltToAngle extends SequenceEvent {

    private final double thresh = 1;
    private double targetAngle = -1;

    public TiltToAngle(double desire) {
        targetAngle = desire;
    }

    public void setTurnAngle(double desire) {
        targetAngle = desire;
    }

    public void execute() {
        if (Math.abs(Global.gTilt.getAngle() - this.targetAngle) > thresh)
            Global.msTilt.overridePower(0.4);
        DebugLog.log(4, referenceName, "Lift angle: " + Global.gTilt.getAngle());
    }

    public boolean exitConditionMet() {
        if(Math.abs(Global.gTilt.getAngle() - this.targetAngle) < thresh) {
            Global.msTilt.overridePower(0.15);
            return true;
        }
        return false;
    }
}*/