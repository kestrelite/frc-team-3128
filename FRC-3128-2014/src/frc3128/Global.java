package frc3128;

import com.sun.squawk.util.MathUtils;
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
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

public class Global {

    public static final MagneticPotEncoder encFL = new MagneticPotEncoder(-125, 1, 2);
    public static final MagneticPotEncoder encFR = new MagneticPotEncoder(18, 1, 3);
    public static final MagneticPotEncoder encBk = new MagneticPotEncoder(10, 1, 4);
    public static MotorLink rotFR = new MotorLink(new Talon(1, 8), encFR, new LinearAngleTarget(.5, 5, 0.005)); //OFFSET: -55 DEG
    public static MotorLink rotFL = new MotorLink(new Talon(1, 9), encFL, new LinearAngleTarget(.5, 5, 0.005)); //OFFSET: -18 DEG
    public static MotorLink rotBk = new MotorLink(new Talon(1, 7), encBk, new LinearAngleTarget(.5, 5, 0.005)); //OFFSET: -10 DEG
    //TODO: Check drive motor polarities
    public static MotorLink drvFR = new MotorLink(new Talon(1, 1));
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2));
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3));
    public static XControl xControl = new XControl(1);

    public static void initializeRobot() {
        DebugLog.setLogLevel(DebugLog.LVL_DEBUG);
    }

    public static void initializeDisabled() {
    }

    public static void initializeAuto() {
    }

    public static void initializeTeleop() {
        Global.rotFR.startControl(0);
        Global.rotFL.startControl(0);
        Global.rotBk.startControl(0);

        ListenerManager.addListener(new Event() {
            public void execute() {
                double thresh = 0.1;
                double x1 = Math.abs(Global.xControl.x1) > thresh ? Global.xControl.x1 : 0.0;
                double y1 = Math.abs(Global.xControl.y1) > thresh ? -Global.xControl.y1 : 0.0;
                double x2 = Math.abs(Global.xControl.x2) > thresh ? Global.xControl.x2 : 0.0;
                double theta = RobotMath.rTD(MathUtils.atan2(y1, x1)) - 90.0;
                double vel = -(Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2)));

                if (Math.abs(vel) < 0.1) {
                    DebugLog.log(DebugLog.LVL_DEBUG, this, "VEL<");
                    theta = 0;
                }
                Global.rotFR.setControlTarget(theta);
                Global.rotFL.setControlTarget(theta);
                Global.rotBk.setControlTarget(theta);
            }
        }, ListenerConst.UPDATE_JOY1);
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
