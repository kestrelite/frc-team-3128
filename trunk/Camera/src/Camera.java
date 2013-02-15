
import edu.wpi.first.smartdashboard.camera.WPICameraExtension;
import edu.wpi.first.smartdashboard.properties.DoubleProperty;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.wpijavacv.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.ArrayList;

public class Camera extends WPICameraExtension {

    public static final String NAME = "Camera Target Tracker";
    public final IntegerProperty threshold = new IntegerProperty(this, "Threshold", 180);
    public final DoubleProperty contourPolygonApproximationPct = new DoubleProperty(this, "Polygon Approximation %", 45);
    NetworkTable table = NetworkTable.getTable("camera");
    WPIColor targetColor = new WPIColor(0, 255, 0);
    WPIColor contourColor = new WPIColor(17, 133, 133);

    @Override
    public WPIImage processImage(WPIColorImage rawImage) {
        WPIBinaryImage blue = rawImage.getBlueChannel().getThreshold(threshold.getValue());
        WPIBinaryImage green = rawImage.getGreenChannel().getThreshold(threshold.getValue());
        WPIBinaryImage red = rawImage.getRedChannel().getThreshold(threshold.getValue());
        // Does the thresholding;

        WPIBinaryImage colorsCombined = blue.getAnd(red).getAnd(green);
        //Mixes the paint;

        colorsCombined.erode(2);
        colorsCombined.dilate(6);
        //Gets rid of small stuff and fills holes;

        WPIContour[] contours = colorsCombined.findContours();
        rawImage.drawContours(contours, contourColor, 3);
        ArrayList<WPIPolygon> polygons = new ArrayList<WPIPolygon>();

        for (WPIContour c : contours) {
            double ratio = ((double) c.getHeight()) / ((double) c.getWidth());
            //if (ratio < 1.5 && ratio > .75)
            if (1 == 1) {
                polygons.add(c.approxPolygon(contourPolygonApproximationPct.getValue()));
                //Makes polygons around edges;
            }
        }
        ArrayList<WPIPolygon> possiblePolygons = new ArrayList<WPIPolygon>();
        for (WPIPolygon p : polygons) {
            if (p.isConvex() && p.getNumVertices() == 4) {
                possiblePolygons.add(p);
            } else {
                rawImage.drawPolygon(p, WPIColor.MAGENTA, 1);
            }
        }
        WPIPolygon square = null;

        int squareArea = 0;
        double centerX = 0;
        double heightRatio = 0;

        for (WPIPolygon p : possiblePolygons) {
            rawImage.drawPolygon(p, WPIColor.BLUE, 5);
            for (WPIPolygon q : possiblePolygons) {
                if (p == q) {
                    continue;
                }
                int pCenterX = (p.getX() + (p.getWidth() / 2));
                int qCenterX = (q.getX() + (q.getWidth() / 2));
                int pCenterY = (p.getY() + (p.getHeight() / 2));
                int qCenterY = (q.getY() + (q.getHeight() / 2));
//                    rawImage.drawPoint(new WPIPoint(pCenterX, pCenterY), targetColor, 5);
//                    rawImage.drawPoint(new WPIPoint(qCenterX, qCenterY), targetColor, 5);
                if (Math.abs(pCenterX - qCenterX) < 20 && Math.abs(pCenterY - qCenterY) < 20) {
                    int pArea = Math.abs(p.getArea());
                    int qArea = Math.abs(q.getArea());
                    if (square != null) {
                        if (square.getY() < p.getY()) {
                            continue;
                        }
                        // if we already have a square, and it is lower, doesn't add anything
                    }
                    if (pArea > qArea) {
                        square = p;
                        squareArea = pArea;
                        centerX = (double) pCenterX / rawImage.getWidth() - .5;
                    } else {
                        square = q;
                        squareArea = qArea;
                        centerX = (double) qCenterX / rawImage.getWidth() - .5;
                    }
                    WPIPoint[] v = square.getPoints();
                    int x1 = Math.abs(v[1].getX() - v[0].getX());
                    int y1 = Math.abs(v[1].getY() - v[0].getY());
                    int y2 = Math.abs(v[2].getY() - v[1].getY());
                    int y3 = Math.abs(v[3].getY() - v[2].getY());
                    int y4 = Math.abs(v[0].getY() - v[3].getY());
                    if (y1 > x1) { // first segment isvertical
                        heightRatio = (double) y1 / y3;
                    } else {
                        heightRatio = (double) y2 / y4;
                    }
                    break;
                }
            }
        }

        if (square != null) {
            int sCenterX = (square.getX() + (square.getWidth() / 2));
            int sCenterY = (square.getY() + (square.getHeight() / 2));
            rawImage.drawPoint(new WPIPoint(sCenterX, sCenterY), targetColor, 5);
        }

        synchronized (table) {
            //table.beginTransaction();

            if (square != null) {
                double distance = (1564.4 / Math.sqrt(squareArea) - 0.5719) * 12;
                table.putBoolean("found", true);
                table.putDouble("area", squareArea);
                table.putDouble("distance", distance);
                table.putDouble("xoffset", -centerX);
                table.putDouble("heightratio", heightRatio);
            } else {
                table.putBoolean("found", false);
            }
            //table.endTransaction();
        }
        return rawImage;
    }
}