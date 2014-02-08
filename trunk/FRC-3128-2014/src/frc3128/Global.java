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
    //TODO: Check drive motor polarities
    public static MotorLink drvFR = new MotorLink(new Talon(1, 1));
    public static MotorLink drvFL = new MotorLink(new Talon(1, 2));
    public static MotorLink drvBk = new MotorLink(new Talon(1, 3));
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

    public static double[] optimizeSwerve(double ang1, double ang2, double vel) {
        double a = RobotMath.angleDistance(ang2, ang1);
        double o[] = new double[2];
        DebugLog.log(DebugLog.LVL_DEBUG, "OptimizeSwerve", "DIST: " + a);
        if (Math.abs(a) > 90) {
            o[0] = ang2 + 180;
            o[1] = -vel;
            DebugLog.log(DebugLog.LVL_DEBUG, "OPtimizeSwerve", "DIST > 90");
        } else {
            o[0] = ang2;
            o[1] = vel;
        }
        return o;
    }

    public static void initializeTeleop() {
        (new Event() {
            public void execute() {
                DebugLog.log(DebugLog.LVL_DEBUG, "Gyro", "Gyro Angle: " + gyr.getAngle());
            }
        }).registerIterableEvent();

        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.gyr.resetAngle();
            }
        }, xControl.getButtonKey("X", true));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.rotFR.setControlTarget(90);
                Global.rotFL.setControlTarget(90);
                Global.rotBk.setControlTarget(90);
            }
        }, xControl.getButtonKey("A", true));

        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.rotFR.setControlTarget(0);
                Global.rotFL.setControlTarget(0);
                Global.rotBk.setControlTarget(0);
            }
        }, xControl.getButtonKey("B", true));

        ListenerManager.addListener(new Event() {
            double c = 7;
            private final double xPosL = -12.77374 / c, xPosR = 12.77374 / c, xPosB = 0.0 / c;
            private final double yPosL = 7.375 / c, yPosR = 7.375 / c, yPosB = -14.75 / c;
            private double vel, theta, rot, xVel, yVel;
            private double spdL, spdR, spdB;
            private double angL, angR, angB;
            
            public void execute() {
                double thresh = 0.2;
                
                double x1 = Math.abs(Global.xControl.x1) > thresh ? Global.xControl.x1 : 0.0;
                double y1 = Math.abs(Global.xControl.y1) > thresh ? -Global.xControl.y1 : 0.0;
                double x2 = Math.abs(Global.xControl.x2) > thresh ? Global.xControl.x2 : 0.0;
                
                vel = -(Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2)));

                rot = x2;

                if (Math.abs(vel) > 0.1) {
                    theta = RobotMath.rTD(MathUtils.atan2(y1, x1)) - Global.gyr.getAngle();
                } else {
                    vel = 0;
                }

                xVel = vel * Math.cos(RobotMath.dTR(theta));
                yVel = vel * Math.sin(RobotMath.dTR(theta));

                spdL = Math.sqrt(MathUtils.pow(xVel + (rot * yPosL), 2) + MathUtils.pow(yVel - (rot * xPosL), 2));
                angL = RobotMath.rTD(MathUtils.atan2(yVel + (rot * xPosL), xVel - (rot * yPosL)));

                spdB = Math.sqrt(MathUtils.pow(xVel + (rot * yPosB), 2) + MathUtils.pow(yVel - (rot * xPosB), 2));
                angB = 180 / Math.PI * (MathUtils.atan2(yVel + (rot * xPosB), xVel - (rot * yPosB)));

                spdR = Math.sqrt(MathUtils.pow(xVel + (rot * yPosR), 2) + MathUtils.pow(yVel - (rot * xPosR), 2));
                angR = 180 / Math.PI * (MathUtils.atan2(yVel + (rot * xPosR), xVel - (rot * yPosR)));

                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "Theta: " + theta);
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "r: " + angR);
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "l: " + angL);
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "b: " + angB);
                DebugLog.log(DebugLog.LVL_DEBUG, "Global", "rot: " + rot);

                double[] r = optimizeSwerve(Global.rotFR.getEncoderAngle(), angR, spdR);
                double[] l = optimizeSwerve(Global.rotFL.getEncoderAngle(), angL, spdL);
                double[] b = optimizeSwerve(Global.rotBk.getEncoderAngle(), angB, spdB);

                Global.rotFR.setControlTarget(r[0]);
                Global.rotFL.setControlTarget(l[0]);
                Global.rotBk.setControlTarget(b[0]);

                if (Math.abs(r[1]) > 1 || Math.abs(l[1]) > 1 || Math.abs(b[1]) > 1) {
                    double scl = Math.max(Math.abs(r[1]), Math.max(Math.abs(l[1]), Math.abs(b[1])));
                    r[1] /= scl;
                    l[1] /= scl;
                    b[1] /= scl;
                }

                Global.drvFR.setSpeed(r[1] / 2);
                Global.drvFL.setSpeed(l[1] / 2);
                Global.drvBk.setSpeed(b[1] / 2);
            }
        }, ListenerConst.UPDATE_DRIVE);
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
