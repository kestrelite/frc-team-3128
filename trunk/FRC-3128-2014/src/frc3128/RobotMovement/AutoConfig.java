package frc3128.RobotMovement;

import frc3128.EventManager.Event;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.EventSequence.DelaySequence;
import frc3128.Global;
import frc3128.Util.DebugLog;

public class AutoConfig {
    public static void initialize() {
        EventSequencer es = new EventSequencer();
        
        es.addEvent(new InitSwerveDrive()); //Initialize Swerve Drive Wheel Orientation
        es.addEvent(new SingleSequence() { //Stop Turning Motors
            public void execute() {
                Global.rotBk.setSpeed(0);
                Global.rotFL.setSpeed(0);
                Global.rotFR.setSpeed(0);
            }
        });
        es.addEvent(new SequenceEvent() { //Drive forward to shooting range
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 2500;}
            public void execute() {
                Global.rotBk.setControlTarget(90);
                Global.rotFL.setControlTarget(90);
                Global.rotFR.setControlTarget(90);
                
                Global.drvBk.setSpeed(-0.50);
                Global.drvFL.setSpeed(0.50);
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
        es.addEvent(new SequenceEvent() { //Wait if the goal is not hot
            public boolean exitConditionMet() {return this.getRunTimeMillis() > 5000 || AutoConfig.isHot();}
            public void execute() {}
        });

        es.addEvent(new SequenceEvent() { //Run arm until launched
            public boolean exitConditionMet() {return !Global.shooterTSensor.get();}
            public void execute() {if(Global.ballTSensor0.get() || Global.ballTSensor1.get()) {Global.mShooter.setSpeed(-1.0);}}
        });
        
        es.addEvent(new DelaySequence(1000));
        
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
    
    public static boolean isHot() {
        DebugLog.log(DebugLog.LVL_SEVERE, "AutoConfig", "isHot event missing!");
        return true;
    }
    
    private AutoConfig() {}
}
