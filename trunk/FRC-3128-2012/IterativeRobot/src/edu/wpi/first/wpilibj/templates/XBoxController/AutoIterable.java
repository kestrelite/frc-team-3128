package edu.wpi.first.wpilibj.templates.XBoxController;

public class AutoIterable {

    double[][] auto;
    private int iteration = 0;
    private final double[] blank = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /*
     * 1: y1 2: y2 3: x1 4: x2 5: triggers
     */

    public AutoIterable(double[][] auto) {
        this.auto = auto;
    }

    public double getRawAxis(int axis) {
        return auto[iteration][axis];
    }

    public void iterate() {
        iteration++;
    }

    public boolean getRawButton(int btn) {
        if (auto[iteration][btn + 5] == 1) {
            return true;
        }
        return false;
    }
}
