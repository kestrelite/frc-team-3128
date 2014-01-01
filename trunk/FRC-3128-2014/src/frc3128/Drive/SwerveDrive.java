package frc3128.Drive;

import com.sun.squawk.util.MathUtils;
import frc3128.EventManager.Event;
import frc3128.HardwareLink.Motor.MotorLinkV2;
import frc3128.Util.RobotMath;

/**
 * @author Yousuf M. Soliman
 */
public class SwerveDrive extends Event {
    private final MotorLinkV2 driveLF, driveRF, driveLB, driveRB;
    private final MotorLinkV2 angleLF, angleRF, angleLB, angleRB;
    
    public SwerveDrive(MotorLinkV2 driveFL, MotorLinkV2 driveFR, MotorLinkV2 driveBL, MotorLinkV2 driveBR, 
            MotorLinkV2 angleFL, MotorLinkV2 angleFR, MotorLinkV2 angleBL, MotorLinkV2 angleBR) {
        this.driveLF = driveFL; this.driveRF = driveFR; this.driveLB = driveBL; this.driveRB = driveBR; 
        this.angleLF = angleFL; this.angleRF = angleFR; this.angleLB = angleBL; this.angleRB = angleBR;
        this.driveLF.startSpeedControl(0); this.driveRF.startSpeedControl(0);
        this.driveLB.startSpeedControl(0); this.driveRB.startSpeedControl(0);
        this.angleLF.startSpeedControl(0); this.angleRF.startSpeedControl(0);
        this.angleLB.startSpeedControl(0); this.angleRB.startSpeedControl(0);
    }
    
    public void execute() {
        double angle = 0, speed = 0, rot = 0;
        
        double dTx = 0, dTy = 0;
        double dXLF = 0, dXLB = 0, dXRF = 0, dXRB = 0, dYLF = 0, dYLB = 0, dYRF = 0, dYRB = 0;
        double dLF = 0, dLB = 0, dRF = 0, dRB = 0;
        double tLF = 0, tLB = 0, tRF = 0, tRB = 0;
        
        dTx = speed * Math.cos(RobotMath.dTR(angle));
        dTy = speed * Math.sin(RobotMath.dTR(angle));
        
        dXLF = dTx - rot;
        dYLF = dTy - rot;
        dXLB = dTx + rot;
        dYLB = dTy - rot;
        dXRF = dTx - rot;
        dYRF = dTy + rot;
        dXRB = dTx + rot;
        dYRB = dTy + rot;

        dLF = Math.sqrt(MathUtils.pow(dXLF, 2) + MathUtils.pow(dYLF, 2));
        dLB = Math.sqrt(MathUtils.pow(dXLB, 2) + MathUtils.pow(dYLB, 2));
        dRF = Math.sqrt(MathUtils.pow(dXRF, 2) + MathUtils.pow(dYRF, 2));
        dRB = Math.sqrt(MathUtils.pow(dXRB, 2) + MathUtils.pow(dYRB, 2));

        if (dYLF != 0) tLF = MathUtils.atan2(dYLF, dXLF);
        else           tLF = 0;
        if (dYLB != 0) tLB = MathUtils.atan2(dYLB, dXLB);
        else           tLB = 0;
        if (dYRF != 0) tRF = MathUtils.atan2(dYRF, dXRF);
        else           tRF = 0;
        if (dYRB != 0) tRB = MathUtils.atan2(dYRB, dXRB);
        else           tRB = 0;
        
        this.driveLB.setControlTarget(dLB);
        this.driveRB.setControlTarget(dRB);
        this.driveLF.setControlTarget(dLF);
        this.driveRF.setControlTarget(dRF);
        this.angleLB.setControlTarget(tLB);
        this.angleRB.setControlTarget(tRB);
        this.angleLF.setControlTarget(tLF);
        this.angleRF.setControlTarget(tRF);
    }
}
