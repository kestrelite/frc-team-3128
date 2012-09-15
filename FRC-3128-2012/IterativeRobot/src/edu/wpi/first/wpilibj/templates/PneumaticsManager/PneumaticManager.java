package edu.wpi.first.wpilibj.templates.PneumaticsManager;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.ThreadLock;

public class PneumaticManager implements EventInterface {

    static Compressor c;
    static ThreadLock cLock;
    static DoubleSolenoidList listOfSolenoids = new DoubleSolenoidList(4);

    public PneumaticManager(Compressor c) throws Exception {
        this.c = c;
        this.c.start();
    }


    //public static void assignCompressor(Compressor c) throws Exception {
    //    PneumaticManager.c = c;
    //    PneumaticManager.cLock = cLock;
    //}

    public static int addDualSolenoid(int a, int b, int c, int d) throws Exception {
        if (PneumaticManager.c == null) {
            throw new Exception("Must assign compressor first!");
        }

        DoubleSolenoid tempDS = new DoubleSolenoid(a, b, c, d);
        listOfSolenoids.add(tempDS);
        return listOfSolenoids.getIndexOf(tempDS);
    }
    
    public int addDualSolenoid(Solenoid solA, Solenoid solB) throws Exception
    {
        if (this.c == null) {
            throw new Error("Must assign compressor first!");
        }
        
        DoubleSolenoid tempDS = new DoubleSolenoid(solA, solB);
        listOfSolenoids.add(tempDS);
        return listOfSolenoids.getIndexOf(tempDS);
    }

    public static DoubleSolenoid getSolenoid(int indexOf) throws Exception {
        if (PneumaticManager.c == null) {
            throw new Error("Must assign compressor first!");

        }
        return listOfSolenoids.getIndex(indexOf);
    }

    public boolean eventIterable() throws Exception {
        return false;
    }

    public void eventProcessor() throws Exception {
    }

    public void eventRegisterSelf() throws Exception {
        EventManager.addEventTrigger(this);
    }
}
