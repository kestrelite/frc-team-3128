package frc3128;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.Controller.XControl;
import frc3128.DriveMecanum.DriveMecanum;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.ListenerManager.ListenerManager;
import frc3128.PneumaticsManager.PistonID;
import frc3128.PneumaticsManager.PneumaticsManager;

class DriveTank extends Event {
    public void execute() {
        double x = Global.xControl1.x1;
        double y = Global.xControl1.y1;
        
        if (Math.abs(y) < 0.15) y = 0;
        if (Math.abs(x) < 0.15) x = 0;
        
        DebugLog.log(4, referenceName, "Y: "+y+", "+x);
        Global.mLB.set(((y-(x/1.5))/1.3));
        Global.mRB.set((-(y+(x/1.5))));
        Global.mLF.set(((y-(x/1.5))/1.3));
        Global.mRF.set((-(y+(x/1.5))));
    }
}

public class Global {
    public final static String referenceName = "Global";
    public       static boolean shutdown = false;
    public final static EventManager      eventManager = new EventManager();
    public       static XControl          xControl1 = new XControl(1);
    public       static PneumaticsManager pnManager = new PneumaticsManager(1,1,1,1);
    public final static Jaguar mLB = new Jaguar(1, 1);
    public final static Jaguar mRB = new Jaguar(1, 4);
    public final static Jaguar mLF = new Jaguar(1, 2);
    public final static Jaguar mRF = new Jaguar(1, 3);
    public       static PistonID pst1 = PneumaticsManager.addPiston(1, 2, 3, 4);
    public       static PistonID pst2;
    
    public static double getMag(double x1, double y1) {
	return Math.sqrt(MathUtils.pow(x1,2) + MathUtils.pow(y1,2));
    }
	
    public static double getTh(double x1, double y1) {
        return MathUtils.atan2(y1,x1);
    }
    
    public static void initializeRobot() {
        DriveMecanum d = new DriveMecanum(true);
    }

    public static void initializeDisabled() {
        
    }
    
    public static void initializeAuto() {
    
    }

    public static void initializeTeleop() {
        
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
}
