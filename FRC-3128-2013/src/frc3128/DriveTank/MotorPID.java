package frc3128.DriveTank;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

/****************************************************************/
/**************PID CONTROL NOT COMPLETE YET**********************/
/****************************************************************/

public class MotorPID extends Event {
    private final Jaguar mtr;
    private final AnalogChannel enc;
    private double pc, ic, dc, lt = 0, integ = 0, tgtEnc = 0, lastEnc = 0;
    
    public MotorPID(Jaguar j, AnalogChannel enc) {
        mtr = j;
        this.enc = enc;
    }
    
    public void setPID(double p, double i, double d) {pc = p; ic = i; dc = d;}
    public void setTargetAngle(double e) {tgtEnc = e;}
    
    public void execute() {
        if(tgtEnc == 0) {stop(); return;}
        if(lt == 0) lt = System.currentTimeMillis();
        double dt = System.currentTimeMillis() - lt;
        double cPos = Global.getAngleFrom(enc);
        double ePos = (tgtEnc - cPos);
        double dPos = (lastEnc != 0 ? (dt / 1000.0) * (lastEnc - cPos) : 0);
        double pow = 0;
        
        pow += pc*ePos; pow += dc*dPos;
        pow *= -1;
        
        this.mtr.set(pow);
        if(Math.abs(tgtEnc - cPos) < 1) {
            stop();
        }
        lastEnc = Global.getAngleFrom(enc);
    }
    
    public void stop() {
        this.mtr.set(0);
        this.cancelEvent();
    }
}