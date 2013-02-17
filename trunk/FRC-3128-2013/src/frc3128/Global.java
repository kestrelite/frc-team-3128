package frc3128;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.Controller.XControl;
import frc3128.DriveTank.DriveTank;
import frc3128.DriveTank.TurnToAngle;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.ListenerManager.ListenerManager;
import frc3128.PneumaticsManager.PistonID;
import frc3128.PneumaticsManager.PneumaticsManager;


class DebugOutputs extends Event {
    public void execute() {
        DebugLog.log(4, referenceName, "X1:" + Global.xControl1.x1 + ", Y1:" + Global.xControl1.y1);
        DebugLog.log(4, referenceName, "Y2: " + Global.xControl1.y2);
        DebugLog.log(3, referenceName, "gTilt:"+Global.gTilt.getAngle() + ", gTurn:"+Global.gTurn.getAngle());
    }
}


class ResetGyro extends Event {
    public void execute() {
        Global.gTilt.reset();
        Global.gTurn.reset();
    }
}

public class Global {
    public final static String referenceName = "Global";
    public       static boolean shutdown = false;

    public final static EventManager eventManager = new EventManager();
    public final static XControl xControl1 = new XControl(1);
    public       static DriveTank driveTank;
    public final static PneumaticsManager pnManager = new PneumaticsManager(1, 1, 1, 2);
    
    public final static Jaguar mLB = new Jaguar(1, 3);
    public final static Jaguar mRB = new Jaguar(1, 1);
    public final static Jaguar mLF = new Jaguar(1, 2);
    public final static Jaguar mRF = new Jaguar(1, 4);
    public final static Jaguar mTilt = new Jaguar(1, 6);
    public final static Jaguar frShoot = new Jaguar(1, 7);

    public       static Relay camLight = new Relay(1, 1, Relay.Direction.kForward);
    public final static Gyro gTilt = new Gyro(1, 2), gTurn = new Gyro(1,1);

    //public      static PistonID pst1 = PneumaticsManager.addPiston(1, 2, 3, 4);
    public       static PistonID pst2;

    public static double getMag(double x1, double y1) {
        return Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2));
    }

    public static double getTh(double x1, double y1) {
        return MathUtils.atan2(y1, x1);
    }

    public static void initializeRobot() {
        DebugLog.setLogLevel(3);
        PneumaticsManager.setCompressorStateOff();
        (new DebugOutputs()).registerIterableEvent();
        Global.gTilt.reset(); Global.gTurn.reset();
        ListenerManager.addListener((new ResetGyro()), "buttonADown");
        //(new TurnToAngle(30.0)).registerIterableEvent();
    }

    public static void initializeDisabled() {
        PneumaticsManager.setCompressorStateOff();
    }

    public static void initializeAuto() {}

    public static void initializeTeleop() {
        //camLight.set(Relay.Value.kOn);
        driveTank = new DriveTank();
    }

    public static void robotReset() {
        EventManager.dropEvents();
        ListenerManager.dropListeners();
    }

    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropListeners();
        Watchdog.getInstance().kill();
    }

    static void disabledTeleop() {
        PneumaticsManager.setCompressorStateOff();
    }
}
