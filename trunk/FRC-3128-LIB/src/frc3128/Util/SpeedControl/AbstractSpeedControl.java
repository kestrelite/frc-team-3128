package frc3128.Util.SpeedControl;

import frc3128.EventManager.Event;

public abstract class AbstractSpeedControl extends Event {
	//TODO: Create the abstract methods & a PID implementation
	private long lastRuntime = -1;
	public SpeedControlled spdControlTarget = null;
	
	public AbstractSpeedControl(SpeedControlled spdControlTarget) {this.spdControlTarget = spdControlTarget;}
	
	public abstract void setTarget(double d);
	public abstract void speedTimestep();
	
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
	 * @return the object which is the target of speed control
	 */
	public final SpeedControlled getSpeedControlledTarget() {return spdControlTarget;}
	
	public final void execute() {
		lastRuntime = System.currentTimeMillis();
		this.speedTimestep();
	}
}
