package frc3128;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.HardwareLink.RelayLink;
import frc3128.Util.DebugLog;
import frc3128.Util.LightChangeEvent;

public class Global {
    public static final MagneticPotEncoder encBk = new MagneticPotEncoder(20, 1, 1);
    public static final MagneticPotEncoder encFR = new MagneticPotEncoder(-48, 1, 2);
    public static final MagneticPotEncoder encFL = new MagneticPotEncoder(17, 1, 3);
    public static final GyroLink gyr = new GyroLink(new Gyro(1, 4));
    
    public static MotorLink rotFR = new MotorLink(new Talon(1, 8), encFR, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -55 DEG
    public static MotorLink rotFL = new MotorLink(new Talon(1, 9), encFL, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -18 DEG
    public static MotorLink rotBk = new MotorLink(new Talon(1, 7), encBk, new LinearAngleTarget(.1, 5, 0.005)); //OFFSET: -10 DEG
    public static MotorLink drvFR = new MotorLink(new Talon(1, 1));
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2));
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3));
    
    public static MotorLink shooter = new MotorLink(new Talon(1, 4));
    public static XControl xControl = new XControl(1);
    
    public static RelayLink redLights = new RelayLink(new Relay(1, 1));
    public static RelayLink blueLights = new RelayLink(new Relay(1, 2));
    public static RelayLink camLights = new RelayLink(new Relay(1, 3));

    public static void initializeRobot() {
        redLights.setOff(); blueLights.setOff();
        DebugLog.setLogLevel(DebugLog.LVL_DEBUG);
    }

    public static void initializeDisabled() {
        redLights.setOff(); blueLights.setOff();
    }

    public static void initializeAuto() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
    }

    public static void initializeTeleop() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
        Global.rotBk.startControl(0);
        Global.rotFL.startControl(0);
        Global.rotFR.startControl(0);
    }

    public static void robotKill() {Watchdog.getInstance().kill();}
    public static void robotStop() {
        EventManager.dropAllEvents();
        ListenerManager.dropAllListeners();
        redLights.setOff(); blueLights.setOff();
    }

    private Global() {}
}
