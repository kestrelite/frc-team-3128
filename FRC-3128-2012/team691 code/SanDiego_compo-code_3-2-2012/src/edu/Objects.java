
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu;

import edu.io.AutoTarget;
import edu.wpi.first.wpilibj.*;
import org.team691.drive.base.Drive;
import org.team691.drive.swerve.SwerveDrive;
import org.team691.util.*;

/**
 *
 * @author SierraTango
 */
public class Objects
{
    
    public static final int SIDECAR_1         = 1; //1 //good
    public static final int SIDECAR_2         = 2; //2 //good
    
    
    public static final int RIGHT_JOYSTICK_PORT = 2;
    public static final int LEFT_JOYSTICK_PORT  = 1;
    public static final int SHOOTER_JOYSTICK_PORT  = 3;
    
    
    public static final int L_DRIVE_1           = 4; //2 //good
    //public static final int L_DRIVE_2           = 2;
    public static final int R_DRIVE_1           = 3; //1 //good
    //public static final int R_DRIVE_2           = 9;
    
    public static final int FR_ENC              = 1; //1
    public static final int FR_PIVOT            = 2; //1 
    public static final int FL_ENC              = 2; //1
    public static final int FL_PIVOT            = 5; //2 //good
    public static final int BL_ENC              = 3; //1
    public static final int BL_PIVOT            = 2; //2 //good
    public static final int BR_ENC              = 4; //1
    public static final int BR_PIVOT            = 5; //1     
    /*
     * V4 = 2,2 BL pivot
     * V3 = 2,3 L Drive 2 (D/C)
     * V2 = 2,4 L Drive
     * V1 = 2,5 FL pivot
     * 
     * V8 = 1,5
     * V7 = 1,4
     * V6 = 1,3
     * V5 = 1,2
     */
    
   // public static final int INTAKE_LIMIT_1      = -1;
   // public static final int INTAKE_LIMIT_2      = -1;
    public static final int L_DRIVE_ENCODER_A   = 7;
    public static final int L_DRIVE_ENCODER_B   = 8;
    public static final int RAMP_MAN_R          = 4; //1 
    public static final int RAMP_MAN_L          = 3; //1 
    public static final int R_DRIVE_ENCODER_A   = 5;
    public static final int R_DRIVE_ENCODER_B   = 6;

    // public static final int SHOOTER_ENCODER_A     = -1;
    // public static final int SHOOTER_ENCODER_B     = -1;
  //  public static final int TRANSFER_SPIKE_2      = 7;
  //  public static final int TURRET_LEFT_LIMIT     = -1;
  //  public static final int TURRET_RIGHT_LIMIT    = -1;
    public static final int SHOOTER_ENCODER     = 14; //1 //good
    public static final int SHOOTER_RPM_R          = 6; //1 //good
    public static final int SHOOTER_RPM_L          = 10; //1 //good
    public static final int FEEDER_SPIKE           = 8; //1 //good
    public static final int TURRET_TURN           = 6; //2 //good
    
    public static final int INTAKE_AND_TRANSFER_VICTOR = 7; //2 //good
    
    public final static Angle FR_OFFSET           = new Angle( -133 );
    public final static Angle FL_OFFSET           = new Angle( 171.5 );
    public final static Angle BR_OFFSET           = new Angle( -39 );
    public final static Angle BL_OFFSET           = new Angle( 42 ); //bad
    /*
     * 62.02:..........FR angle: 135.05274410297034
        62.02:..........FL angle: 13.549427366336687
        62.02:..........BL angle: 1.9919846178217888
        62.02:..........BR angle: 34.50988814257429
        62.02:..........Y, X, T: 0.0, 0.0, 0.0
     */
    

    // User input----------------------------------------------------------------
    public static Joystick rightJoy   = new Joystick( RIGHT_JOYSTICK_PORT );
    public static Joystick leftJoy    = new Joystick( LEFT_JOYSTICK_PORT );
    public static EnhancedJoystick shooterJoy = new EnhancedJoystick( SHOOTER_JOYSTICK_PORT );

    // End user input------------------------------------------------------------

    // Drive components----------------------------------------------------------

    // right velocity control
    public static SpeedController rDriveVictor1 = new Victor( SIDECAR_1, R_DRIVE_1 );
    //public static SpeedController rDriveVictor2 = new Victor( SIDECAR_1, R_DRIVE_2 );

    public static SpeedController rDrive =  rDriveVictor1;
                                          //new DoubleSpeedController( rDriveVictor1,
                                          //                        rDriveVictor2,
                                          //                        false, false );

    public static Encoder rDriveEncoder = new Encoder( SIDECAR_1, R_DRIVE_ENCODER_A,
                                                       SIDECAR_1, R_DRIVE_ENCODER_B,
                                                       false,
                                                       CounterBase.EncodingType.k2X );

    // left velocity control
    public static SpeedController lDriveVictor1 = new Victor( SIDECAR_2, L_DRIVE_1 );
    //public static SpeedController lDriveVictor2 = new Victor( SIDECAR_1, L_DRIVE_2 );

    public static SpeedController lDrive        = lDriveVictor1;
                                                  //new DoubleSpeedController( lDriveVictor1,
                                                  //                    lDriveVictor2,
                                                  //                    false, false );

    public static Encoder lDriveEncoder = new Encoder( SIDECAR_1, L_DRIVE_ENCODER_A,
                                                       SIDECAR_1, L_DRIVE_ENCODER_B,
                                                       false,
                                                       CounterBase.EncodingType.k2X );

    // front-right position control
    public static SpeedController frSteeringVictor = new Victor( SIDECAR_1, FR_PIVOT );

