/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu;

import edu.wpi.first.wpilibj.SpeedController;
import org.team691.drive.base.Drive;

/**
 *
 * @author SierraTango
 */
public class TankDrive implements Drive
{
    public SpeedController R = Objects.rDrive;
    public SpeedController L = Objects.lDrive;
    
    
    public void update( double Y, double X, double T )
    {
        R.set( Y - T );
        L.set( Y + T );
    }
    
    public void disable(){}
    public void enable(){}
    public void stop(){}
    public void lockDown(){}
}
