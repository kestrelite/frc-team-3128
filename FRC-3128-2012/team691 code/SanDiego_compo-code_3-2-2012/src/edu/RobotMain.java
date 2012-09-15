/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/



package edu;

import edu.io.AutoTarget;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SimpleRobot;
import org.team691.drive.base.Drive;
import org.team691.util.EnhancedJoystick;
import org.team691.util.Mathf;
import org.team691.util.Time;
import org.team691.util.Util;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot
{
    public static boolean DEBUG_MODE = true;
    public static final int THROTTLE = EnhancedJoystick.THROTTLE;
    
    //manual preset RPM values
    public static final int KEY_RPM         = 1700;
    public static final int MID_RPM         = 1900;
    public static final int BUMP_RPM        = 2100;
    
    public static final int MIN_RPM        = 750;
    public static final int MAX_RPM        = 2600;
    //end manual preset RPM values
    
    public static final double DEFAULT_RPM_OFFSET    = 75;
    public static final double DEBUG_UPDATE_INTERVAL = 2.0;
    public double printTime                    = Time.time() + DEBUG_UPDATE_INTERVAL;
    public boolean isManual                    = false;
    public boolean lastManualButtonHeld        = false;
    public boolean lockSwerve                  = false;
    public boolean acceptTargetingData         = true;
    public boolean lastAutoAimButtonHeld       = false;
    private boolean lastOffsetHeld             = false;
    public Turret turret                       = Objects.turret;
    public Drive swerveDrive                   = Objects.swerveDrive;
    public EnhancedJoystick shooterJoy         = Objects.shooterJoy;
    public Joystick rightJoy                   = Objects.rightJoy;
    public RampArticulate rampArticulate       = Objects.armSystem;
    public Joystick leftJoy                    = Objects.leftJoy;
    public IntakeAndConveyor intakeAndConvayor                       = Objects.intakeSystem;
    public AutoTarget autoTarget               = Objects.autoTarget;
    public double Y                            = 0,
                  X                            = 0,
                  T                            = 0,
                  turtRot                      = 0,
                  turtPow                      = 0,
                  rpmOffset                    = 0;
    

    //allows cordination with alliance during automode -- wait until basket is clear
    public static final double AUTO_MODE_SHOOT_DELAY = 1.0;
    /**
     * This function is called once each time the robot enters autonomous mode.
     * 
     * As of 3-2-2012, autonomous simply sits still, aims the turret at the top
     * target, and then shoots when ready. It waits until AUTO_MODE_SHOOT_DELAY
     * seconds have passed before attempting to shoot, allowing us to wait for
     * our alliance partners to unload their baskets first.
     */
    public void autonomous()
    {
        zeroAll();
       // swerveDrive.enable();
        double autoShootTime = Time.time() + AUTO_MODE_SHOOT_DELAY;
        
        while( isEnabled() && isAutonomous() )
        {
            rpmOffset = DEFAULT_RPM_OFFSET;
            if( !Time.newCycle() )
                continue;
            
            acceptTargetingData = true;
            handleTurretAutoTargeting();
            turret.update();
            
            turret.spinUpShooter();
            if( Time.time() > autoShootTime )
                turret.shootWithChecks();
            
            if( DEBUG_MODE )
                debugPrint();
        }//end autonomous loop
        turret.spinDownShooter();
        turret.stopShooting();
        rpmOffset = DEFAULT_RPM_OFFSET;
    }//end autonomous

    /**
     * This function is called once each time the robot is disabled.
     * 
     * The disabled method, it does nothing!
     */
    public void disabled()
    {
        zeroAll();
        //swerveDrive.disable();
        
        while( isDisabled() )
        {
            if ( !Time.newCycle() )
                continue;

            acceptTargetingData = false;
            handleTurretAutoTargeting();
            if( DEBUG_MODE )
                debugPrint();
        }//end disabled loop
        
        acceptTargetingData = true;
    }//end disabled
    
    /**
     * This function is called once each time the robot enters operator control.
     * 
     * Every cycle, operatorControl will 
     */
    public void operatorControl()
    {
       /*
        while(isEnabled() && isOperatorControl() )
        {
            if (rightJoy.getRawButton(1))
                Objects.intakeSystem.turnOn();
            else
                Objects.intakeSystem.turnOff();
            
            
        }
        /**/
        zeroAll();
        swerveDrive.enable();

        while( isEnabled() && isOperatorControl() )
        {
            swerveDrive.enable();
            // place time-sensitive code below this line
            if ( !Time.newCycle() )
                continue;

            //get joystick axis input
            Y       = Util.joystickDeadZone( -leftJoy.getY() );
            X       = Util.joystickDeadZone( -leftJoy.getX()  );
            T       = Util.joystickDeadZone( rightJoy.getX() );
            turtRot = Util.joystickDeadZone( -shooterJoy.getZ() ); //twist
            // the /2 and *2 make the deadzone larger w/out losing any data
            
            turtPow = ( -shooterJoy.getThrottle() );
            turtPow = ( 1 + turtPow ) / 2; //moves from [-1, 1] to [0, 1]
            turtPow = Util.joystickDeadZone(turtPow);
            
            //now mathematicly modify the axises
            //T =   (T > 0)  ?  T*T  :  -(T*T); //square weights downward; keep the sign
            //turtPow = Mathf.sqrt(turtPow);    //sqrt weights upward
            
            //check the various buttons and execute commands as needed
            //isManual = shooterJoy.axisToButton( THROTTLE, false, true );
            handleUniversalInput();
            if ( isManual )
                handleManualModeInput();
            else
                handleAutomaticModeInput();

            // update the swerve drive
            if ( lockSwerve )
                swerveDrive.lockDown();
            else
                swerveDrive.update( Y, X, T );
            
            handleTurretAutoTargeting();
            
            //turret.setSpeedOffset( rpmOffset );
            // override kinect input if the driver wants to turn the turret manually
            if ( turtRot != 0 )
                turret.setAngleObjective( turtRot );
            turret.update();

            //print various debugging output
            if( DEBUG_MODE )
                debugPrint();
        } //end operator control loop
    }// end operator control

    /**
     * Checks all the buttons related to controlling the turret while auto-targeting
     * is enabled
     * 
     * as of 3-2-2012 this binds:
     *      shooter trigger (1) = shoot with checks (hold)
     *      shooter 4           = ignore targeting computer
     *      shooter 6           =  watch targeting computer  
     */
    protected void handleAutomaticModeInput()
    {
        //toggle on/off accepting targeting data -- absolute set on SHOOTER 6, -4
        if( shooterJoy.getRawButton(4) )
            acceptTargetingData = false;
        else if( shooterJoy.getRawButton(6) )
            acceptTargetingData = true;

        
        //handle launching balls -- hold on SHOOTER 1
        if( shooterJoy.getRawButton(1) )
            turret.shootWithChecks();
        else
            turret.stopShooting();
    } //end handle automatic controls

    /**
     * checks all the buttons related to controlling the turret while auto-targeting
     *  is disabled
     * 
     * as of 3-2-2012 this binds:
     *      shooter trigger (1) : shoot WITHOUT checks -- feeder and intake ON (hold)
     *      shooter throttle    : set target RPM based on linear interpolation of the max and min RPM values
     *      shooter 4           : hold to set target RPM to a preset value that will hit the top from the key
     *      shooter 6           : hold to set target RPM to a preset value that will hit the top from the center
     */
    public void handleManualModeInput()
    {        
        //handle turret shooting -- hold on SHOOTER 1
        if( shooterJoy.getRawButton(1) )
            turret.shoot();           //turns on the feeder, but not the shooter
        else
            turret.stopShooting();
        
        //handle turret left/right  -- absolute setting on SHOOTER TWIST
        //turret.setAngleObjective(turtRot); //done in both already
         if( turtPow == 0 )
            turret.setSpeedObjective(0);
        else
            turret.setSpeedObjective( Mathf.lerp( MIN_RPM, MAX_RPM, turtPow ) );
        
        if( shooterJoy.getRawButton(6) )
            turret.setSpeedObjective( MID_RPM );
        else if ( shooterJoy.getRawButton(4) )
            turret.setSpeedObjective( KEY_RPM );

        //handle turret power -- absolute setting on SHOOTER throttle
    } //end manual mode controls
    
    /**
     * Checks all buttons related to controlling the robot. These do not change
     * no matter if the robot is in manual mode or automatic mode.
     * 
     * as of 3-2-2012 this binds  :
     *      shooter hat switch    : set auto target to aim at the HAT_DIRECTION hoop
     *      shooter 10            : RPM offset += 25 (toggle)
     *      shooter 9             : RPM offset -= 25 (toggle)
     *      shooter 2             : spin up shooter  (hold)
     *      left 3 or shooter 7   : bring down ramp manipulator (hold)
     *      left 2 or shooter 8   : bring up ramp manipulator   (hold)
     *      right 3 or shooter 3  : turn on both the intake and conveyor (hold)
     *      right 2 or shooter 4  : reverse both the intake and conveyor (hold)
     *      right 1 or shooter 11 : lock down swerve drive into the X position (hold)
     *      left  1               : reduce left joystick axis by 1/3 (hold)
     *      shooter 12            : toggle in/out of manual. This resets the following vars:
     *                                  - rpm offset         --> 0
     *                                  - target hoop        --> top
     *                                  - targeting computer --> enabled
     *      shooter twist axis   : directly power the turret motor
     * 
     */
    protected void handleUniversalInput()
    {
        
        //select the auto-aim target -- absolute hold/set on shooter hat switch
        if( shooterJoy.getRawButton( EnhancedJoystick.BUTTON_DU ) )
            autoTarget.cycleTarget( AutoTarget.TOP );
        else if( shooterJoy.getRawButton( EnhancedJoystick.BUTTON_DL ) )
            autoTarget.cycleTarget( AutoTarget.LEFT );
        else if( shooterJoy.getRawButton( EnhancedJoystick.BUTTON_DD ) )
            autoTarget.cycleTarget( AutoTarget.BOTTOM );
        else if( shooterJoy.getRawButton( EnhancedJoystick.BUTTON_DR ) )
            autoTarget.cycleTarget( AutoTarget.RIGHT );
        
        //adjust rpm offset -- toggle on SHOOTER 10, -9
        if( shooterJoy.getButtons() != 0)
        {
            if( !lastOffsetHeld )
            {
                if( shooterJoy.getRawButton(10) )
                {
                    rpmOffset += 25;
                    lastOffsetHeld = true;
                }
                else if( shooterJoy.getRawButton(9) )
                {
                    rpmOffset -= 25;
                    lastOffsetHeld = true;
                }
            }
        }
        else
            lastOffsetHeld = false;
        
        //handle shooter spinup / spindown  -- hold on SHOOTER 2
        if( shooterJoy.getRawButton(2) )
            turret.spinUpShooter();
        else
            turret.spinDownShooter();
        
        
        
        //move the ramp arm -- hold on RIGHT 3, -4 and SHOOTER 8, -7
        if( leftJoy.getRawButton(3) || shooterJoy.getRawButton(7) )
            rampArticulate.bringDown();
        else if( leftJoy.getRawButton(2) || shooterJoy.getRawButton(8) )
            rampArticulate.bringUp();
        else
            rampArticulate.turnOff();
        
        //handle intakeAndConvayor and conveyor -- hold on SHOOTER 3, -5
        if( rightJoy.getRawButton(3) || shooterJoy.getRawButton(3) )
            intakeAndConvayor.turnOn();
        else if( rightJoy.getRawButton(2) || shooterJoy.getRawButton(5) )
            intakeAndConvayor.turnReverse();
        else
            intakeAndConvayor.turnOff();
        
        // Slows down robot for micro adjustment -- hold on LEFT 3
        if ( leftJoy.getRawButton( 1 ) )
        {
            Y *= 0.333;
            X *= 0.333;
            T *= 0.333;
        }
        // Sets all wheels inward to "Lock down" and prevent movement -- hold on LEFT 2 and SHOOTER 11
        else if ( rightJoy.getRawButton( 1 ) || shooterJoy.getRawButton( 11 ) )
            lockSwerve = true;
        else
            lockSwerve = false;
        
        //toggle manual shooting mode -- toggle on SHOOTER 12
        if( shooterJoy.getRawButton(12) )
        {
            if( !lastManualButtonHeld )
            {
                //toggle manual
                isManual = !isManual; 
                lastManualButtonHeld = true;
                
                
                //reset certain variables (but not zero all)
                rpmOffset = DEFAULT_RPM_OFFSET;
                turret.setSpeedObjective(0);
                turret.setSpeedOffset(0);
                turret.setAngleObjective(0);
                autoTarget.cycleTarget( AutoTarget.TOP );
                acceptTargetingData = !isManual; //manual true --> no data
            }
        }
        else
            lastManualButtonHeld = false;
        
        
        if( shooterJoy.getRawButton(9) && shooterJoy.getRawButton(10) )
            Objects.feederSpike.set( Relay.Value.kReverse);
    } //end handle universal controls
    

    /**
     * handles the actions needed to maintain the auto targeting computer.
     * Does (almost) nothing if <code>acceptTargetingData</code> is false.
     */
    protected void handleTurretAutoTargeting()
    {
        autoTarget.checkForBlockedConnection();
        //handle targeting computer connection
        if ( autoTarget.newData() )
        {
            newData++;
            if( acceptTargetingData )
            {
                turret.setAngleObjective( autoTarget.getTurretAngle() );
                turret.setSpeedObjective( autoTarget.getTurretPower() );
            }
            autoTarget.clearNewData();
        }
        turret.setSpeedOffset(rpmOffset);
    }
    protected int newData = 0;
    /**
     * Prints out debug data continuously.
     */
    protected void debugPrint()
    {
        if ( Time.time() > printTime )
        {
            log( "network updates per second    : " + newData/DEBUG_UPDATE_INTERVAL
                 + "\nnetwork Distance to target  : " + autoTarget.getPacket().distance
                 + "\ncurrent RPM               : " + turret.getShooterRPM()
                 + "\ntarget  RPM               : " + turret.getShooterTargetRPM()
                 + "\ncurrent rotate setting    : " + turret.getRotateSetting()
                 + "\n current RPM offset       : " + rpmOffset);
            log( "FR angle: " + Objects.frSteeringEncoder.get().get() );
            log( "FL angle: " + Objects.flSteeringEncoder.get().get() );
            log( "BL angle: " + Objects.blSteeringEncoder.get().get() );
            log( "BR angle: " + Objects.brSteeringEncoder.get().get() );
            //log( "target angle : " + ((SwerveDrive)swerveDrive).target.get() );
            log( "Y, X, T: " + Y + ", " + X + ", " + T );
            
            newData = 0;

            /*
             * log("lastPID......: " + Electronics.FL_POS_PID.getLastPID() );
             * log("mtr setting..: " + Electronics.FL_POS_PID.getLastPID() );
             */
            printTime = Time.time() + DEBUG_UPDATE_INTERVAL;
        }
    }

    /**
     * Resets various variables to their very first initialize and virtuous state.
     */
    public void zeroAll()
    {
        edu.wpi.first.wpilibj.Watchdog.getInstance().setEnabled(false);
        Time.newCycle();
        Time.newCycle();
        
        isManual                      = false;
        lastManualButtonHeld          = false;
        lockSwerve                    = false;
        acceptTargetingData           = true;
        lastAutoAimButtonHeld         = false;
        lastOffsetHeld                = false;
        Y = X = T                     = 0;
        rpmOffset                     = DEFAULT_RPM_OFFSET;

        turret.setAngleObjective(0);
        turret.setSpeedObjective(0);
        turret.setSpeedOffset(0);
        autoTarget.cycleTarget( AutoTarget.TOP );
        swerveDrive.stop();
        System.gc();
    }

    /**
     * Terse System.out.println that adds the current time to the print out.
     * @param s  the string to print.
     */
    public static void log(String s)
    {
        System.out.println( Time.string() + ":.........." + s );
    }
}

//FIRST FRC team 691 2012 competition code
