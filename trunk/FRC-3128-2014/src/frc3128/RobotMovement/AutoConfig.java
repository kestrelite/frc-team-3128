package frc3128.RobotMovement;

import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.Global;
import frc3128.Util.Constants;

public class AutoConfig {
    public static void initialize() {
        EventSequencer es = new EventSequencer();
        
        es.addEvent(new SequenceEvent() { //Drive forward to shooting range
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 2800;}
            public void execute() {
                Global.drvBk.setSpeed(-0.50);
                Global.drvFL.setSpeed(-0.50);
                Global.drvFR.setSpeed(-0.50);
            }
        });
        es.addEvent(new SingleSequence() { //Stop motors
            public void execute() {
                Global.drvBk.setSpeed(0);
                Global.drvFL.setSpeed(0);
                Global.drvFR.setSpeed(0);
            }
        });

        /*es.addEvent(new SequenceEvent() {
            public boolean exitConditionMet() {
                return (Vision.targetRecognition() || this.getRunTimeMillis() > 3000);
            }
            public void execute() {}
        });*/

//        if(Constants.SHOOTER_ENABLED)
//            es.addEvent(new SequenceEvent() { //Run arm until launched
//                public boolean exitConditionMet() {return !Global.shooterTSensor.get();}
//                public void execute() {Global.mShooter.setSpeed(-1.0);}
//            });
//        
        es.addEvent(new SingleSequence() { //Do a dance
            public void execute() {
                new LightsFlashEvent(Global.blueLights, true, 1250).registerIterableEvent();
                new LightsFlashEvent(Global.redLights, false, 1250).registerIterableEvent();
            }
        });
        
        Global.mArmMove.setSpeed(-0.45);
        (new Event() {
            public void execute() {Global.mArmMove.setSpeed(0);}
        }).registerTimedEvent(300);   
        es.startSequencer();
    }

    private AutoConfig() {}
}
