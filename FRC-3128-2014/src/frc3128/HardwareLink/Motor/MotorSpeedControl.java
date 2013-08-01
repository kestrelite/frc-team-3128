package frc3128.HardwareLink.Motor;

import frc3128.EventManager.Event;

/**
 *
 * @author Noah Sutton-Smolin
 */
public abstract class MotorSpeedControl extends Event {
	//TODO: Create a PID implementation
	private long lastRuntime = -1;
	private MotorLink controlledMotor = null;
	
	public MotorSpeedControl() {}
	protected void setControlledMotor(MotorLink m) {controlledMotor = m;}
	
	public abstract void setControlTarget(double d);
	public abstract void speedTimestep(double dt);
	
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
	public MotorLink getLinkedMotor() {return controlledMotor;}
	
	public final void execute() {
		lastRuntime = System.currentTimeMillis();
		this.speedTimestep(this.getLastRuntimeDist()/1000.0);
	}
}
