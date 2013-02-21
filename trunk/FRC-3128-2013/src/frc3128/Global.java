package frc3128;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.AutoTarget.ATiltLock;
import frc3128.AutoTarget.AutoFire;
import frc3128.AutoTarget.AutoTurn;
import frc3128.Connection.CameraCon;
import frc3128.Controller.XControl;
import frc3128.DriveTank.DriveTank;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.ListenerManager;
import frc3128.EventManager.Sync.MotorSync;
import frc3128.PneumaticsManager.PistonID;
import frc3128.PneumaticsManager.PneumaticsManager;

class DebugOutputs extends Event {
    public void execute() {
        DebugLog.log(4, referenceName, "X1:" + Global.xControl1.x1 + ", Y1:" + Global.xControl1.y1);
        DebugLog.log(4, referenceName, "Y2: " + Global.xControl1.y2);
        DebugLog.log(3, referenceName, "gTilt:" + Global.gTilt.getAngle() + ", gTurn:" + Global.gTurn.getAngle());
        DebugLog.log(3, referenceName, "mSpeed: " + Global.mShoot1.get() + " mSpeed2: " + Global.mShoot2.get());
    }
}

public class Global {
    public final static String referenceName = "Global";
    public final static EventManager eventManager = new EventManager();
    public       static XControl xControl1;
    
    public final static PneumaticsManager pnManager = new PneumaticsManager(1, 1, 1, 2);
    public final static PistonID pstFire = PneumaticsManager.addPiston(1, 1, 1, 2, true, false);
    
    public       static DriveTank driveTank;
    public final static Jaguar mLB     = new Jaguar(1, 3);
    public final static Jaguar mRB     = new Jaguar(1, 1);
    public final static Jaguar mLF     = new Jaguar(1, 2);
    public final static Jaguar mRF     = new Jaguar(1, 4);
    public final static MotorSync msTilt = new MotorSync(1, 6);
    public final static Jaguar mShoot1 = new Jaguar(1, 7);
    public final static Jaguar mShoot2 = new Jaguar(1, 8);
    
    public final static Gyro gTilt = new Gyro(1, 2);
    public final static Gyro gTurn = new Gyro(1, 1);
    public final static Relay camLight = new Relay(1, 1, Relay.Direction.kForward);

    public final static CameraCon dashConnection = new CameraCon();
    
    public static void initializeRobot() {
        Global.gTilt.reset();
        Global.gTurn.reset();
        DebugLog.setLogLevel(3);
        PneumaticsManager.setCompressorStateOff();
        DebugLog.log(3, referenceName, "ROBOT INITIALIZATION COMPLETE");
    }

    public static void initializeDisabled() {
        PneumaticsManager.setCompressorStateOn();
        Global.camLight.set(Relay.Value.kOn);
    }

    public static void initializeAuto() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
        Global.gTilt.reset(); Global.gTurn.reset();
        
        dashConnection.registerIterableEvent();
        
        EventSequencer autoAim = new EventSequencer();
        autoAim.addEvent(new AutoTurn());
        autoAim.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot1.set(-1.0);
                Global.mShoot2.set(-1.0);
            }
        });
        autoAim.addEvent(new ATiltLock());
        autoAim.addEvent(new AutoFire());
        autoAim.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot1.set(0);
                Global.mShoot2.set(0);
            }
        });        
        autoAim.startSequence();
    }

    public static void initializeTeleop() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();

        Global.xControl1 = new XControl(1);
        driveTank = new DriveTank();
        dashConnection.registerIterableEvent();
    }

    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Watchdog.getInstance().kill();
    }
    
    public static void robotStop() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Global.stopMotors();
        PneumaticsManager.lockAllPistons();
    }
    
    public static void robotPause() {
        EventManager.toggleEventProcessing();
        Global.stopMotors();
    }
    
    public static void stopMotors() {
        Global.mLF.set(0);
        Global.mRF.set(0);
        Global.mRB.set(0);
        Global.mLB.set(0);
        Global.mShoot1.set(0);
        Global.mShoot2.set(0);
        Global.msTilt.overridePower(0);
    }
}