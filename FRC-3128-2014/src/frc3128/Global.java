package frc3128;

import frc3128.RobotMovement.SwerveDrive;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.HardwareLink.RelayLink;
import frc3128.RobotMovement.AutoConfig;
import frc3128.RobotMovement.CockArm;
import frc3128.Util.DebugLog;
import frc3128.Util.LightChangeEvent;

public class Global {
    public static final MagneticPotEncoder encFR = new MagneticPotEncoder(-48, 1, 2);
    public static final MagneticPotEncoder encFL = new MagneticPotEncoder(17, 1, 3);
    public static final MagneticPotEncoder encBk = new MagneticPotEncoder(20, 1, 4);
    public static       GyroLink gyr = new GyroLink(new Gyro(1));
    
    public static MotorLink rotFR = new MotorLink(new Talon(1, 8), encFR, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -55 DEG
    public static MotorLink rotFL = new MotorLink(new Talon(1, 9), encFL, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -18 DEG
    public static MotorLink rotBk = new MotorLink(new Talon(1, 7), encBk, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -10 DEG
    public static MotorLink drvFR = new MotorLink(new Talon(1, 1));
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2));
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3));
    public static MotorLink mShooter = new MotorLink(new Talon(1, 4));
    
    public static XControl xControl = new XControl(1);
    
    public static RelayLink redLights = new RelayLink(new Relay(1, 1));
    public static RelayLink blueLights = new RelayLink(new Relay(1, 2));
    public static RelayLink camLights = new RelayLink(new Relay(1, 3));

    public static CockArm cockArm = new CockArm();
    
    public static void initializeRobot() {
        redLights.setOff();
        blueLights.setOff();
        DebugLog.setLogLevel(DebugLog.LVL_DEBUG);
        
        Global.rotBk.startControl(0);
        Global.rotFL.startControl(0);
        Global.rotFR.startControl(0);
    }

    public static void initializeDisabled() {
        redLights.setOff();
        blueLights.setOff();
    }

    public static void initializeAuto() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
        Global.cockArm.registerIterableEvent();
        AutoConfig.initialize();
    }

    public static void initializeTeleop() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
        Global.cockArm.registerIterableEvent();
        
        ListenerManager.addListener(new Event() {
            public void execute() {Global.gyr.resetAngle();}
        }, xControl.getButtonKey("X", true));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockArm.cancelEvent();
                Global.mShooter.setSpeed(-1);
            }
        }, Global.xControl.getButtonKey("A", true));

        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockArm.registerIterableEvent();
                Global.mShooter.setSpeed(0);
            }
        }, Global.xControl.getButtonKey("A", false));

        ListenerManager.addListener(new SwerveDrive(), "updateDrive");
    }

    public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {
        EventManager.dropAllEvents();
        ListenerManager.dropAllListeners();
        redLights.setOff();
        blueLights.setOff();
    }

    private Global() {}
}
