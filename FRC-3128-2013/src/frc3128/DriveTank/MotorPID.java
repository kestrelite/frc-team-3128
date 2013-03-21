package frc3128.DriveTank;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.Global;

public class MotorPID extends Event {
    private final Jaguar mtr;
    private final AnalogChannel enc;
    private double pc, ic, dc, lt = 0, integ = 0, tgtenc = 0, lastenc = 0;
    
    public MotorPID(int jagPortA, int jagPortB, int encPort) {
        this.mtr = new Jaguar(jagPortA, jagPortB);
        this.enc = new AnalogChannel(encPort);
    }
    
    public void setPID(double p, double i, double d) {pc = p; ic = i; dc = d;}
    public void setTargetAngle(double e) {tgtenc = e;}
    
    public void execute() {
        if(tgtenc == 0) return;
        if(lt == 0) lt = System.currentTimeMillis();
        double dt = System.currentTimeMillis() - lt;
        double cpos = Global.getAngleFrom(enc);
        double epos = (tgtenc - cpos);
        double dpos = (lastenc != 0 ? (dt / 1000.0) * (lastenc - cpos) : 0);
        double pow = 0;
        
        pow += pc*epos; pow += dc*dpos; //ignoring integral position
        
        DebugLog.log(5, referenceName, "PIDPow: " + pow + ", cpos: " + cpos + ", epos: " + epos + ", dpos: " + dpos + ", tgtenc: " + tgtenc + ", lastenc: " + lastenc + ", pc: " + pc + ", dc: " + dc);        
        //this.mtr.set(pow);
        lastenc = Global.getAngleFrom(enc);
    }
    
    public void stop() {
        this.mtr.set(0);
        this.cancelEvent();
    }
}