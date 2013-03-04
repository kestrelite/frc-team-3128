import edu.wpi.first.smartdashboard.properties.DoubleProperty;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.wpijavacv.WPIBinaryImage;
import edu.wpi.first.wpijavacv.WPIColor;
import edu.wpi.first.wpijavacv.WPIColorImage;
import edu.wpi.first.wpijavacv.WPIContour;
import edu.wpi.first.wpijavacv.WPIImage;
import edu.wpi.first.wpijavacv.WPIPoint;
import edu.wpi.first.wpijavacv.WPIPolygon;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import tracking.Target;
import tracking.TrackingManager;

public class CameraAlphaTargeting extends WPICameraExtension {

    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 480;
    public static final int AIM_THRESHOLD = 25;
    public static final double TARGET_WIDTH = 2;
    public static final double TARGET_HEIGHT = 1.5;
    private static final int HORIZON_LINE_Y = IMAGE_HEIGHT - 30;
    private NetworkTable table = NetworkTable.getTable("camera");
    private NetworkTable shooterRotate = NetworkTable.getTable("ShooterRotateCommand");
    private NetworkTable shooterAim = NetworkTable.getTable("ShooterAimTable");
    private DoubleProperty polygonQuality;
    private IntegerProperty erodeAmount;
    private IntegerProperty dilateAmount;
    private IntegerProperty redMaxProperty;
    private IntegerProperty greenMinProperty;
    private IntegerProperty greenMaxProperty;
    private IntegerProperty blueMinProperty;
    private DoubleProperty cameraViewAngle;
    private DoubleProperty velocityProperty;
    private DoubleProperty thetaProperty;
    private List<WPIPolygon> foundPolygons;
    private Point mouseLocation;
    private Target selectedTarget;
    private TrackingManager tracker;
    private double displayedVelocity;

