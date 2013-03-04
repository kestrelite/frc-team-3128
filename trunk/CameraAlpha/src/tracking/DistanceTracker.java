package tracking;

import edu.wpi.first.wpijavacv.WPIPolygon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DistanceTracker implements TargetTracker {

    public static final double MAX_DISTANCE = 2500; // 50^2
    public static final int MAX_MISSING = 15;

    public void update(List<Target> knownTargets, List<FrameResults> frames) {
        List<Target> removalQueue = new ArrayList<Target>();
        removalQueue.addAll(knownTargets);

        FrameResults last = frames.get(frames.size() - 1);
        List<WPIPolygon> polygons = last.getPolygons();

        for (WPIPolygon p : polygons) {
            Target t = findClosest(removalQueue, p);

            if (t == null) {
                // new / previously unknown target found
                t = new Target(Target.TARGET_COUNT, p);
                Target.TARGET_COUNT++;
                knownTargets.add(t);
                System.out.println("Found new target #" + t.getNumber());
            } else {
                // update the old target
                t.setPolygon(p);
                t.setFramesMissing(0);
                removalQueue.remove(t);
            }
        }

        for (Target t : removalQueue) {
            t.missing();

            if (t.getFramesMissing() > MAX_MISSING) {
                knownTargets.remove(t);
                System.out.println("Removed target #" + t.getNumber());
            }
        }
    }


    public Target findClosest(List<Target> targets, WPIPolygon poly) {
        Point pCenter = getCenter(poly);

        Target ret = null;
        double retDistance = 0;

        for (Target t : targets) {
            Point tCenter = getCenter(t.getPolygon());

            if (ret == null) {
                ret = t;
                retDistance = getCenter(ret.getPolygon()).distanceSq(pCenter);
            } else {
                double newDistance = getCenter(t.getPolygon()).distanceSq(pCenter);

                if (newDistance < retDistance) {
                    ret = t;
                    retDistance = newDistance;
                }
            }
        }

        if (ret != null
                && pCenter.distanceSq(getCenter(ret.getPolygon())) > MAX_DISTANCE) {
            return null;
        }

        return ret;
    }


    private Point getCenter(WPIPolygon p) {
        int cx = p.getX() + (p.getWidth() / 2);
        int cy = p.getY() + (p.getHeight() / 2);

        return new Point(cx, cy);
    }
}
