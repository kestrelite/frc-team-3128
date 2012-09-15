
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team691.util;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author SierraTango
 */
public class RPMMotor
{
    public static final double DEFAULT_ENCODER_CLICKS_TO_REVOLUTION = 0.166667; // 0.002778; // ~1/360
    public static final double DEFAULT_ENCODER_SPEED_TO_RPM = 0.1;
    public static final double DEFAULT_KP                   = 80.0;      // 100
    public static final double DEFAULT_KI                   = 0;         // 1
    public static final double DEFAULT_KD                   = 2;         // 30
    public static final double DEFAULT_SCALE                = 0.0000001; // 0.000001
    public static final double ERROR_PID = 0.025; // less than 2.5% change
    public static final double ERROR_RPM = 75; // within 100 rpm of target ~5%
    public static final double UPDATES_PER_SECOND           = 20;
    public static final double UPDATE_INTERVAL              = 1.0 / UPDATES_PER_SECOND;
    protected double encoderSpeed                           = 0;
    protected int lastCount                                 = 0;
    protected int deltaCount                                = 0;
    protected double lastPID                                = 0.0;
    protected double objectiveRPM                           = 0.0;
    public PID pid = new PID( DEFAULT_SCALE, DEFAULT_KP, DEFAULT_KI, DEFAULT_KD );
    protected double lastTime                               = Time.time();
    public double encoderToRevolution = DEFAULT_ENCODER_CLICKS_TO_REVOLUTION;
    public double encoderToRPM                              = DEFAULT_ENCODER_SPEED_TO_RPM;
    protected double deltaTime                              = UPDATE_INTERVAL;
    protected double rpm                                    = 0;
    protected boolean disabled                              = false;
    protected CounterBase enc;
    protected SpeedController mtr;

    public RPMMotor(SpeedController motor, CounterBase enconder)
    {
        mtr = motor;
        enc = enconder;
    }

    public void update()
    {
        if ( !updateSpeedVars() || isDisabled() )
            return;

        if ( objectiveRPM > rpm )
            lastPID = pid.calc( objectiveRPM, rpm );
        else
            lastPID = pid.calc( objectiveRPM, rpm * 1.1 );

        adjustMotor( lastPID );
    }

    public boolean updateSpeedVars()
    {
        if ( Time.time() - UPDATE_INTERVAL < lastTime )
            return false;

        deltaTime    = Time.time() - lastTime;
        deltaCount   = enc.get() - lastCount;
        encoderSpeed = ( encoderToRevolution * ( deltaCount ) ) / deltaTime;
        rpm          = encoderSpeed; // * 60;
        lastTime     = Time.time();
        lastCount    = enc.get();
        return true;
    }

    public void overWritePID(double value)
    {
        disable();
        mtr.set( value );
    }

    public void adjustMotor(double value)
    {
        mtr.set( mtr.get() + value );
    }

    public double getTarget()
    {
        return objectiveRPM;
    }

    public void setTarget(double value)
    {
        objectiveRPM = value;
    }

    public double getCurrent()
    {
        return rpm;
    }

    public PID getPID()
    {
        return pid;
    }

    public void setPID(PID newPID)
    {
        pid = newPID;
    }

    public double getLastPID()
    {
        return lastPID;
    }

    public void disable()
    {
        disabled = true;

        mtr.set( 0 );
    }

    public void enable()
    {
        disabled = false;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    // encoder clicks per cycle
    public boolean isCloseToTarget()
    {
        return Mathf.approximately( lastPID, 0, ERROR_PID )
               && Mathf.approximately( rpm, objectiveRPM, ERROR_RPM );
    }

    public double getLastDCount()
    {
        return deltaCount;
    }

    public double getLastDTime()
    {
        return deltaTime;
    }
}


//FIRST FRC team 691 2012 competition code
