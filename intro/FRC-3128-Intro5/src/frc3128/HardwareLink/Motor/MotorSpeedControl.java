package frc3128.HardwareLink.Motor;

import frc3128.EventManager.Event;

/**
 *
 * @author Noah Sutton-Smolin
 */
public abstract class MotorSpeedControl extends Event {
    private long lastRuntime = -1;
    private MotorLink controlledMotor = null;
    
    public MotorSpeedControl() {}
    protected void setControlledMotor(MotorLink m) {controlledMotor = m;}
    
    public abstract void    setControlTarget(double val);
    public abstract double  speedTimestep(double dt);
    public abstract void    clearControlRun();
    public abstract boolean isComplete();
    
    /**
     * 
     * @return the last runtime in system clock milliseconds
     */
    public final long getLastRuntime() {return lastRuntime;}
    
    /**
     * 
     * @return how long ago the event was last called (used for dT)
     */
    public final long getLastRuntimeDist() {return System.currentTimeMillis() - lastRuntime;}

    /**
     * 
     * @return the encoder value of the controlled motor
     */
    public double getLinkedEncoderAngle() {return this.controlledMotor.getEncoderAngle();}

    /**
     * 
     * @return the linked motor's speed
     */
    public double getLinkedMotorSpeed() {return this.controlledMotor.getSpeed();}
    
    public final void execute() {
        lastRuntime = System.currentTimeMillis();
        this.controlledMotor.setSpeedControlled(this.speedTimestep(this.getLastRuntimeDist()/1000.0));
        if(this.isComplete()) this.controlledMotor.deleteSpeedControl();
    }
}
