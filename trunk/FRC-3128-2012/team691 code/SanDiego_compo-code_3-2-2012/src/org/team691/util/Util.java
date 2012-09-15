
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team691.util;

/**
 *
 * @author SierraTango
 */
public class Util
{
    
    public static final double JOYSTICK_DEADZONE = 0.075; //7.5%
    public static double joystickDeadZone(final double value)
    {
        if( Math.abs(value) < JOYSTICK_DEADZONE )
            return 0;
        return value;
    }
    
    
    public static final double DEADBAND_VALUE = 0.082;
    public static final double VICTOR_FIT_C1  = -1.56847;
    public static final double VICTOR_FIT_C2  = -5.46889;
    public static final double VICTOR_FIT_E1  = 0.437239;
    public static final double VICTOR_FIT_A1  = ( -( 125.0 * VICTOR_FIT_E1
                                                    + 125.0 * VICTOR_FIT_C1
                                                    - 116.00 / 125.0 ) );
    public static final double VICTOR_FIT_E2 = 2.24214;
    public static final double VICTOR_FIT_G2 = -0.042375;
    public static final double VICTOR_FIT_A2 = ( -125.0
                                                 * ( VICTOR_FIT_C2 + VICTOR_FIT_E1
                                                     + VICTOR_FIT_G2 ) - 116.00 ) / 125.0;

    public static double victorLinearize(double desiredPower)
    {
        // deadzone
        if ( desiredPower > DEADBAND_VALUE )
            desiredPower -= DEADBAND_VALUE;
        else if ( desiredPower < -DEADBAND_VALUE )
            desiredPower += DEADBAND_VALUE;
        else
            return 0.0;

        // move -1.0-1.0 into the range of (-1+deadband)-(1-deadband)s
        desiredPower = desiredPower / ( 1.0 - DEADBAND_VALUE );

        // x^2   -->   x^7
        double desiredPower3 =  desiredPower * desiredPower * desiredPower;
        double desiredPower5 = desiredPower3 * desiredPower * desiredPower;
        double desiredPower7 = desiredPower5 * desiredPower * desiredPower;

        // Calculate 5th order
        double answerOrder5 = ( VICTOR_FIT_A1 * desiredPower5
                                + VICTOR_FIT_C1 * desiredPower3
                                + VICTOR_FIT_E1 * desiredPower );

        // calculate 7th order
        double answerOrder7 = ( VICTOR_FIT_A2 * desiredPower7
                                + VICTOR_FIT_C2 * desiredPower5
                                + VICTOR_FIT_E2 * desiredPower3
                                + VICTOR_FIT_G2 * desiredPower );

        // average 5th and 6th together
        double answer = 0.85 * 0.5 * ( answerOrder7 + answerOrder5 )
                        + 0.15 * desiredPower * ( 1 - DEADBAND_VALUE );

        if ( answer > 0.001 )
            answer += DEADBAND_VALUE;
        else if ( answer < -0.001 )
            answer -= DEADBAND_VALUE;

        return answer;
    }
}


//FIRST FRC team 691 2012 competition code