    public CameraAlphaTargeting() {
        polygonQuality = new DoubleProperty(this, "Polygon Quality", 45);
        erodeAmount = new IntegerProperty(this, "Erode Amount", 0);
        dilateAmount = new IntegerProperty(this, "Dilate Amount", 6);
        cameraViewAngle = new DoubleProperty(this, "View Angle", 54);
        redMaxProperty = new IntegerProperty(this, "Red Maximum", 157);
        greenMinProperty = new IntegerProperty(this, "Green Minimum", 28);
        greenMaxProperty = new IntegerProperty(this, "Green Maximum", 166);
        blueMinProperty = new IntegerProperty(this, "Blue Minimum", 219);
        velocityProperty = new DoubleProperty(this, "Velocity", 0);
        thetaProperty = new DoubleProperty(this, "Theta", 45);

        foundPolygons = new ArrayList<WPIPolygon>();
        tracker = new TrackingManager();

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    @Override
    public WPIImage processImage(WPIColorImage rawImage) {

        shooterAim.putDouble("velocity", velocityProperty.getValue());
        shooterAim.putDouble("theta", thetaProperty.getValue());

        int redMax = redMaxProperty.getValue();
        int greenMin = greenMinProperty.getValue();
        int greenMax = greenMaxProperty.getValue();
        int blueMin = blueMinProperty.getValue();

        WPIBinaryImage blueChan = rawImage.getBlueChannel().getThreshold(blueMin);
        WPIBinaryImage redChan = rawImage.getRedChannel().getThresholdInverted(redMax);
        WPIBinaryImage greenChan1 = rawImage.getGreenChannel().getThreshold(greenMin);
        WPIBinaryImage greenChan2 = rawImage.getGreenChannel().getThresholdInverted(greenMax);

        blueChan.and(redChan);
        blueChan.and(greenChan1);
        blueChan.and(greenChan2);

        blueChan.erode(erodeAmount.getValue());
        blueChan.dilate(dilateAmount.getValue());

        WPIContour[] contours = blueChan.findContours();
        List<WPIPolygon> squares = new ArrayList<WPIPolygon>();
        List<WPIPolygon> badPolygons = new ArrayList<WPIPolygon>();

        for (WPIContour c : contours) {
            WPIPolygon p = c.approxPolygon(polygonQuality.getValue());
            if (p.isConvex() && p.getNumVertices() == 4) {
                squares.add(p);
            } else {
                badPolygons.add(p);
            }
        }

        List<WPIPolygon> polygonsFiltered = filterSimilar(squares);

        for (WPIPolygon p : badPolygons) {
            rawImage.drawPolygon(p, WPIColor.RED, 3);
        }

        for (WPIPolygon p : polygonsFiltered) {
            rawImage.drawPolygon(p, WPIColor.CYAN, 1);

            int cx = p.getX() + (p.getWidth() / 2);
            int cy = p.getY() + (p.getHeight() / 2);
            rawImage.drawPoint(new WPIPoint(cx, cy), WPIColor.YELLOW, 2);
        }

        foundPolygons.clear();
        foundPolygons.addAll(polygonsFiltered);

        tracker.processFrame(foundPolygons);

        if (selectedTarget != null) {

            if (!tracker.getKnownTargets().contains(selectedTarget)) {

                selectedTarget = null;
                stopRotation();
            } else {

                WPIPolygon target = selectedTarget.getPolygon();
                int cxTarget = target.getX() + (target.getWidth() / 2); // 500
                int cxImage = IMAGE_WIDTH / 2; // 320

                int dx = cxTarget - cxImage;

                if (Math.abs(dx) < AIM_THRESHOLD) {
                    // ready to shoot, stop rotating
                    stopRotation();

                    double x = getDistanceHorizontal(selectedTarget.getPolygon());
                    double y = getDistanceVertical(selectedTarget.getPolygon());

                    /*double theta = 52.36 - 0.5991 * x + 36.73 * y
                     + 0.005431 * Math.pow(x, 2) - 0.5144 * x * y
                     - 13.63 * Math.pow(y, 2) + 0.001624 * Math.pow(x, 2) * y
                     + 0.2275 * x * Math.pow(y, 2) + 2.35 * Math.pow(y, 3)
                     - 0.0008125 * Math.pow(x * y, 2)
                     - 0.03133 * x * Math.pow(y, 3) - 0.1885 * Math.pow(y, 4)
                     + 0.0000695 * Math.pow(x * y, 2) * y
                     + 0.001408 * x * Math.pow(y, 4)
                     + 0.005756 * Math.pow(y, 5);*/

                    double velocity = 13.77 + 0.6473 * x + 12.26 * y
                            + 0.001124 * Math.pow(x, 2) - 0.1462 * x * y
                            + 0.02471 * Math.pow(y, 2)
                            + 0.0005207 * Math.pow(x, 2) * y
                            + 0.008972 * x * Math.pow(y, 2)
                            + 0.003813 * Math.pow(y, 3);

                    //shooterAim.putDouble("velocity", velocity);
                    //shooterAim.putDouble("theta", theta);

                    displayedVelocity = velocity;
                } else {
                    rotate(dx);
                }
            }
        }

        return rawImage;
    }

    private List<WPIPolygon> filterSimilar(List<WPIPolygon> polys) {
        List<WPIPolygon> ret = new ArrayList<WPIPolygon>();
        ret.addAll(polys);

        List<WPIPolygon> removalQueue = new ArrayList<WPIPolygon>();

        for (WPIPolygon p : polys) {
            List<WPIPolygon> similar = getSimilar(p, polys);

            WPIPolygon largest = getLargest(similar);

            similar.remove(largest);

            removalQueue.addAll(similar);
        }

        for (WPIPolygon p : removalQueue) {
            removeAll(p, ret);
        }

        return ret;
    }

    private List<WPIPolygon> getSimilar(WPIPolygon poly, List<WPIPolygon> pool) {
        List<WPIPolygon> ret = new ArrayList<WPIPolygon>();

        int xCenter = poly.getX() + (poly.getWidth() / 2);
        int yCenter = poly.getY() + (poly.getHeight() / 2);

        for (WPIPolygon p : pool) {
            int pcx = p.getX() + (p.getWidth() / 2);
            int pcy = p.getY() + (p.getHeight() / 2);

            int dx = Math.abs(pcx - xCenter);
            int dy = Math.abs(pcy - yCenter);

            int distSquared = (dx * dx) + (dy * dy);

            if (distSquared < 20 * 20) {
                ret.add(p);
            }
        }

        return ret;
    }

    public WPIPolygon getLargest(List<WPIPolygon> pool) {
        WPIPolygon ret = null;

        for (WPIPolygon p : pool) {
            if (ret == null || p.getArea() > ret.getArea()) {
                ret = p;
            }
        }

        return ret;
    }

    public <T> void removeAll(T obj, List<T> pool) {
        while (pool.contains(obj)) {
            pool.remove(obj);
        }
    }

    public double getDistanceHorizontal(WPIPolygon p) {
        return Math.sqrt(Math.pow(getDistanceCorners(p), 2)
                - Math.pow(getDistanceVertical(p), 2));
    }

    public double getDistanceVertical(WPIPolygon p) {
        int heightAbove = p.getY() + p.getHeight() / 2 - HORIZON_LINE_Y;
        double trueHeight = heightAbove * TARGET_HEIGHT / p.getHeight();

        return -trueHeight;
    }

    public double getDistanceCorners(WPIPolygon p) {
        double imageWidth = IMAGE_WIDTH;

        List<WPIPoint> points = Arrays.asList(p.getPoints());

        List<WPIPoint> right = new ArrayList<WPIPoint>();
        right.addAll(points);
        right.remove(findMinimum(right));
        right.remove(findMinimum(right));

        List<WPIPoint> left = new ArrayList<WPIPoint>();
        left.addAll(points);
        left.removeAll(right);

        double leftX = averageX(left);
        double leftY = averageY(left);
        double rightX = averageX(right);
        double rightY = averageY(right);

        double dx = Math.abs(leftX - rightX);
        double dy = Math.abs(leftY - rightY);

        double width = Math.sqrt((dx * dx) + (dy * dy)) - (dilateAmount.getValue() * 0);

        double fovWidth = ((Math.sqrt(6.25) * imageWidth) / width) / 2;
        double theta = cameraViewAngle.getValue() / 2;

        double distance = fovWidth / Math.tan(Math.toRadians(theta));
        return distance;
    }

    public double getDistance(WPIPolygon p) {
        double pixelHeight = p.getHeight() - dilateAmount.getValue() * 3 / 2;
        double fovHeight = (TARGET_HEIGHT * IMAGE_HEIGHT) / pixelHeight;

        double angle = cameraViewAngle.getValue() / 2;
        double distance = ((fovHeight / 2) / (Math.tan(Math.toRadians(angle))));

        System.out.println("PixelHeight = " + pixelHeight);
        System.out.println("foveHeight = " + fovHeight);
        System.out.println("Distance = " + distance);
        System.out.println();

        return distance;
    }

    private WPIPoint findMinimum(List<WPIPoint> points) {
        WPIPoint ret = null;

        for (WPIPoint p : points) {
            if (ret == null || p.getX() < ret.getX()) {
                ret = p;
            }
        }

        return ret;
    }

    public double averageX(List<WPIPoint> points) {
        double sum = 0;

        for (WPIPoint p : points) {
            sum += p.getX();
        }

        return sum / points.size();
    }

    public double averageY(List<WPIPoint> points) {
        double sum = 0;

        for (WPIPoint p : points) {
            sum += p.getY();
        }

        return sum / points.size();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getDrawnImage() != null) {
            BufferedImage image = getDrawnImage();
            Graphics2D g2d = image.createGraphics();
            int horizY = image.getHeight() - 30;

            for (WPIPolygon p : foundPolygons) {
                int cx = p.getX() + (p.getWidth() / 2);
                int cy = p.getY() + (p.getHeight() / 2);
                int heightAboveSeen = -p.getY() + horizY;
                double heightAboveTrue =
                        ((double) heightAboveSeen) * TARGET_HEIGHT / ((double) p.getHeight());
                //Now do point calculations to find the top and bottom corners
                WPIPoint[] points = p.getPoints();
                double upperWidth;
                double lowerWidth;
                double distance;
                WPIPoint p1 = null;
                WPIPoint p2 = null;
                for (int i = 0; i < 4; i++) {
                    if (p1 == null || p1.getY() < points[i].getY()) {
                        if (p2 == null || p1.getY() > p2.getY()) {
                            p2 = p1;
                        }
                        p1 = points[i];
                    } else if (p2 == null || p2.getY() < points[i].getY()) {
                        p2 = points[i];
                    }
                }

                upperWidth = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
                        + Math.pow(p1.getY() - p2.getY(), 2));

                System.out.println("UpperWidth = " + upperWidth);

                p1 = (p2 = null);

                for (int i = 0; i < 4; i++) {
                    if (p1 == null || p1.getY() > points[i].getY()) {
                        if (p2 == null || p2.getY() > p1.getY()) {
                            p2 = p1;
                        }
                        p1 = points[i];
                    } else if (p2 == null || p2.getY() > points[i].getY()) {
                        p2 = points[i];
                    }
                }

                lowerWidth = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2)
                        + Math.pow(p1.getY() - p2.getY(), 2));

