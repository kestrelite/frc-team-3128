package frc3128;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc3128.Connection.CameraCon;
import frc3128.Controller.AttackControl;
import frc3128.Controller.XControl;
import frc3128.DriveTank.DriveTankAttack;
import frc3128.DriveTank.MotorPID;
import frc3128.EventManager.Event;
import frc3128.EventManager.EventManager;
import frc3128.EventManager.EventSequence.EventSequencer;
import frc3128.EventManager.EventSequence.SequenceEvent;
import frc3128.EventManager.EventSequence.SingleSequence;
import frc3128.EventManager.ListenerManager;
import frc3128.PneumaticsManager.PistonID;
import frc3128.PneumaticsManager.PneumaticsManager;

class DashboardOutputs extends Event {
    private long startIterTime = -1;
    private long lastIterTime = -1;
    private long ctr;
    
    public void execute() {
        if(startIterTime == -1) {startIterTime = System.currentTimeMillis();}
        this.lastIterTime = System.currentTimeMillis();
        ctr++;
        
        SmartDashboard.putNumber("gTurn: ", Global.gTurn.getAngle());
        
        SmartDashboard.putBoolean("Comp: ", PneumaticsManager.getCompressorState());
        SmartDashboard.putNumber("mSpin: ", Global.mShoot.get());
        SmartDashboard.putNumber("mLB: ", Global.mLB.get());
        SmartDashboard.putNumber("mRB: ", Global.mRB.get());
        SmartDashboard.putNumber("mLF: ", Global.mLF.get());
        SmartDashboard.putNumber("mRF: ", Global.mRF.get());
        SmartDashboard.putNumber("Angle: ", Global.getAngleFrom(Global.enc));
    }
}

public class Global {
    public final static String referenceName = "Global";
    public final static EventManager eventManager = new EventManager();
    public       static AttackControl aControl1;
    public       static AttackControl aControl2;
    
    public       static XControl xControl1;
    
    public final static PneumaticsManager pnManager = new PneumaticsManager(1, 1, 1, 2);
    public final static PistonID pstFire = PneumaticsManager.addPiston(1, 1, 1, 2, true, false);
    public final static PistonID pstLift = PneumaticsManager.addPiston(1, 3, 1 ,4, false, true);
    
    public       static DriveTankAttack driveTank;
    public final static Jaguar mLB     = new Jaguar(1, 1);
    public final static Jaguar mRB     = new Jaguar(1, 3);
    public final static Jaguar mLF     = new Jaguar(1, 2);
    public final static Jaguar mRF     = new Jaguar(1, 4);
    public final static Jaguar mShoot = new Jaguar(1, 7);
    public final static Jaguar mTilt = new Jaguar(1, 8);
    
    public final static AnalogChannel enc = new AnalogChannel(1, 3);
    public final static MotorPID mPIDTilt = new MotorPID(mTilt, enc);

    public final static Gyro gTurn = new Gyro(1, 1);
    
    public final static Relay camLight = new Relay(1, 1, Relay.Direction.kForward);

    public final static CameraCon dashConnection = new CameraCon();
    
    public static void initializeRobot() {
        Global.gTurn.reset();
        PneumaticsManager.setCompressorStateOn();
        DebugLog.setLogLevel(3);
        DebugLog.log(3, referenceName, "ROBOT INITIALIZATION COMPLETE");
        Global.mPIDTilt.setPID(1, 0, 0);
    }

    public static void initializeDisabled() {
        Global.camLight.set(Relay.Value.kOn);
        PneumaticsManager.setCompressorStateOn();
    }

    public static void initializeAuto() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();
        Global.gTurn.reset();
        dashConnection.registerIterableEvent();
        
        EventSequencer autoSequence = new EventSequencer();
        autoSequence.addEvent(new SingleSequence() {
            public void execute() {
                Global.mPIDTilt.setTargetAngle(190);
                Global.mPIDTilt.registerIterableEvent();
            }
        });
        autoSequence.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot.set(1);
            }
        });
        autoSequence.addEvent(new SequenceEvent() {
            long lastTime = 3000;
            public boolean exitConditionMet() {
                //if(this.getRunTimeMillis() > 1000 * 8) return true;
                return false;
            }

            public void execute() {
                if(this.getRunTimeMillis() - this.lastTime > 1250) {
                    PneumaticsManager.setPistonInvertState(Global.pstFire);
                    this.lastTime = this.getRunTimeMillis();
                }
            }
        });
        autoSequence.addEvent(new SingleSequence() {
            public void execute() {
                Global.mShoot.set(0);
            }
        });
        autoSequence.startSequence();
    }

    public static void initializeTeleop() {
        EventManager.dropEvents(); ListenerManager.dropAllListeners();

        Global.mShoot.set(0.0);
        
        Global.aControl1 = new AttackControl(3);
        Global.aControl2 = new AttackControl(2);                
        Global.xControl1 = new XControl(1);
        driveTank = new DriveTankAttack();
        
        dashConnection.registerIterableEvent(); 
    }

    public static void robotKill() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Watchdog.getInstance().kill();
    }
    
    public static void robotStop() {
        EventManager.dropEvents();
        ListenerManager.dropAllListeners();
        Global.stopMotors();
    }
    
    public static void robotPause() {
        EventManager.toggleEventProcessing();
        Global.stopMotors();
    }
    
    public static void stopMotors() {
        Global.mLF.set(0);
        Global.mRF.set(0);
        Global.mRB.set(0);
        Global.mLB.set(0);
        Global.mShoot.set(0);
        Global.mPIDTilt.stop();
    }
    
    public static double getAngleFrom(AnalogChannel c) {
        double voltage = 0, value = 0;
        for(char i = 0; i<10; i++) {
            voltage += c.getVoltage();
            value += c.getValue();
        }
        voltage /= 10; value /= 10;
        return voltage/5.0*360;
    }
    
    public static double signum(double a) {
        if(a == 0) return 0;
        return Math.abs(a)/a;
    }
}