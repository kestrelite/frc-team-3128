package frc3128.PneumaticsManager;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import frc3128.DebugLog;
import frc3128.EventManager.Event;
import frc3128.ListenerManager.ListenerManager;
import java.util.Vector;

class LockAllPistons extends Event {
    public void execute() {
        for(int i = 0; i < PneumaticsManager.dualSolenoidList.size(); i++)
            ((DualSolenoid) PneumaticsManager.dualSolenoidList.elementAt(i)).unlockPiston();
    }
}

public class PneumaticsManager {
    private   static Compressor c;
    private   static boolean compressorSet = false;
    protected static Vector dualSolenoidList = new Vector();
    private   static final String referenceName = "PneumaticsManager";

    public PneumaticsManager(Compressor c) {
        PneumaticsManager.c = c;
        PneumaticsManager.compressorSet = true;
        ListenerManager.addListener(new LockAllPistons(), "lockPistons");
        PneumaticsManager.c.stop();
    }

    public PneumaticsManager(int a, int b, int c, int d) {
        PneumaticsManager.c = new Compressor(a, b, c, d);
        PneumaticsManager.compressorSet = true;
        ListenerManager.addListener(new LockAllPistons(), "lockPistons");
        PneumaticsManager.c.stop();
    }
    
    public static PistonID addPiston(Solenoid solA, Solenoid solB, boolean solAState, boolean solBState) {
        dualSolenoidList.addElement(new DualSolenoid(solA, solB, solAState, solBState));
        return new PistonID(dualSolenoidList.size() - 1);
    }

    public static PistonID addPiston(int a, int b, int c, int d, boolean solAState, boolean solBState) {
        dualSolenoidList.addElement(new DualSolenoid(new Solenoid(a, b), new Solenoid(c, d), solAState, solBState));
        return new PistonID(dualSolenoidList.size() - 1);
    }

    public static void setPistonStateLocked(PistonID id) {
        if(!id.getInversion())
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).lockPiston();
        else
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).unlockPiston();
    }

    public static void setPistonStateUnlocked(PistonID id) {
        if(!id.getInversion())
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).unlockPiston();
        else
            ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).lockPiston();
    }

    public static void setPistonStateOn(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOn();
    }

    public static void setPistonStateOff(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonOff();
    }

    public static void setPistonInvertState(PistonID id) {
        ((DualSolenoid) dualSolenoidList.elementAt(id.getID())).setPistonInverted();
    }
    
    public static void setCompressorStateOn() {
        if(PneumaticsManager.compressorSet)
            PneumaticsManager.c.start();
        if(!PneumaticsManager.compressorSet)
            DebugLog.log(1, referenceName, "Compressor is being started without first being instantiated!");
    }

    public static void setCompressorStateOff() {
        if(PneumaticsManager.compressorSet)
            PneumaticsManager.c.stop();
        if(!PneumaticsManager.compressorSet)
            DebugLog.log(1, referenceName, "Compressor is being stopped without first being instantiated!");
    }

    public static void lockAllPistons() {
        for(int i = 0; i < dualSolenoidList.size(); i++)
            ((DualSolenoid) dualSolenoidList.elementAt(i)).lockPiston();
    }
}