                System.out.println("LowerWidth = " + lowerWidth);
                System.out.println("HeightActual = " + heightAboveTrue);
                System.out.println();

                distance = heightAboveSeen / ((upperWidth / lowerWidth) - 1) / (heightAboveTrue + TARGET_HEIGHT);
                distance = Math.sqrt(distance);

                g2d.setColor(Color.WHITE);
                g2d.drawString(String.format("D: %.3f ft", getDistanceHorizontal(p)),
                        cx + 5, cy);
                g2d.drawString(String.format("H: %.3f", getDistanceVertical(p)),
                        cx + 5, cy - 15);

                if (mouseLocation != null) {
                    Polygon awtPolygon = convert(p);
                    if (awtPolygon.contains(mouseLocation)) {
                        g2d.setColor(Color.GREEN);
                        GraphicsUtil.alpha(g2d, 0.50f);
                        g2d.fill(awtPolygon);
                        GraphicsUtil.alpha(g2d, 1);

                        g2d.setColor(Color.YELLOW);
                        g2d.draw(awtPolygon);

                        g2d.setColor(Color.BLACK);
                    }
                }
            }

            g2d.setColor(Color.WHITE);

            g2d.drawString("Targets Known: " + tracker.getKnownTargets().size(), 15, 15);
            if (shooterRotate.containsKey("running")) {
                boolean running = shooterRotate.getBoolean("running");
                g2d.drawString("Aiming:        " + running, 15, 30);

                if (shooterRotate.containsKey("direction") && running) {
                    g2d.drawString("Aim Direction: " + shooterRotate.getInt("direction"), 15, 45);
                }

                g2d.drawString("Velocity: " + String.format("%.3f", displayedVelocity), 15, 60);
            }

            for (Target t : tracker.getKnownTargets()) {
                WPIPolygon poly = t.getPolygon();

                int cx = poly.getX() + (poly.getWidth() / 2);
                int cy = poly.getY() + (poly.getHeight() / 2);

                g2d.drawString("T: " + t.getNumber(), cx + 5, cy + 12);
            }

            if (selectedTarget != null) {
                Polygon p = convert(selectedTarget.getPolygon());

                GraphicsUtil.alpha(g2d, 0.5f);
                g2d.setColor(Color.RED);
                g2d.fill(p);
                GraphicsUtil.alpha(g2d, 1);

                g2d.setColor(Color.ORANGE);
                g2d.draw(p);

            }

            g2d.drawLine(0, horizY, image.getWidth(), horizY);

            g2d.dispose();
        }

        super.paintComponent(g);
    }

    public Polygon convert(WPIPolygon poly) {
        Polygon ret = new Polygon();

        for (WPIPoint p : poly.getPoints()) {
            ret.addPoint(p.getX(), p.getY());
        }

        return ret;
    }
    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            mouseLocation = e.getPoint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseLocation = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            for (Target t : tracker.getKnownTargets()) {
                Polygon p = convert(t.getPolygon());

                if (p.contains(e.getPoint())) {
                    selectedTarget = t;

                    JOptionPane.showMessageDialog(
                            CameraAlphaTargeting.this, "Polygon selected!");
                }
            }
        }
    };

    private void rotate(int dx) {
        if (dx > 0) {
            shooterRotate.putInt("direction", 1);
        } else if (dx < 0) {
            shooterRotate.putInt("direction", -1);
        } else {
            shooterRotate.putInt("direction", 0);
        }

        shooterRotate.putBoolean("running", true);
    }

    private void stopRotation() {
        shooterRotate.putBoolean("running", false);
        shooterRotate.putInt("direction", 0);
    }
}
