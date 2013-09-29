package frc3128.EventManager;

import java.util.Vector;

/**

 @author Noah Sutton-Smolin
 */
public class ResourceLock {
    private static Vector lockedObjects = new Vector();
    
    private boolean existsIn(Object o) {
        for(int i = 0; i < lockedObjects.size(); i++)
            if(lockedObjects.elementAt(i).equals(o)) return true;
        return false;
    }
    
    public boolean lock(Object o) {
        if(existsIn(o)) return false;
        lockedObjects.addElement(o);
        return true;
    }
    
    public boolean isLocked(Object o) {return existsIn(o);}
    
    public boolean unlock(Object o) {
        if(!existsIn(o)) return false;
        lockedObjects.removeElement(o);
        return true;
    }
    
    private ResourceLock() {}
}
