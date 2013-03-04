package tracking;

import edu.wpi.first.wpijavacv.WPIPolygon;
import java.util.ArrayList;
import java.util.List;

public class FrameResults {

    private List<WPIPolygon> polygons;

    public FrameResults() {
        polygons = new ArrayList<WPIPolygon>();
    }

    public FrameResults(List<WPIPolygon> polygons) {
        this.polygons = polygons;
    }

    public List<WPIPolygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<WPIPolygon> polygons) {
        this.polygons = polygons;
    }
}
