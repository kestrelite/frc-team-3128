package tracking;

import edu.wpi.first.wpijavacv.WPIPolygon;
import java.util.ArrayList;
import java.util.List;

public class TrackingManager {

    private List<FrameResults> frames;
    private TargetTracker tracker;
    private List<Target> knownTargets;

    public TrackingManager() {
        frames = new ArrayList<FrameResults>();
        knownTargets = new ArrayList<Target>();

        tracker = new DistanceTracker();
    }

    public void processFrame(List<WPIPolygon> polygons) {
        FrameResults results = new FrameResults();
        results.setPolygons(polygons);
        frames.add(results);

        tracker.update(knownTargets, frames);
    }

    public List<Target> getKnownTargets() {
        return knownTargets;
    }
}
