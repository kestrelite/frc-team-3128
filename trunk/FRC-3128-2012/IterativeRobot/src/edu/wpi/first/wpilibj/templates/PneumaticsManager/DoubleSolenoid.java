package edu.wpi.first.wpilibj.templates.PneumaticsManager;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;

public class DoubleSolenoid implements EventInterface {

    Solenoid solenoidA;
    Solenoid solenoidB;
    boolean updateableA = false;
    boolean updateableB = false;

    DoubleSolenoid(int a, int b, int c, int d) {
        solenoidA = new Solenoid(a, b);
        solenoidB = new Solenoid(c, d);
    }
    
    DoubleSolenoid(Solenoid solA, Solenoid solB)
    {
        solenoidA = solA;
        solenoidB = solB;
    }

    public void lockInPlace(boolean whilePressurized) throws Exception {
        if (whilePressurized) {
            updateableA = true;
            updateableB = true;
        } else {
            updateableA = true;
            updateableB = true;
        }
        this.eventRegisterSelf();
    }

    public void extend() throws Exception {
        updateableA = false;
        updateableB = true;
        this.eventRegisterSelf();
    }

    public void retract() throws Exception {
        updateableA = true;
        updateableB = false;
        this.eventRegisterSelf();
    }

    public boolean eventIterable() throws Exception {
        return false;
    }

    public void eventProcessor() throws Exception {
        solenoidA.set(updateableA);
        solenoidB.set(updateableB);
    }

    public void eventRegisterSelf() throws Exception {
        EventManager.addEventTrigger(this);
    }
}
