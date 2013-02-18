package frc3128.DriveTank;

import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.ListenerManager.ListenerManager;

class StopDrive extends Event {
    public void execute() {
        Global.mLB.set(0);
        Global.mLF.set(0);
        Global.mRB.set(0);
        Global.mRF.set(0);        
    }
}

public class TurnToAngle extends Event {

    private final double thresh = 1;
    private double targetAngle = -1;

    public void setTurnAngle(double desire) {
        targetAngle = desire;
        this.registerIterableEvent();
    }

    public void execute() {
        if (1 == 1) throw new Error("Must set gyro polarization");
            
        if (Math.abs(Global.gTurn.getAngle() - this.targetAngle) > thresh) {

            Global.mLB.set(((this.targetAngle - Global.gTurn.getAngle()) / 90) + .2);
            Global.mLF.set(((this.targetAngle - Global.gTurn.getAngle()) / 90) + .2);
            Global.mRB.set(((this.targetAngle - Global.gTurn.getAngle()) / -90) + .2);
            Global.mRF.set(((this.targetAngle - Global.gTurn.getAngle()) / -90) + .2);
        } else
            (new StopDrive()).registerSingleEvent();
    }
}
