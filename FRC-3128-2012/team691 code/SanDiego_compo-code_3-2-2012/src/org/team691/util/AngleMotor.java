
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team691.util;

import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author SierraTango
 */
public class AngleMotor
{
    public static final double DEFAULT_SCALE       = 0.000035; // 0.000075
    public static final double DEFAULT_KP          = 70;       // 125
    public static final double DEFAULT_KI          = 5;        // 5
    public static final double DEFAULT_KD          = 35;       // 100
    public static final double CONSTANT_ADDER      = 0.1;      // 0.22
    public static final double I_CHECKS_PER_SECOND = 3;        // 3
    public static final double MAX_ERROR_FOR_ADDER = 0.978;    // ~cos(12) == 12degrees
    public static final double MAX_POWER           = 1;       // 0.23 //0.75
    public static final double MIN_POWER           = -1;      // -0.23 //0.75

    // public static final double SCALE     =  10; //1.5
    // public static final double SCALE_I   =  1.0;
    // public static final boolean LERP_I_TO_ZERO = false;
    public static final boolean CONSTANT_PUNCH    = false;
    public static final double SIGNIFICANT        = 17;                        // degrees
    public static final double UPDATES_PER_SECOND = 20;                        // 17
    public static final double I_UPDATE_INTERVAL  = 1.0 / I_CHECKS_PER_SECOND; // 0.33333
    public static final double UPDATE_INTERVAL    = 1.0 / UPDATES_PER_SECOND;
    protected double lastDist                     = 0.0;
    protected double lastPID                      = 0.0;
    protected PID pid = new PID( DEFAULT_SCALE, DEFAULT_KP, DEFAULT_KI, DEFAULT_KD );
    protected Angle target                        = Angle.zero();
    protected double nextUpdate                   = Time.time() + UPDATE_INTERVAL;
    protected double nextICheck                   = Time.time() + I_CHECKS_PER_SECOND;
    protected double lastTime                     = Time.time();
    protected Angle lastITarget                   = Angle.zero();
    protected Angle finalTarget                   = Angle.zero();
    protected boolean enabled                     = true;
    protected double dTime                        = UPDATE_INTERVAL;
    protected Angle current                       = Angle.zero();
    protected AngleSensor a;
    protected SpeedController mtr;

    public AngleMotor(SpeedController mtr, AngleSensor a)
    {
        enable();

        this.mtr = mtr;
        this.a   = a;
    }

    /**
     * Gives command flow to the motors so that they can
     */
    public void update()
    {
        if ( !enabled )
            return;
        if ( Time.time() < nextUpdate )
            return;

        // Smooth out the change from the current target to the new target.
        // Prevents the motors from spazzing out, and resists small changes in
        // the input.
        dTime  = Time.time() - lastTime;
        target = finalTarget; // target.lerp( finalTarget, dTime * SCALE );

        // Find where we are, figure out how far away we are, then do the PID
        // calculation.
        current  = a.get();
        lastDist = current.distanceTo( target );
        lastPID  = pid.calc( lastDist );

        /*
         * System.out.println
         *       (
         *             "\ncurrent angle: " + current.get()
         *           + "\ntarget angle : " + target.get()
         *           + "\ndistance to  : " + lastDist
         *           + "\nPID output   : " + lastPID
         *       );
         */

        // Swerve wheels spin really fast. Clamping motor power helps prevent
        // spaz outs.
        lastPID = clampMotorIn( lastPID );

        // Constant punch
        if ( CONSTANT_PUNCH && ( Mathf.abs( getCosOfError() ) < MAX_ERROR_FOR_ADDER ) )
            lastPID += ( Mathf.sign( lastPID ) * CONSTANT_ADDER );

        mtr.set( Util.victorLinearize( lastPID ) );

        // System.out.println( "motor power : " + Util.victorLinearize(lastPID) );

        // allows us to use I to overcome mechanical resistance, but not explode
        if ( Time.time() > nextICheck )
        {
            if ( significantChange( lastITarget, finalTarget ) )
                pid.resetErrorI();

            lastITarget = finalTarget;
            nextICheck  = Time.time() + I_UPDATE_INTERVAL;
        }

        // update time-related variables.
        nextUpdate = Time.time() + UPDATE_INTERVAL;
        lastTime   = Time.time();

    }

    /**
     * Clamps the maximum and minimum power that can be sent to the motors so
     * that they don't overshoot too much.
     * @param value The value to clamp. This should be the most recent value
     * returned by PID.
     * @return The value clamped between MAX_POWER and MIN_POWER.
     */
    public static double clampMotorIn(double value)
    {
        if ( value > MAX_POWER )
            return MAX_POWER;
        if ( value < MIN_POWER )
            return MIN_POWER;
        return value;
    }

    public void overwritePID(double input)
    {
        disable();
        mtr.set( Util.victorLinearize( input ) );
    }

    public void enable()
    {
        enabled = true;
    }

    public void disable()
    {
        enabled = false;

        mtr.set( 0.0 );
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isDisabled()
    {
        return !enabled;
    }

    public void reset()
    {
        // disable();
        mtr.set( 0 );

        finalTarget = Angle.zero();
        target      = Angle.zero();

        // setTargetAngle(Angle.zero());
        pid.resetErrorI();
        a.reset();
    }

    public double getCosOfError()
    {
        return Mathf.cos( current.distanceTo( target ) * Mathf.DEG2RAD );
    }

    public void setTargetAngle(Angle target)
    {
        this.finalTarget = target;
    }

    public Angle getTargetAngle()
    {
        return finalTarget;
    }

    public void setPID(PID pid)
    {
        this.pid = pid;
    }

    public PID getPID()
    {
        return pid;
    }

    public double getLastPID()
    {
        return lastPID;
    }

    public double getLastDistance()
    {
        return lastDist;
    }

    public static boolean significantChange(Angle a, Angle b)
    {
        if ( Mathf.abs( a.distanceTo( b ) ) > SIGNIFICANT )
            return true;
        return false;
    }
}


//FIRST FRC team 691 2012 competition code
