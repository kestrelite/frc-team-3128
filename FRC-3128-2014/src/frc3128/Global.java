package frc3128;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.Drive.SwerveDrive;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerConst;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Controller.XControlMap;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;
import frc3128.Util.RobotMath;

public class Global {

    public static final MagneticPotEncoder encFL = new MagneticPotEncoder(-160, 1, 2);
    public static final MagneticPotEncoder encFR = new MagneticPotEncoder(-50, 1, 3);
    public static final MagneticPotEncoder encBk = new MagneticPotEncoder(30, 1, 4);
    public static final GyroLink gyr = new GyroLink(new Gyro(1));
    
    public static MotorLink rotFR = new MotorLink(new Talon(1, 8), encFR, new LinearAngleTarget(.2, 4, 0.005)); //OFFSET: -55 DEG
    public static MotorLink rotFL = new MotorLink(new Talon(1, 9), encFL, new LinearAngleTarget(.2, 4, 0.005)); //OFFSET: -18 DEG
    public static MotorLink rotBk = new MotorLink(new Talon(1, 7), encBk, new LinearAngleTarget(.2, 4, 0.005)); //OFFSET: -10 DEG

    public static MotorLink drvFR = new MotorLink(new Talon(1, 1));
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2));
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3));
    
    public static MotorLink shooter = new MotorLink(new Talon(1, 4));
    
    public static XControl xControl = new XControl(1);

    public static void initializeRobot() {
        DebugLog.setLogLevel(DebugLog.LVL_DEBUG);
        Global.rotFR.startControl(0);
        Global.rotFL.startControl(0);
        Global.rotBk.startControl(0);
    }

    public static void initializeDisabled() {
    }

    public static void initializeAuto() {
    }

    public static void initializeTeleop() {
        ListenerManager.addListener(new Event() {
            public void execute() { Global.gyr.resetAngle(); }
        }, xControl.getButtonKey("X", true));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(.5);
            }
        }, xControl.getButtonKey("A", true));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(0);
            }
        }, xControl.getButtonKey("A", false));
    }

    public static void robotKill() {
        Watchdog.getInstance().kill();
    }

    public static void robotStop() {
        EventManager.dropAllEvents();
        ListenerManager.dropAllListeners();
    }

    private Global() {
    }
}
