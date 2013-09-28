package frc3128;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.EventSequence.TimedSequence;
import frc3128.EventManager.ListenerConst;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Motor.MotorLink;

/**
 * 
 * @author Noah Sutton-Smolin
 */
public class Global {
    public static final XControl xControl1 = new XControl(1);
    public static final MotorLink mLeft = new MotorLink(new Jaguar(1,1));
    public static final MotorLink mRight = new MotorLink(new Jaguar(1,2));
    
    public static void initializeRobot() {}
    public static void initializeDisabled() {}
    public static void initializeAuto() {
        EventSequencer autoSeq = new EventSequencer();

        autoSeq.addEvent(new TimedSequence(1000) {
            public void execute() {
                Global.mLeft.setSpeed(0.7);
                Global.mRight.setSpeed(0.7);
            }
        });

        autoSeq.addEvent(new SequenceEvent() {
            public void execute() {
                Global.mLeft.setSpeed(-1);
                Global.mRight.setSpeed(0);
            }

            public boolean exitConditionMet() {
                return !(Global.mLeft.getEncoderAngle() < 50);
            }
        });

        autoSeq.addEvent(new SingleSequence() {
            public void execute() {
                Global.mLeft.setSpeed(0);
                Global.mRight.setSpeed(0);
            }
        });

        autoSeq.startSequence();
    }
    
    public static void initializeTeleop() {
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.mLeft.setSpeed(Global.xControl1.y1+Global.xControl1.x1);
                Global.mLeft.setSpeed(Global.xControl1.y1-Global.xControl1.x1);
            }
        }, ListenerConst.UPDATE_JOY1);
    }

    public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {EventManager.dropAllEvents(); ListenerManager.dropAllListeners();}

    private Global() {}
}