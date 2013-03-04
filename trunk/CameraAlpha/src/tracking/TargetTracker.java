package tracking;

import java.util.List;

public interface TargetTracker {

    public void update(List<Target> knownTargets, List<FrameResults> frames);
}
