package frc3128.Targeting;

import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;

public class TurnToAngle extends SequenceEvent {

    private final double thresh = 1;
    private double targetAngle = -1;

    public TurnToAngle(double desire) {
        targetAngle = desire;
    }

    public void setTurnAngle(double desire) {
        targetAngle = desire;
    }

    public void execute() {
        if (Math.abs(Global.gTurn.getAngle() - this.targetAngle) > thresh) {
            Global.mLF.set(((this.targetAngle - Global.gTurn.getAngle()) / 90) + .3);
            Global.mRF.set(((this.targetAngle - Global.gTurn.getAngle()) / -90) - .3);
        } else {
            Global.stopMotors();
        }
    }

    public boolean exitConditionMet() {
        if(Math.abs(Global.gTurn.getAngle()) > 30) Global.robotStop();
        if(Math.abs(Global.gTurn.getAngle() - this.targetAngle) < thresh) {
            Global.mLF.set(0);
            Global.mRF.set(0);
            return true;
        }
        return false;
    }
}