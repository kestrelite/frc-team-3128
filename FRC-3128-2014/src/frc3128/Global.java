package frc3128;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Watchdog;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.ListenerManager;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.Encoder.MagneticPotEncoder;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.HardwareLink.Motor.SpeedControl.LinearAngleTarget;
import frc3128.HardwareLink.Motor.SpeedControl.PIDAngleTarget;
import frc3128.HardwareLink.Piston;
import frc3128.Util.Connection.Beaglebone;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

public class Global {

    public final static XControl xControl1 = new XControl(1);
    public final static MagneticPotEncoder angleTest = new MagneticPotEncoder(1, 7);
    public final static MotorLink angleMotor = new MotorLink(new Jaguar(1, 6), angleTest, new PIDAngleTarget(.05, 5, 50, .005, .001, 1));
    public final static Compressor comp = new Compressor(1, 1, 1, 2);
    public final static Piston testP = new Piston(new Solenoid(1, 3), new Solenoid(1, 4), true, false);
 
    public static void initializeRobot() {
        DebugLog.setLogLevel(DebugLog.LVL_STREAM); 
        //comp.start();
    }

    public static void initializeDisabled() {
    }

    public static void initializeAuto() {
    }

    public static void initializeTeleop() {
        double x = angleMotor.getEncoderAngle()+90;
        angleMotor.startSpeedControl(x);
        DebugLog.log(DebugLog.LVL_DEBUG, "Global", "Target: "+x);
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