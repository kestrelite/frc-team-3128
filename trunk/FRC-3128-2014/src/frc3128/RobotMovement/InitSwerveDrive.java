package frc3128.RobotMovement;

import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.Global;

public class InitSwerveDrive extends SequenceEvent {
    private final double epsilon = 4;
    public void execute() {
        Global.rotFL.setSpeed(Global.rotFL.getEncoderAngle() < epsilon ? 0 : .5);
        Global.rotFR.setSpeed(Global.rotFR.getEncoderAngle() < epsilon ? 0 : .5);
        Global.rotBk.setSpeed(Global.rotBk.getEncoderAngle() < epsilon ? 0 : .5);
    }
    public boolean exitConditionMet() {
        return Global.rotFL.getEncoderAngle() < epsilon && 
               Global.rotFR.getEncoderAngle() < epsilon && 
               Global.rotBk.getEncoderAngle() < epsilon;           
    }
}
