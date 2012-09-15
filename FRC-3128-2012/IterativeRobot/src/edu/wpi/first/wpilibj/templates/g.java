package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.EventManager.EventTimer;
import edu.wpi.first.wpilibj.templates.MotorControl.MotorController;
import edu.wpi.first.wpilibj.templates.XBoxController.ButtonMap;
import edu.wpi.first.wpilibj.templates.XBoxController.DriveAbstract;
import edu.wpi.first.wpilibj.templates.XBoxController.XBoxController;

import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.MotorControl.EncoderController;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticManager;
import edu.wpi.first.wpilibj.templates.DriveCode.*;

class SwagLights {

    static Relay redLED = new Relay(2, 1);
    static Relay blueLED = new Relay(2, 2);

    public SwagLights() {
        if (DriverStation.Alliance.kBlue.value == DriverStation.Alliance.kBlue_val) {
            redLED.set(Relay.Value.kOff);
            blueLED.set(Relay.Value.kOn);
        } else {
            redLED.set(Relay.Value.kOn);
            blueLED.set(Relay.Value.kOff);
        }
    }
}

class ETimerTest implements EventInterface {

    public boolean eventIterable() throws Exception {
        return false;
    }

    public void eventProcessor() throws Exception {
        System.out.println("Timer has been triggered.");
    }

    public void eventRegisterSelf() throws Exception {
        EventManager.addEventTrigger(this);
    }
}

public class g {

    private static Object trash;
    //static XBoxController xControlFd, xControlSwerve;
    //static XBoxController xControlDebug;
    public static ButtonMap b = new ButtonMap();
    public static XBoxController xControl, xControlBAD;
    private static ThreadLock controllerLock = new ThreadLock();
    private static ThreadLock controllerLock2 = new ThreadLock();
    public static MotorController mLB, mRB, mLF, mRF, mLBTurn, mRBTurn, mLFTurn, mRFTurn, mBAD, mBAD2;
    private static ThreadLock mLBLock = new ThreadLock();
    private static ThreadLock mRBLock = new ThreadLock();
    private static ThreadLock mLFLock = new ThreadLock();
    private static ThreadLock mRFLock = new ThreadLock();
    private static ThreadLock mLBTurnLock = new ThreadLock();
    private static ThreadLock mRBTurnLock = new ThreadLock();
    private static ThreadLock mLFTurnLock = new ThreadLock();
    private static ThreadLock mRFTurnLock = new ThreadLock();
    private static ThreadLock mBADLock = new ThreadLock();
    //static SwagLights swag = new SwagLights();
    public static Gyro rotGyro = new Gyro(1, 1);
    public static int bridgeSol, catSol;
    public static EventTimer catapultTimer;
    private static Compressor comp = new Compressor(1, 1, 1, 8);
    private static PneumaticManager pm;

    public static double normalize(double a) {
        if(Math.floor(Math.abs(a)) == 180) return a;
        while (Math.abs(a) > 180) {
            a -= 360 * (Math.abs(a) / a);
        }
        return a;
    }

    public static double round(double a, int places) {
        return a;
        //return ((double)Math.floor(a/((double)MathUtils.pow(10,places)) + .5))/((double)MathUtils.pow(10, places));
    }
    public final String driveIsSwerve = "dSWERVE";
    public final String driveIsTank = "dTANK";
    public final String driveIsReset = "dRESET";
    public final String driveIsEmpty = "dEMPTY";
    public final String driveIsAuto = "dAUTO";

    public static void instantiate(String driveType, boolean pneumaticsEnabled) throws Exception {

        if (pneumaticsEnabled) {
            comp.start();
            try {
                pm = new PneumaticManager(comp);
            } catch (Exception e) {
                throw new Error(e.getMessage());
            }
            bridgeSol = PneumaticManager.addDualSolenoid(1, 1, 1, 2);
            catSol = PneumaticManager.addDualSolenoid(1, 3, 1, 4);
            PneumaticManager.getSolenoid(1).extend();
            PneumaticManager.getSolenoid(0).retract();
            g.catapultTimer = new EventTimer(new CatapultHandler(catSol, true));
        }

        mLB = new MotorController(1, 9, mLBLock);
        mRB = new MotorController(1, 7, mRBLock);
        mLF = new MotorController(1, 3, mLFLock);
        mRF = new MotorController(1, 8, mRFLock);
        mRBTurn = new MotorController(1, 6, new EncoderController(2, 1, 2, 2), mRBTurnLock);
        mRFTurn = new MotorController(1, 5, new EncoderController(2, 3, 2, 4), mRFTurnLock);
        mLFTurn = new MotorController(1, 1, new EncoderController(2, 5, 2, 6), mLFTurnLock);
        //new EncoderController(2,9,2,10);
        mLBTurn = new MotorController(1, 4, new EncoderController(2, 11, 2, 13), mLBTurnLock);
        mBAD = new MotorController(1, 2, mBADLock);
        mBAD2 = new MotorController(1, 10, mBADLock);
        mBAD2.reverseMotor();
        mRF.reverseMotor();
        mRB.reverseMotor();
        mRFTurn.reverseMotor();
        mRFTurn.reverseEncoder();
        mRBTurn.reverseEncoder();
        mRBTurn.reverseMotor();
        mLFTurn.reverseEncoder();
        //mLFTurn.reverseMotor();
        //mLBTurn.reverseEncoder(); //Uncommented... probably?

        if (driveType.equals("dSWERVE")) {
            xControl = new XBoxController(2, controllerLock,
                    new DriveSwerve(mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn));
        }
        if (driveType.equals("dRESET")) {
            xControl = new XBoxController(2, controllerLock,
                    new DriveReset(mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn, mBAD, mBAD2));
        }
        if (driveType.equals("dTANK")) {
            xControl = new XBoxController(2, controllerLock,
                    new DriveTank(mLB, mLBTurn, mRB, mRBTurn, mLF, mLFTurn, mRF, mRFTurn, mBAD, mBAD2));
        }
        //if (driveType.equals("dEMPTY")) {
            //xControl = new XBoxControllerNew(1, controllerLock,
            //        new EmptyDrive(), new AutoRecorder());
        //}

        //if (driveType.equals("dAUTO")) {
            //xControl = new XBoxControllerNew(1, controllerLock, new EmptyDrive(), new AutoExample());
        //}
        
        xControlBAD = new XBoxController(1, controllerLock2, new DriveBAD(mBAD, mBAD2));

        if (xControl == null) {
            throw new Error("Must assign valid controller, you idiot.");
        }

        xControl.startEventMonitor(false);
        xControlBAD.startEventMonitor(false);

        //g.e = new EventTimer(new ETimerTest());

        EventManager.addIterable(new WatchdogMonitor());
        EventManager.addIterable(new IPSCounter());
        //e.enableTargetTime(5);
    }
    public static EventTimer e;
}