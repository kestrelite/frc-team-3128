package frc3128;

import com.sun.squawk.util.MathUtils;
import frc3128.EventManager.Event;
import frc3128.EventManager.ListenerManager;
import frc3128.Global;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

public class SwerveDrive extends Event {
    private final double dimConst = 8;
    private final double xPosL = -12.77374 / dimConst, xPosR = 12.77374 / dimConst, xPosB = 0.0 / dimConst;
    private final double yPosL = 7.375 / dimConst, yPosR = 7.375 / dimConst, yPosB = -14.75 / dimConst;
    private double vel, theta, rot, xVel, yVel;
    private double spdL, spdR, spdB;
    private double angL, angR, angB;

    public static double[] optimizeSwerve(double ang1, double ang2, double vel) {
        double a = RobotMath.angleDistance(ang2, ang1);
        double o[] = new double[2];
        DebugLog.log(DebugLog.LVL_DEBUG, "OptimizeSwerve", "DIST: " + a);
        if (Math.abs(a) > 90) {
            o[0] = ang2 + 180;
            o[1] = -vel;
            DebugLog.log(DebugLog.LVL_DEBUG, "OptimizeSwerve", "DIST > 90");
        } else {
            o[0] = ang2;
            o[1] = vel;
        }
        return o;
    }

    public void execute() {
        double thresh = 0.2;

        double x1 = Math.abs(Global.xControl.x1) > thresh ? Global.xControl.x1 : 0.0;
        double y1 = Math.abs(Global.xControl.y1) > thresh ? -Global.xControl.y1 : 0.0;
        double x2 = Math.abs(Global.xControl.x2) > thresh ? Global.xControl.x2 : 0.0;

        vel = -(Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2)));
        rot = x2;

        if (Math.abs(vel) > 0.1) {theta = RobotMath.rTD(MathUtils.atan2(y1, x1));} 
            else {vel = 0;}

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
    
    public SwerveDrive() {
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(-1);
            }
        }, Global.xControl.getButtonKey("A", true));

        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(1);
            }
        }, Global.xControl.getButtonKey("B", true));

        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(0);
            }
        }, Global.xControl.getButtonKey("A", false));
        
        ListenerManager.addListener(new Event() {
            public void execute() {
                Global.shooter.setSpeed(0);
            }
        }, Global.xControl.getButtonKey("B", false));
        
        ListenerManager.addListener(new SwerveDrive(), "updateDrive");        
    }
}