    public static AngleSensor frSteeringEncoder    = new AngleSensor( 1, FR_ENC,
                                                                   FR_OFFSET );
    public static AngleMotor frSteeringController = new AngleMotor( frSteeringVictor,
                                                                    frSteeringEncoder );

    // front-left position control
    public static SpeedController flSteeringVictor = new Victor( SIDECAR_2, FL_PIVOT );
    public static AngleSensor flSteeringEncoder    = new AngleSensor( 1, FL_ENC,
                                                                   FL_OFFSET );
    public static AngleMotor flSteeringController = new AngleMotor( flSteeringVictor,
                                                                    flSteeringEncoder );

    // back-left position control
    public static SpeedController blSteeringVictor = new Victor( SIDECAR_2, BL_PIVOT );
    public static AngleSensor blSteeringEncoder    = new AngleSensor( 1, BL_ENC,
                                                                   BL_OFFSET );
    public static AngleMotor blSteeringController = new AngleMotor( blSteeringVictor,
                                                                    blSteeringEncoder );

    // back-right position control
    public static SpeedController brSteeringVictor = new Victor( SIDECAR_1, BR_PIVOT );
    public static AngleSensor brSteeringEncoder    = new AngleSensor( 1, BR_ENC,
                                                                   BR_OFFSET );
    public static AngleMotor brSteeringController = new AngleMotor( brSteeringVictor,
                                                                    brSteeringEncoder );

    // the swerve drive object

    // End drive components-----------------------------------------------------

    // Shooter and turret components--------------------------------------------

    // shooter components
    public static SpeedController shooterRPMVictorR = new Jaguar( SIDECAR_1,
                                                                  SHOOTER_RPM_R );

    public static SpeedController shooterRPMVictorL = new Jaguar( SIDECAR_1,
                                                                  SHOOTER_RPM_L );

    public static DoubleSpeedController shooterDoubleMotor =
        new DoubleSpeedController( shooterRPMVictorR, shooterRPMVictorL, false, true );

    public static CounterBase shooterRPMEncoder = new Counter();
    public static RPMMotor turretShooterMotor   = new RPMMotor( shooterDoubleMotor,
                                                              shooterRPMEncoder );

    // turret components
    public static SpeedController turretRotateVictor = new Victor( SIDECAR_2,
                                                                   TURRET_TURN );

    /*
    public static DigitalInput turretLimitRight = new DigitalInput( SIDECAR_2,
                                                                    TURRET_RIGHT_LIMIT );
    public static DigitalInput turretLimitLeft = new DigitalInput( SIDECAR_2,
                                                                   TURRET_LEFT_LIMIT );
    */
    // turret logic object

    // End shooter and turret components----------------------------------------

    // manipulation objects-----------------------------------------------------

    public static Relay feederSpike = new Relay( SIDECAR_1, FEEDER_SPIKE );

    // public static Relay convayorSpike = new Relay( SIDECAR_2, TRANSFER_SPIKE_2 );

    public static Relay rampSpikeR             = new Relay( SIDECAR_1, RAMP_MAN_R);
    public static Relay rampSpikeL             = new Relay( SIDECAR_1, RAMP_MAN_L);
  //  public static Relay rampSpike              = new Relay( SIDECAR_2, RAMP_SPIKE );
    public static SpeedController intakeAndConvayorSpike = new Victor( SIDECAR_2, INTAKE_AND_TRANSFER_VICTOR );

//    public static DigitalInput intakeLimit2    = new DigitalInput( SIDECAR_2,
//                                                                INTAKE_LIMIT_2 );
//    public static DigitalInput intakeLimit1 = new DigitalInput( SIDECAR_2,
//                                                                INTAKE_LIMIT_1 );

    
    // end manipulation components-----------------------------------------------

    // camera tracking object

    // shooter encoder setup-----------------------------------------------------
    static
    {
        ( (Counter)shooterRPMEncoder ).setUpSource( SIDECAR_1, SHOOTER_ENCODER );
        ( (Counter)shooterRPMEncoder ).clearDownSource();
        ( (Counter)shooterRPMEncoder ).setMaxPeriod( 200 );
        ( (Counter)shooterRPMEncoder ).setUpSourceEdge( true, false );
        ( (Counter)shooterRPMEncoder ).start();
    }

    // end shooter encoder setup-------------------------------------------------

    // drive PID calibration-----------------------------------------------------
    // scale, KP, KI, KD = (DEFAULT_SCALE, 70, 5, 35)
    public static final double DEFAULT_SCALE = 0.000035; // 0.000075

    static
    {
        // FRONT RIGHT
        frSteeringController.setPID( new PID( -DEFAULT_SCALE * 0.5, 126, 3.2, 1700)); //148.5, 4.5, 1452 ) ); // 315, 23.86, 2227.5) );

        // FRONT LEFT
        flSteeringController.setPID( new PID( -DEFAULT_SCALE * 0.5,120, 3, 1600));// 132, 4, 1452 ) ); // 280, 21.21, 1386) );

        // BACK LEFT                     watch the +/- !
        blSteeringController.setPID( new PID( -DEFAULT_SCALE * 0.5, 132, 3.4, 1800));//132, 3.6, 1613 ) );

        // BACK RIGHT
        brSteeringController.setPID( new PID(  DEFAULT_SCALE * 0.5, 125, 3.2, 1900));//247.5, 4.95, 4125 ) ); // 525, 26.25, 3937.5) );
    }

    public static Drive swerveDrive     = new TankDrive();//new SwerveDrive();
    public static AutoTarget autoTarget = new AutoTarget();
    public static IntakeAndConveyor intakeSystem      = new IntakeAndConveyor();
    public static RampArticulate armSystem = new RampArticulate( false, true );
    public static Turret turret = new Turret();
    // end drive PID calibration-------------------------------------------------

}


//FIRST FRC team 691 2012 competition code
