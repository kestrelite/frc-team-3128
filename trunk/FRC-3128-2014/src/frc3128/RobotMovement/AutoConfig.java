package frc3128.RobotMovement;

import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.EventSequence.DelaySequence;
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
            public void execute() {
                Global.rotBk.setControlTarget(90);
                Global.rotFL.setControlTarget(90);
                Global.rotFR.setControlTarget(90);
                
                Global.drvBk.setSpeed(1.0);
                Global.drvFL.setSpeed(1.0);
                Global.drvFR.setSpeed(1.0);
            }
        });
        es.addEvent(new SingleSequence() { //Stop motors
            public void execute() {
                Global.drvBk.setSpeed(0);
                Global.drvFL.setSpeed(0);
                Global.drvFR.setSpeed(0);
            }
        });
        es.addEvent(new SequenceEvent() { //Wait if the goal is not hot
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 5000 || AutoConfig.isHot();}
            public void execute() {}
        });

        es.addEvent(new SingleSequence() { //Stop the arm cocking event
            public void execute() {Global.cockShooter.cancelEvent();}
        });
        es.addEvent(new SequenceEvent() { //Run arm until cocked
            public boolean exitConditionMet() {return Global.shooterTSensor.get();}
            public void execute() {Global.mShooter.setSpeed(-1.0);}
        });
        es.addEvent(new SequenceEvent() { //Run arm until launched
            public boolean exitConditionMet() {return !Global.shooterTSensor.get();}
            public void execute() {Global.mShooter.setSpeed(-1.0);}
        });        
        es.addEvent(new SingleSequence() { //Re-enable the arm cocking event for teleop
            public void execute() {Global.cockShooter.registerIterableEvent();}
        });
        es.addEvent(new DelaySequence(1000));

        es.addEvent(new SequenceEvent() { //Drive forward into goal
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 2500;}
            public void execute() {
                Global.rotBk.setControlTarget(90+Global.gyr.getAngle());
                Global.rotFL.setControlTarget(90+Global.gyr.getAngle());
                Global.rotFR.setControlTarget(90+Global.gyr.getAngle());
                
                Global.drvBk.setSpeed(1.0);
                Global.drvFL.setSpeed(1.0);
                Global.drvFR.setSpeed(1.0);
            }
        });
       es.addEvent(new SingleSequence() { //Stop motors
            public void execute() {
                Global.drvBk.setSpeed(0);
                Global.drvFL.setSpeed(0);
                Global.drvFR.setSpeed(0);
            }
        });
        es.addEvent(new SingleSequence() { //Do a dance
            public void execute() {
                new LightsFlashEvent(Global.blueLights, true, 1250).registerIterableEvent();
                new LightsFlashEvent(Global.redLights, false, 1250).registerIterableEvent();
            }
        });
        es.startSequencer();
    }
    
    public static boolean isHot() {
        DebugLog.log(DebugLog.LVL_SEVERE, "AutoConfig", "isHot event missing!");
        return false;
    }
    
    private AutoConfig() {}
}
