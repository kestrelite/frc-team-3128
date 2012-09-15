package util.Controller;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import util.EventManager.EventMono;

public class Controller extends Joystick implements EventMono {

    boolean[] buttons = new boolean[10];
    double[] driveVals = new double[5];
    boolean runButtons = false;
    boolean runDrive = false;
    DriveLayout d;

    public Controller(int a, DriveLayout d) {
        super(a);
        this.d = d;
    }

    public void iterate() {
        for (int i = 0; i < buttons.length; i++) {
            if (this.getRawButton(i+1) != buttons[i]) {
                if (this.getRawButton(i+1)) {
                    d.buttonEnable(i);
                }
                if (!this.getRawButton(i+1)) {
                    d.buttonDisable(i);
                }
                buttons[i] = this.getRawButton(i+1);
            }
        }
        boolean thing = false;
        for (int i = 0; i < driveVals.length; i++) {
            if (this.getRawAxis(i + 1) != driveVals[i]) {
                thing = true;
            }
        }
        if (thing) {
            d.dEvent(this.getRawAxis(ButtonMap.X1), this.getRawAxis(ButtonMap.Y1),
                    this.getRawAxis(ButtonMap.TR), this.getRawAxis(ButtonMap.X2),
                    this.getRawAxis(ButtonMap.Y2));
        }
    }

    public boolean shouldIterate() {
        return true;
    }

    public boolean shouldRemove() {
        return false;
    }
}
