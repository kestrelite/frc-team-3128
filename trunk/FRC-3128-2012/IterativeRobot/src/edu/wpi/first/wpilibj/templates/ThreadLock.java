package edu.wpi.first.wpilibj.templates;

public class ThreadLock {

    private Object lockingObject = null;

    public synchronized boolean getLockIfAvailable(Object o) {
        if (lockingObject == null) {
            lockingObject = o;
            return true;
        }
        return false;
    }

    public synchronized void waitForLock(Object o) {
        while (!(o == lockingObject)) {
            if (lockingObject == null) {
                lockingObject = o;
            }
            if (lockingObject != null) {
                System.out.println("WARN: Object waiting for lock, yielding...");
            }
            Thread.yield();
        }
    }

    public void releaseLock(Object o) throws Exception {
        if (lockingObject != o && lockingObject != null) {
            throw new Exception("Lock cannot be released by non-locking object");
        }
        lockingObject = null;
    }

    public boolean isLocked() {
        if (lockingObject == null) {
            return true;
        }
        return false;
    }

    public boolean isLockedBy(Object o) {
        try {
            if (lockingObject.equals(o)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
