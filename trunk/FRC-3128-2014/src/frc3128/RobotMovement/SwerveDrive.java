package frc3128.RobotMovement;

import com.sun.squawk.util.MathUtils;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.Util.RobotMath;

public class SwerveDrive extends Event {
    private double vel, theta, rot, xVel, yVel;
    private double spdL, spdR, spdB;
    private double angL, angR, angB;
    private double maxVel = 8, maxRot = RobotMath.dTR(345), c = 0.3;
    private final double xPosL = -12.77374, xPosR = 12.77374, xPosB = 0.0;
    private final double yPosL = 7.375, yPosR = 7.375, yPosB = -14.75;

    public static double[] optimizeSwerve(double ang1, double ang2, double vel) {
        double a = RobotMath.angleDistance(ang2, ang1);
        double o[] = new double[2];
        if (Math.abs(a) > 90) {
            o[0] = ang2 + 180.0;
            o[1] = -vel;
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
        rot = maxRot * x2 * 0.3;

        if (Math.abs(vel) > 0.1)
            theta = RobotMath.rTD(MathUtils.atan2(y1, x1)) + Global.gyr.getAngle() + Global.gyrBias;
        else 
            vel = 0;

        xVel = maxVel * vel * Math.cos(RobotMath.dTR(theta));
        yVel = maxVel * vel * Math.sin(RobotMath.dTR(theta));

        spdL = Math.sqrt(MathUtils.pow(xVel + (c*rot * yPosL), 2) + MathUtils.pow(yVel - (c*rot * xPosL), 2));
        angL = RobotMath.rTD(MathUtils.atan2(yVel + (c*rot * xPosL), xVel - (c*rot * yPosL)));

        spdB = Math.sqrt(MathUtils.pow(xVel + (c*rot * yPosB), 2) + MathUtils.pow(yVel - (c*rot * xPosB), 2));
        angB = 180.0 / Math.PI * (MathUtils.atan2(yVel + (c*rot * xPosB), xVel - (c*rot * yPosB)));

        spdR = Math.sqrt(MathUtils.pow(xVel + (c*rot * yPosR), 2) + MathUtils.pow(yVel - (c*rot * xPosR), 2));
        angR = 180.0 / Math.PI * (MathUtils.atan2(yVel + (c*rot * xPosR), xVel - (c*rot * yPosR)));

        double[] r = optimizeSwerve(Global.rotFR.getEncoderAngle(), angR, spdR);
        double[] l = optimizeSwerve(Global.rotFL.getEncoderAngle(), angL, spdL);
        double[] b = optimizeSwerve(Global.rotBk.getEncoderAngle(), angB, spdB);
        
        Global.rotFR.setControlTarget(r[0]+(x1 == 0 && x2 != 0 ? 0.1 : 0));
        Global.rotFL.setControlTarget(l[0]+(x1 == 0 && x2 != 0 ? 0.1 : 0));
        Global.rotBk.setControlTarget(b[0]+(x1 == 0 && x2 != 0 ? 0.1 : 0));
        
        r[1] /= maxVel;
        l[1] /= maxVel;
        b[1] /= maxVel;
        
        if (Math.abs(r[1]) > 1 || Math.abs(l[1]) > 1 || Math.abs(b[1]) > 1) {
            double scl = Math.max(Math.abs(r[1]), Math.max(Math.abs(l[1]), Math.abs(b[1])));
            r[1] /= scl;
            l[1] /= scl;
            b[1] /= scl;
        }
        
        Global.drvFR.setSpeed(-r[1]/1.0);
        Global.drvFL.setSpeed(-l[1]/1.0);
        Global.drvBk.setSpeed(-b[1]/1.0);
    }

    public SwerveDrive() {}   
}
