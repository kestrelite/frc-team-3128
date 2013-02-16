package frc3128;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.Connection.Connection;
import frc3128.Controller.XControl;
import frc3128.DriveTank.DriveTank;
import frc3128.EventManager.EventManager;
import frc3128.ListenerManager.ListenerManager;
import frc3128.PneumaticsManager.PistonID;
import frc3128.PneumaticsManager.PneumaticsManager;

public class Global {
    public final static String referenceName = "Global";
    public       static boolean shutdown = false;
    public final static EventManager eventManager = new EventManager();
    public static XControl xControl1 = new XControl(1);
    public static DriveTank driveTank;
    //public static PneumaticsManager pnManager = new PneumaticsManager(1, 1, 1, 1);
    public final static Jaguar mLB = new Jaguar(1, 3);
    public final static Jaguar mRB = new Jaguar(1, 1);
    public final static Jaguar mLF = new Jaguar(1, 2);
    public final static Jaguar mRF = new Jaguar(1, 4);
    public final static Jaguar mTilt = new Jaguar(1, 6);
    public final static Relay camLight = new Relay(1, Relay.Direction.kForward);
    //public final static Gyro gTilt;
    //public static PistonID pst1 = PneumaticsManager.addPiston(1, 2, 3, 4);
    public static PistonID pst2;

    public static double getMag(double x1, double y1) {
        return Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2));
    }

    public static double getTh(double x1, double y1) {
        return MathUtils.atan2(y1, x1);
    }

    public static void initializeRobot() {
        //camLight.set(Relay.Value.kOff);
        DebugLog.setLogLevel(4);
    }

    public static void initializeDisabled() {}

    public static void initializeAuto() {}

    public static void initializeTeleop() {
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
    
    public static double getMag(double x1, double y1) {
        return Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2));
    }

    public static double getTh(double x1, double y1) {
        return MathUtils.atan2(y1, x1);
    }
}
