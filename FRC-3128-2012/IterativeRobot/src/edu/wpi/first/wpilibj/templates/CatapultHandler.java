package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.templates.EventManager.EventInterface;
import edu.wpi.first.wpilibj.templates.EventManager.EventManager;
import edu.wpi.first.wpilibj.templates.PneumaticsManager.PneumaticManager;

public class CatapultHandler implements EventInterface {

    private int sol;
    private boolean state = false;

    public CatapultHandler(int a, boolean position) throws Exception {
        this.sol = a;
        this.state = position;
        this.eventRegisterSelf();
    }

    public static double determineTime() {
        return 1;
    }

    public void flip() throws Exception {
        state = !state;
        this.eventRegisterSelf();
    }

    public boolean eventIterable() throws Exception {
        return false;
    }

    public void eventProcessor() throws Exception {
        if (state) {
            PneumaticManager.getSolenoid(sol).extend();
        } else {
            PneumaticManager.getSolenoid(sol).retract();
        }
    }

    public final void eventRegisterSelf() throws Exception {
        EventManager.addEventTrigger(this);
    }
}
