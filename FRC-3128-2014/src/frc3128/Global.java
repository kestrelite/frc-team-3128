package frc3128;

import edu.wpi.first.wpilibj.DigitalInput;
import frc3128.RobotMovement.SwerveDrive;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerConst;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.AttackControl;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Controller.XControlMap;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.HardwareLink.RelayLink;
import frc3128.RobotMovement.AutoConfig;
import frc3128.RobotMovement.CockArm;
import frc3128.RobotMovement.LightsFlashEvent;
import frc3128.Util.DebugLog;
import frc3128.Util.LightChangeEvent;

public class Global {
    public static final GyroLink gyr = new GyroLink(new Gyro(1, 1));
    public static final DigitalInput shooterTSensor = new DigitalInput(1, 4);
    public static final DigitalInput ballTSensor = new DigitalInput(1, 1);
    
    public static final MagneticPotEncoder encFR = new MagneticPotEncoder(180-333, 1, 2); //Clockwise is positive. 
    public static final MagneticPotEncoder encFL = new MagneticPotEncoder(180-327, 1, 3);
    public static final MagneticPotEncoder encBk = new MagneticPotEncoder(180-195, 1, 4);
    
    public static MotorLink rotFR = new MotorLink(new Talon(1, 8), encFR, new LinearAngleTarget(.40, 3, 0.005, 2.0));
    public static MotorLink rotFL = new MotorLink(new Talon(1, 9), encFL, new LinearAngleTarget(.40, 3, 0.005, 2.0));
    public static MotorLink rotBk = new MotorLink(new Talon(1, 7), encBk, new LinearAngleTarget(.40, 3, 0.005, 2.0));
    public static MotorLink drvFR = new MotorLink(new Talon(1, 1), 0.856); //137.5
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2), 1.000); //113.6
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3), 0.904); //130.0
    
    public static MotorLink mShooter = new MotorLink(new Talon(1, 4));
    public static MotorLink mArmRoll = new MotorLink(new Talon(1, 6));
    public static MotorLink mArmMove = new MotorLink(new Talon(1, 5));

    public static XControl xControl;
    public static AttackControl aControl;
    public static CockArm cockShooter = new CockArm();
    
    public static RelayLink redLights = new RelayLink(new Relay(1, 1));
    public static RelayLink blueLights = new RelayLink(new Relay(1, 2));
    public static RelayLink camLights = new RelayLink(new Relay(1, 3));
    public static LightsFlashEvent redLightsFlash = new LightsFlashEvent(Global.redLights, false);
    public static LightsFlashEvent blueLightsFlash = new LightsFlashEvent(Global.blueLights, true);

    public static AxisCamera camera;// = AxisCamera.getInstance("10.31.28.11");
    public static double gyrBias = 180;
                
    public static void initializeRobot() {
        redLights.setOff();
        blueLights.setOff();

        Global.rotBk.startControl(90);
        Global.rotFL.startControl(90);
        Global.rotFR.startControl(90);

        DebugLog.setLogLevel(DebugLog.LVL_INFO);
    }

    public static void initializeDisabled() {
        redLights.setOff();
        blueLights.setOff();
    }

    public static void initializeAuto() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
        Global.camLights.setOn();
        Global.rotBk.startControl(90);
        Global.rotFL.startControl(90);
        Global.rotFR.startControl(90);
        Global.cockShooter.registerIterableEvent();
        AutoConfig.initialize();
    }
    
    public static void initializeTeleop() {
        new LightChangeEvent(redLights, blueLights).registerSingleEvent();
        Global.camLights.setOff();
        Global.cockShooter.registerIterableEvent();
        xControl = new XControl(1);
        aControl = new AttackControl(2);
        Global.rotBk.startControl(90);
        Global.rotFL.startControl(90);
        Global.rotFR.startControl(90);

        (new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_INFO, "Global", "FL: " + Global.rotFL.getEncoderAngle());
                DebugLog.log(DebugLog.LVL_INFO, "Global", "FR: " + Global.rotFR.getEncoderAngle());
                DebugLog.log(DebugLog.LVL_INFO, "Global", "BK: " + Global.rotBk.getEncoderAngle());
            }
        }).registerIterableEvent();
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.gyr.resetAngle();
                gyrBias = 0;
            }
        }, xControl.getButtonKey("Y", true));
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.gyr.resetAngle();
                gyrBias = 180;
            }
        }, xControl.getButtonKey("B", true));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockShooter.cancelEvent();
                Global.mShooter.setSpeed(1);
            }
        }, Global.xControl.getButtonKey("X", true));
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockShooter.registerIterableEvent();
                Global.mShooter.setSpeed(0);
            }
        }, Global.xControl.getButtonKey("X", false));
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockShooter.cancelEvent();
                Global.mShooter.setSpeed(- 1);
            }
        }, Global.xControl.getButtonKey("RB", true));
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.cockShooter.registerIterableEvent();
                Global.mShooter.setSpeed(0);
            }
        }, Global.xControl.getButtonKey("RB", false));
        
        ListenerManager.addListener(new Event() {
            public void execute() {Global.cockShooter.stopArmCock();}
        }, Global.xControl.getButtonKey(XControlMap.BACK, true));
        ListenerManager.addListener(new Event() {
            public void execute() {Global.cockShooter.startArmCock();}
        }, Global.xControl.getButtonKey(XControlMap.START, true));

        ListenerManager.addListener(new Event() {
            public void execute() {
                double trgrs = Global.aControl.y;
                if(Math.abs(trgrs) > 0.25)
                    Global.mArmMove.setSpeed(0.55*(trgrs > 0 ? 1 : -1));
                else 
                    Global.mArmMove.setSpeed(0);
            }
        }, ListenerConst.UPDATE_ATK_JOY);
        ListenerManager.addListener(new Event() {
            public void execute() {Global.mArmRoll.setSpeed(0.90);}
        }, Global.aControl.getListenerKey(1, true));
        ListenerManager.addListener(new Event() {
            public void execute() {Global.mArmRoll.setSpeed(0);}
        }, Global.aControl.getListenerKey(1, false));
        ListenerManager.addListener(new Event() {
            public void execute() {Global.mArmRoll.setSpeed(-0.90);}
        }, Global.aControl.getListenerKey(3, true));
        ListenerManager.addListener(new Event() {
            public void execute() {Global.mArmRoll.setSpeed(0);}
        }, Global.aControl.getListenerKey(3, false));
        ListenerManager.addListener(new Event() {
            public void execute() {redLights.setFlipped(); blueLights.setFlipped();}
        }, Global.aControl.getListenerKey(5, true));
        
        ListenerManager.addListener(new SwerveDrive(), ListenerConst.UPDATE_X_DRIVE);
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
