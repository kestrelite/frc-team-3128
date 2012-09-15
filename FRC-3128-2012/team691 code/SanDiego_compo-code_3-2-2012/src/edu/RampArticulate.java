
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu;

/**
 *
 * @author WRS
 */
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class RampArticulate
{
    // public static final double SAFTEY_INTERVAL = 1.0;
    public static final Value FWD = Relay.Value.kForward;
    public static final Value REV = Relay.Value.kReverse;
    public static final Value OFF = Relay.Value.kOff;
    protected Relay rr             = Objects.rampSpikeR;
    protected Relay rl             = Objects.rampSpikeL;
    protected boolean reverseL = false;
    protected boolean reverseR = false;
    
    public RampArticulate( boolean reverseL, boolean reverseR )
    {
        this.reverseL = reverseL;
        this.reverseR = reverseR;
    }

    // protected double offTime                   = Time.time() + SAFTEY_INTERVAL;
    // protected Value currentDirection           = OFF;

    // Brings up the ramp articulator, has a shutoff after 5 seconds to stop burnout
    public void bringUp()
    {
        set( REV );
    }

    // Brings ramp articulator down
    public void bringDown()
    {
        set( FWD );
    }

    // turns the ramp articulator off
    public void turnOff()
    {
        set( OFF );
    }
    
    public void set (Value in)
    {
        if( reverseR )
        {
            if( in == FWD )
                rr.set( REV );
            else if( in == REV )
                rr.set( FWD );
            else
                rr.set(OFF);
                
        }
        else
            rr.set(in);
        
        if( reverseL )
        {
            if( in == FWD )
                rl.set( REV );
            else if( in == REV )
                rl.set( FWD );
            else
                rl.set(OFF);
        }
        else
            rl.set(in);
    }
}


//FIRST FRC team 691 2012 competition code
