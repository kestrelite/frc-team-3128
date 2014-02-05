package frc3128.Drive;

import com.sun.squawk.util.MathUtils;
import frc3128.EventManager.Event;
import frc3128.Global;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;
import frc3128.Util.DebugLog;
import frc3128.Util.RobotMath;

public class SwerveDrive extends Event {

    private static final double SILLY_ROTATIONAL_CONSTANT = 6.779661;
    private final double xPosL = -12.77374, xPosR = 12.77374, xPosB = 0.0;
    private final double yPosL = 7.375, yPosR = 7.375, yPosB = -14.75;
    private double vel, theta, rot, xVel, yVel;
    private double spdL, spdR, spdB;
    private double angL, angR, angB;
    private double x1, y1, x2, thresh = 0.1;

    public SwerveDrive() { }

    public void execute() {
        x1 = Math.abs(Global.xControl.x1) > thresh ?  Global.xControl.x1 : 0.0;
        y1 = Math.abs(Global.xControl.y1) > thresh ? -Global.xControl.y1 : 0.0;
        x2 = Math.abs(Global.xControl.x2) > thresh ?  Global.xControl.x2 : 0.0;

        vel = Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2));
        theta = RobotMath.rTD(MathUtils.atan2(y1, x1))-90.0;
        rot = (x2 / 128.0) * SwerveDrive.SILLY_ROTATIONAL_CONSTANT;

        DebugLog.log(DebugLog.LVL_DEBUG, this, "Theta: "+theta);
        
        //Global.rotFR.setControlTarget(theta-18);
        //Global.rotFL.setControlTarget(theta-55);
        //Global.rotBk.setControlTarget(theta-10);
        
//	xVel = vel * Math.cos(theta);
//	yVel = vel * Math.sin(theta);
//        
//        spdL = Math.sqrt(MathUtils.pow(xVel+(rot*yPosL), 2) + MathUtils.pow(yVel-(rot*xPosL), 2));
//        angL = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosL), yVel-(rot*xPosL)));
//        
//        spdB = Math.sqrt(MathUtils.pow(xVel+(rot*yPosB), 2) + MathUtils.pow(yVel-(rot*xPosB), 2));
//        angB = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosB), yVel-(rot*xPosB)));
//        
//        spdR = Math.sqrt(MathUtils.pow(xVel+(rot*yPosR), 2) + MathUtils.pow(yVel-(rot*xPosR), 2));
//        angR = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosR), yVel-(rot*xPosR)));
//        
//        this.driveB.setSpeed(spdB);
//        this.driveL.setSpeed(spdL);
//        this.driveR.setSpeed(spdR);
//        this.angleB.setSpeedControlTarget(angB);
//        this.angleL.setSpeedControlTarget(angL);
//        this.angleR.setSpeedControlTarget(angR);
    }
}
