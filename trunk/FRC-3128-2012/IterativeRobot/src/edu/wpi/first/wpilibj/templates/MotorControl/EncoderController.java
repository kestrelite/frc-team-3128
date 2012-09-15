package edu.wpi.first.wpilibj.templates.MotorControl;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.templates.g;

public class EncoderController extends Encoder {

    private double gearRatio = 26.0 / 22.0;
    private double resolution = 4.0;
    private double polarity = 1.0;

    public EncoderController(int a, int b, int c, int d) {
        super(a, b, c, d);
        this.reset();
        this.start();
    }

    public EncoderController(int a, int b, int c) {
        super(a, b, a, c);
        this.reset();
        this.start();
    }

    public void setGearRatio(double ratio) {
        gearRatio = ratio;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public void reversePolarity() {
        this.polarity *= -1;
    }

    public double getAngle() {
        return g.normalize((super.getRaw() * this.polarity) / ((this.resolution) * (this.gearRatio)));
    }
}
