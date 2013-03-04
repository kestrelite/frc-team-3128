package tracking;

import edu.wpi.first.wpijavacv.WPIPolygon;

public class Target {

    public static int TARGET_COUNT = 0;
    private int number;
    private WPIPolygon polygon;
    private int framesMissing;

    public Target(int number, WPIPolygon polygon) {
        this.number = number;
        this.polygon = polygon;

        framesMissing = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public WPIPolygon getPolygon() {
        return polygon;
    }

    public void setPolygon(WPIPolygon polygon) {
        this.polygon = polygon;
    }

    public int getFramesMissing() {
        return framesMissing;
    }

    public void setFramesMissing(int framesMissing) {
        this.framesMissing = framesMissing;
    }

    public void missing() {
        framesMissing++;
    }
}
