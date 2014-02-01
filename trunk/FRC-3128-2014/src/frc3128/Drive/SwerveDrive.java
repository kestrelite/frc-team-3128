package frc3128.Drive;

import com.sun.squawk.util.MathUtils;
import frc3128.EventManager.Event;
import frc3128.HardwareLink.Controller.XControl;
import frc3128.HardwareLink.GyroLink;
import frc3128.HardwareLink.Motor.MotorLink;

public class SwerveDrive extends Event {
    private static final double SILLY_ROTATIONAL_CONSTANT = 6.779661;
    
    private final MotorLink driveL, driveR, driveB;
    private final MotorLink angleL, angleR, angleB;
    private final XControl joy1;
    private final GyroLink gyro;
    private final double xPosL = -12.77374, xPosR = 12.77374, xPosB = 0.0;
    private final double yPosL = 7.375, yPosR = 7.375, yPosB = -14.75;
    
    private double vel, theta, rot, xVel, yVel;
    private double spdL, spdR, spdB;
    private double angL, angR, angB;
    
    private double x1, y1, x2, thresh = 0.1;
    
    
    public SwerveDrive(XControl joy, GyroLink gyro, 
            MotorLink driveL, MotorLink driveR, MotorLink driveB, 
            MotorLink angleL, MotorLink angleR, MotorLink angleB) {
        this.driveL = driveL; this.driveR = driveR; this.driveB = driveB;
        this.angleL = angleL; this.angleR = angleR; this.angleB = angleB; 
        this.driveL.startSpeedControl(0); 
        this.driveR.startSpeedControl(0);
        this.driveB.startSpeedControl(0); 
        this.angleL.startSpeedControl(0); 
        this.angleR.startSpeedControl(0);
        this.angleB.startSpeedControl(0); 
        this.joy1 = joy;
        this.gyro = gyro;
    }
    
    public void execute() {
        x1 = joy1.x1>thresh?joy1.x1:0.0;
        y1 = joy1.y1>thresh?joy1.y1:0.0;
        x2 = joy1.x2>thresh?joy1.x2:0.0;
        
        vel = Math.sqrt(MathUtils.pow(x1, 2) + MathUtils.pow(y1, 2));
        theta = MathUtils.atan(y1/x1)+(Math.PI/180.0)*gyro.getAngle();
        rot = (x2/128.0)*SwerveDrive.SILLY_ROTATIONAL_CONSTANT;
        
	xVel = vel * Math.cos(theta);
	yVel = vel * Math.sin(theta);
        
        spdL = Math.sqrt(MathUtils.pow(xVel+(rot*yPosL), 2) + MathUtils.pow(yVel-(rot*xPosL), 2));
        angL = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosL), yVel-(rot*xPosL)));
        
        spdB = Math.sqrt(MathUtils.pow(xVel+(rot*yPosB), 2) + MathUtils.pow(yVel-(rot*xPosB), 2));
        angB = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosB), yVel-(rot*xPosB)));
        
        spdR = Math.sqrt(MathUtils.pow(xVel+(rot*yPosR), 2) + MathUtils.pow(yVel-(rot*xPosR), 2));
        angR = 180 / Math.PI * (MathUtils.atan2(xVel+(rot*yPosR), yVel-(rot*xPosR)));
        
        this.driveB.setSpeed(spdB);
        this.driveL.setSpeed(spdL);
        this.driveR.setSpeed(spdR);
        this.angleB.setSpeedControlTarget(angB);
        this.angleL.setSpeedControlTarget(angL);
        this.angleR.setSpeedControlTarget(angR);
    }
}
