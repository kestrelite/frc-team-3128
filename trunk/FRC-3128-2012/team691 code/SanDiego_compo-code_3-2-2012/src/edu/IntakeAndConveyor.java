
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedController;
import org.team691.util.FakeSpeedController;

public class IntakeAndConveyor
{
    public static final Value kForward = Relay.Value.kForward;
    public static final Value kReverse = Relay.Value.kReverse;
    public static final Value kOff     = Relay.Value.kOff;
    public int numBalls                = 0;
    public boolean wasCollected        = false;
    public boolean wasShot             = false;
    protected SpeedController r                  = Objects.intakeAndConvayorSpike;
    //public DigitalInput intakeLimitTwo = Objects.intakeLimit2;
    //public DigitalInput intakeLimitOne = Objects.intakeLimit1;

    // turns on too allow balls in
    public void turnOn()
    {
        r.set( 1 );
    }

    // Turns the system off
    public void turnOff()
    {
        r.set( 0 );
    }

    // Reverse roller to not allow balls in
    public void turnReverse()
    {
        r.set( -1 );
    }

    // automatically intakes balls if and only if we do not have 3 balls already
    /*
    public void autoCollect()
    {
        // if the intake limit switch is pressed and has not yet been toggled,
        // increase the count of how many balls have been collected
        // and toggle the boolean representing that the switch was just thrown
        // (the toggling is to prevent one ball over the limit switch from
        // being counted as multiple if it stays on the switch)
        if ( ( intakeLimitOne.get() == true ) )
        {
            if ( !wasCollected )
            {
                numBalls++;

                wasCollected = true;
            }
        }
        else
            wasCollected = false;

        // use the sae toggle system as above to count the number of balls being shot
        if ( ( intakeLimitTwo.get() == true ) && ( wasShot == false ) )
        {
            if ( !wasShot )
            {
                numBalls--;

                wasShot = true;
            }
        }
        else
            wasShot = false;

        // if we have less than three balls, turn intake on, else turn
        // it backwards to repel oncoming balls
        if ( numBalls < 3 )
            r.set( kForward );
        else
            r.set( kReverse );
    }
    *
    */
}


//FIRST FRC team 691 2012 competition code
