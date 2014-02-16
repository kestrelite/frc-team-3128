package frc3128.RobotMovement;

import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.Global;
import frc3128.Util.DebugLog;

/**
 *
 * @author Noah Sutton-Smolin
 */
public class AutoConfig {
    public static void initialize() {
        EventSequencer es = new EventSequencer();
        es.addEvent(new SequenceEvent() { //Drive forward to shooting range
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 750;}
            public void execute() {DebugLog.log(DebugLog.LVL_SEVERE, this, "Drive event missing!");/*Drive forward*/}
        });
        es.addEvent(new SequenceEvent() { //Wait if the goal is not hot
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 6000 || AutoConfig.isHot();}
            public void execute() {}
        });
        es.addEvent(new SingleSequence() { //Stop the arm cocking event
            public void execute() {Global.cockArm.cancelEvent();}
        });
        es.addEvent(new SequenceEvent() { //Run the shooter
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 500;}
            public void execute() {Global.mShooter.setSpeed(-1.0);}
        });
        es.addEvent(new SingleSequence() { //Re-enable the arm cocking event for teleop
            public void execute() {Global.cockArm.registerIterableEvent();}
        });
        es.addEvent(new SequenceEvent() { //Drive forward into goal
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 2500;}
            public void execute() {DebugLog.log(DebugLog.LVL_SEVERE, this, "Drive event missing!");/*Drive forward*/}
        });
        es.startSequencer();
    }
    
    public static boolean isHot() {
        DebugLog.log(DebugLog.LVL_SEVERE, "AutoConfig", "isHot event missing!");
        return true;
    }
    
    private AutoConfig() {}
}
