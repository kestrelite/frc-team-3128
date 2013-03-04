
import java.util.Random;

public class VisionParameters {

    private int redFilter;
    private int greenFilter;
    private int blueFilter;
    private int erodeIterations;
    private int dilateIterations;
    private double polygonQuality;
    private static final int COLOR_FILTER_CHANGE = 10;
    private static final int COLOR_FILTER_MIN = 20;
    private static final int COLOR_FILTER_MAX = 100;
    private static final int ERODE_ITERATIONS_CHANGE = 1;
    private static final int ERODE_ITERATIONS_MIN = 0;
    private static final int ERODE_ITERATIONS_MAX = 5;
    private static final int DILATE_ITERATIONS_CHANGE = 1;
    private static final int DILATE_ITERATIONS_MIN = 0;
    private static final int DILATE_ITERATIONS_MAX = 10;
    private static final int POLYGON_QUALITY_CHANGE = 5;
    private static final int POLYGON_QUALITY_MIN = 10;
    private static final int POLYGON_QUALITY_MAX = 50;

    public VisionParameters() {
    }

    public VisionParameters(int redFilter, int greenFilter, int blueFilter, int erodeIterations,
            int dilateIterations, double polygonQuality) {
        this.redFilter = redFilter;
        this.greenFilter = greenFilter;
        this.blueFilter = blueFilter;
        this.erodeIterations = erodeIterations;
        this.dilateIterations = dilateIterations;
        this.polygonQuality = polygonQuality;
    }

    public int getBlueFilter() {
        return blueFilter;
    }

    public void setBlueFilter(int blueFilter) {
        this.blueFilter = blueFilter;
    }

    public int getDilateIterations() {
        return dilateIterations;
    }

    public void setDilateIterations(int dilateIterations) {
        this.dilateIterations = dilateIterations;
    }

    public int getErodeIterations() {
        return erodeIterations;
    }

    public void setErodeIterations(int erodeIterations) {
        this.erodeIterations = erodeIterations;
    }

    public int getGreenFilter() {
        return greenFilter;
    }

    public void setGreenFilter(int greenFilter) {
        this.greenFilter = greenFilter;
    }

    public double getPolygonQuality() {
        return polygonQuality;
    }

    public void setPolygonQuality(double polygonQuality) {
        this.polygonQuality = polygonQuality;
    }

    public int getRedFilter() {
        return redFilter;
    }

    public void setRedFilter(int redFilter) {
        this.redFilter = redFilter;
    }

    public VisionParameters permutate() {
        Random r = new Random();
        switch (r.nextInt() % 6) {
            case 0:
                return new VisionParameters(max(COLOR_FILTER_MIN, min(COLOR_FILTER_MAX,
                        redFilter + COLOR_FILTER_CHANGE * ((r.nextInt() % 2 == 0) ? (1) : (-1)))),
                        greenFilter, blueFilter, erodeIterations, dilateIterations, polygonQuality);
            case 1:
                return new VisionParameters(redFilter, max(COLOR_FILTER_MIN,
                        min(COLOR_FILTER_MAX, greenFilter + COLOR_FILTER_CHANGE
                        * ((r.nextInt() % 2 == 0) ? (1) : (-1)))), blueFilter, erodeIterations,
                        dilateIterations, polygonQuality);
            case 2:
                return new VisionParameters(redFilter, greenFilter, max(COLOR_FILTER_MIN,
                        min(COLOR_FILTER_MAX, blueFilter + COLOR_FILTER_CHANGE
                        * ((r.nextInt() % 2 == 0) ? (1) : (-1)))), erodeIterations, dilateIterations,
                        polygonQuality);
            case 3:
                return new VisionParameters(redFilter, greenFilter, blueFilter,
                        max(ERODE_ITERATIONS_MIN,
                        min(ERODE_ITERATIONS_MAX, erodeIterations
                        + ERODE_ITERATIONS_CHANGE * ((r.nextInt() % 2 == 0) ? (1) : (-1)))),
                        dilateIterations, polygonQuality);
            case 4:
                return new VisionParameters(redFilter, greenFilter, blueFilter, erodeIterations,
                        max(DILATE_ITERATIONS_MIN, min(DILATE_ITERATIONS_MAX,
                        dilateIterations + DILATE_ITERATIONS_CHANGE
                        * ((r.nextInt() % 2 == 0) ? (1) : (-1)))), polygonQuality);
            case 5:
                return new VisionParameters(redFilter, greenFilter, blueFilter, erodeIterations,
                        dilateIterations,
                        max(POLYGON_QUALITY_MIN, min(POLYGON_QUALITY_MAX,
                        polygonQuality + POLYGON_QUALITY_CHANGE
                        * ((r.nextInt() % 2 == 0) ? (1) : (-1)))));
            default:
                return null; 
        }
    }

    private int max(int a, int b) {
        return (a > b) ? (a) : (b);
    }

    private double max(double a, double b) {
        return (a > b) ? (a) : (b);
    }

    private int min(int a, int b) {
        return (a < b) ? (a) : (b);
    }

    private double min(double a, double b) {
        return (a < b) ? (a) : (b);
    }
}
