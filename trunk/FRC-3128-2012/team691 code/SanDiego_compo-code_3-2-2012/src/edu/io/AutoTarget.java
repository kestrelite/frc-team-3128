
package edu.io;

import org.team691.util.Time;

/*
 * NOTE THIS IS AN UNFINISHED WRAPPER CLASS
 * GERERD, THE METHODS AND WHAT I NEED THEM TO DO ARE LISTED BELOW
 * PLEASE FILL THEM IN WITH THE CORRECT REFERENCES TO THE KINECT NETWORKING CLASS
 */
public class AutoTarget
{
    public static final double PIXEL_COUNT_TO_MOTOR_POWER = 0.018;

    public static final int NETWORK_UPDATES_PER_SECOND    = 15; // use 20 or 30 for final
    public static final String URL                        = "socket://10.6.91.42:20000";
    protected volatile int targetHoop                     = 1;

    // on/off - ready to fire - target # - can see
    // 1 bit  -  1 bit        -  2 bits  -  4 bits
    // 1         0               11         1011
    // 10111011
    protected volatile double turretAngle  = 0;
    protected volatile int turretPower     = 0;
    protected volatile boolean readyToFire = false;
    protected volatile boolean newData     = false;
    protected volatile boolean lockedOn    = false;
    protected KPacket packet;
    protected TargetingComputerConnection tcc;

    public AutoTarget()
    {
        // init kinect networking class. Runs in a seperate thread
        if ( tcc != null )
            tcc.kill();

        tcc = new TargetingComputerConnection( URL, NETWORK_UPDATES_PER_SECOND, this );

        tcc.start();

        packet = tcc.getPacket();
    }

    public void checkForBlockedConnection()
    {
        if ( tcc.hasBlocked() )
        {
            log( "Second thread has blocked! killing it" );

            // end the thread, or at least downsize it.
            tcc.kill();
            tcc.setPriority( Thread.MIN_PRIORITY );
            tcc.interrupt();

            log( "replacing with new thread" );

            tcc = new TargetingComputerConnection( URL, NETWORK_UPDATES_PER_SECOND,
                                                   this );

            tcc.start();

            packet = tcc.getPacket();

            log( "done" );
        }
    }

    /**
     * return an int organized as a single byte that represents the following info:
     * (1 bit) is the camera on
     * (1 bit) is the PID of the turret roller ready to fire
     * (2 bits) what hoop the turret/kinect is currently targeting
     * (4 bits) which hoops the kinect can see
     *
     * a value of -1 means that we are not connected to Deep Thought.
     */
    public KPacket getPacket()
    {
        return packet;
    }

    /**
     * returns if the kinect has found a target, but might not be ready
     * to fire.
     */

    // 10111001 & 00001111 = 00001001
    public boolean isLockedOn()
    {
        return lockedOn;
    }

    /**
     * Returns if Deep Thought has finalized a firing solution, and thinks we
     * will hit if we fire now.
     */

    // 10111001 & 01000000 = 01000000
    public boolean isReadyToFire()
    {
        return  readyToFire;
    }

    /**
     * return an int representing the angle that the Kinect auto targeting
     * deems as the best angle to shoot at
     * (to be used for turret swiveling)
     */
    public double getTurretAngle()
    {
        return turretAngle;
    }

    /**
     * return an int representing the turret roller speed that the Kinect
     * auto targeting deems as the best power to shoot at
     * (to be used for specific distances)
     */
    public int getTurretPower()
    {
        return turretPower;
    }

    // public PID getPID(){ return pid; }
    // protected PID pid = new PID(0.0001, 1.0, 0.01, 3);

    public static final int TOP    = 1;
    public static final int LEFT   = 2;
    public static final int BOTTOM = 3;
    public static final int RIGHT  = 4;
    /**
     * Tells the targeting computer what hoop to aim at.
     * 1 = top
     * 2 = left
     * 3 = bottom
     * 4 = right
     * -1 = DISABLE (example: you're looking at the enemy's hoops)
     */
    public void cycleTarget(int hoopNum)
    {
        tcc.setTargetHoopNumber( hoopNum );

        targetHoop = hoopNum;
    }

    public boolean newData()
    {
        return newData;
    }

    public void clearNewData()
    {
        newData = false;
    }

    // //////EVERYTHING BELOW THIS LINE IS FOR GERARD ONLY///////////////////////
    // //////////////////////DON'T TOUCH >:(/////////////////////////////////////

    /**
     * Updates the value of the target byte. The method is public because I need
     * to call it in TargetingComputerConnection, but it needs to be protected
     * to protect it from user error.
     * @param check If this is not the exact same connection as the one stored
     * locally, then the method does nothing and returns.
     */
    public synchronized boolean updateLockedOn(boolean value,
                                               TargetingComputerConnection check)
    {
        // protects the public method
        if ( check != tcc )
            return false;

        // else do everything below
        lockedOn = value;
        newData  = true;
        return true;
    }

    public synchronized boolean updateTurretAngle(int value,
                                                  TargetingComputerConnection check)
    {
        // protects the public method
        if ( check != tcc )
            return false;

        // else do everything below
        turretAngle = value * PIXEL_COUNT_TO_MOTOR_POWER;
        newData     = true;
        return true;
    }

    public synchronized boolean updateTurretPower(int value,
                                                  TargetingComputerConnection check)
    {
        // protects the public method
        if ( check != tcc )
            return false;

        // else do everything below
        turretPower = value;
        newData     = true;
        return true;
    }

    public synchronized boolean updateNewData(boolean value,
                                              TargetingComputerConnection check)
    {
        // protects the public method
        if ( check != tcc )
            return false;

        newData = value;
        return true;
    }

    public synchronized boolean updateReadyToFire(boolean value,
                                                  TargetingComputerConnection check)
    {
        // protects the public method
        if ( check != tcc )
            return false;

        newData     = true;
        readyToFire = value;
        return true;
    }

    /*
     * public synchronized boolean updatePID(int oneOverScale, int KP, int KI, int KD,
     *       TargetingComputerConnection check)
     * {
     *   //protects the public method
     *   if( check != tcc )
     *       return false;
     *
     *   log("setting new PID object in autotarget");
     *   newData = true;
     *   pid = new PID( 1.0/oneOverScale, KP, KI, KD );
     *   return true;
     * }
     */

    public static void log(String s)
    {
        System.out.println( Time.string() + ":.........." + s );
    }
}


//FIRST FRC team 691 2012 competition code
