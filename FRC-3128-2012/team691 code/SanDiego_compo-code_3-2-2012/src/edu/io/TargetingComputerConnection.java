
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import org.team691.util.Time;

/**
 *
 * @author Gerard
 */
public class TargetingComputerConnection extends Thread
{
    public static final double BLOCK_TIMEOUT      = 2;
    public static final String DEFAULT_TARGET_URL = "socket://10.6.91.42:20000";
    public static final int NEW_DATA              = 0;
    public static final int NOT_CONNECTED         = -1;
    public static final int NO_NEW_DATA           = 1;
    protected static long intervalMills           = 100; // 10 times per second
    protected byte[] buffer                       = new byte[256];
    protected DataInputStream in                  = null;
    protected DataOutputStream out                = null;
    protected SocketConnection socket             = null;
    protected volatile int targetHoop             = 1;
    protected String targetUrl                    = DEFAULT_TARGET_URL;
    protected volatile double readBlockTime       = Time.time() + BLOCK_TIMEOUT;
    protected KPacket packet                      = new KPacket();
    protected volatile boolean isReading          = false;
    protected boolean continues                   = true;
    protected AutoTarget wraper;
    protected int tryConnect                      = 0;
    
    public TargetingComputerConnection(double ioChecksPerSecond, AutoTarget wraper)
    {
        this.wraper                               = wraper;
        TargetingComputerConnection.intervalMills = (long)( ( 1000.0
                                                              / ioChecksPerSecond ) );
    }

    public TargetingComputerConnection(String url, double ioChecksPerSecond,
                                       AutoTarget wraper)
    {
        this.targetUrl                            = url;
        this.wraper                               = wraper;
        TargetingComputerConnection.intervalMills = (long)( ( 1000.0
                                                              / ioChecksPerSecond ) );
    }

    /**
     * The main method of the new thread.
     */
    public void run()
    {
        while( continues )
        {
            try
            {
                readLock();

                switch(getConnectionStatus())
                {
                    case NOT_CONNECTED :
                        if( (++tryConnect % 10) == 0)
                            attemptConnection();
                        break;

                    case NO_NEW_DATA :
                        requestNewData();
                        break;

                    case NEW_DATA :

                        // log( "avalible: " + in.available() );
                        updateData();
                        break;
                }

                readUnlock();

                try
                {
                    synchronized (this)
                    {
                        this.wait( intervalMills );
                    }
                }
                catch (InterruptedException e)
                {
                    log( "interrupted. ignoring and running next cycle." );
                }
            }
            catch (Exception e)
            {
                log( "Exception in TCC thread. Ignoring.\n\t" + e.toString() );
            }
        }
    }

    protected void readLock()
    {
        isReading     = true;
        readBlockTime = Time.time() + BLOCK_TIMEOUT;
    }

    protected void readUnlock()
    {
        isReading     = false;
        readBlockTime = Time.time() + 10000000;
    }

    public boolean hasBlocked()
    {
        return ( isReading && ( Time.time() > readBlockTime ) );
    }

    protected void updateData()
    {
        if ( readPacket() )
        {
            echoPacket();
            wraper.updateNewData( true, this );

            /*
             * if(packet.offset_from_top != 0)
             *   wraper.updatePID(packet.offset_from_top, packet.offset_from_left,
             *           packet.offset_from_bottom, packet.offset_from_right, this);
             */
        }
        else
            requestNewData();

        switch(targetHoop)
        {
            case AutoTarget.TOP : // top
                wraper.updateTurretPower( packet.speed_top, this );
                wraper.updateTurretAngle( packet.offset_from_top, this );
                wraper.updateReadyToFire( Math.abs( packet.offset_from_top ) < 11, this );
                wraper.updateLockedOn( ( packet.target_number & 0x0F ) != 0, this );
                break;
            case AutoTarget.LEFT : // left
                wraper.updateTurretPower( packet.speed_left, this );
                wraper.updateTurretAngle( packet.offset_from_left, this );
                wraper.updateReadyToFire( Math.abs( packet.offset_from_left ) < 11,
                                          this );
                wraper.updateLockedOn( ( packet.target_number & 0x0F ) != 0, this );
                break;
            case AutoTarget.BOTTOM : // bottom
                wraper.updateTurretPower( packet.speed_bottom, this );
                wraper.updateTurretAngle( packet.offset_from_bottom, this );
                wraper.updateReadyToFire( Math.abs( packet.offset_from_bottom ) < 11,
                                          this );
                wraper.updateLockedOn( ( packet.target_number & 0x0F ) != 0, this );
                break;
            case AutoTarget.RIGHT : // right
                wraper.updateTurretPower( packet.speed_right, this );
                wraper.updateTurretAngle( packet.offset_from_right, this );
                wraper.updateReadyToFire( Math.abs( packet.offset_from_right ) < 11,
                                          this );
                wraper.updateLockedOn( ( packet.target_number & 0x0F ) != 0, this );
                break;
        }
    }

    protected void echoPacket()
    {
        write( packet );
    }

    protected void requestNewData()
    {
        if ( getConnectionStatus() != NOT_CONNECTED )
            write( (byte)'W' );
    }

    /**
     * Tells Deep Thought to aim for a different hoop
     */
    public synchronized void setTargetHoopNumber(int target)
    {
        targetHoop = target;

        updateData();
    }

    public KPacket getPacket()
    {
        return packet;
    }

    /**
     * Returns an error code saying what state the connection is in
     * @return
     */
    public int getConnectionStatus()
    {
        // no input/output, no data
        if ( ( in == null ) || ( out == null ) || ( socket == null ) )
            return NOT_CONNECTED;

        // we have in/out. see if there's anything new
        try
        {
            return ( in.available() > 0 ) ? NEW_DATA : NO_NEW_DATA;
        }
        catch (IOException e)
        {
            log( "IO exception when checking for new data. closing all streams." );
            tryCloseAllStreams();
            return NOT_CONNECTED;
        }
    }

    protected void write(KPacket data)
    {
        write( data.data );
    }

    protected void write(byte[] data)
    {
        try
        {
            out.write( data );
            out.flush();
        }
        catch (IOException ex)
        {
            log( "IOException while writing a data packet. Closing streams." );
            tryCloseAllStreams();
        }
    }

    protected void write(byte data)
    {
        try
        {
            out.write( data );
            out.flush();
        }
        catch (IOException ex)
        {
            log( "IOException while writing a data packet. Closing streams." );
            tryCloseAllStreams();
        }
    }

    /**
     * Tries to read a 56-byte KinectDataPacket from the input stream.
     * @return null if less than 5 bytes can be read from the stream.
     */
    protected boolean readPacket()
    {
        try
        {
            if ( getConnectionStatus() != NEW_DATA )
            {
                return false;
            }

            int readable = in.available();

            if ( readable < packet.data.length )
            {
                return false;
            }

            for(int count = 0; count < readable; count++)
            {
                buffer[count] = in.readByte();
            }

            // find the most recent header packet in the buffer
            String tester = new String( buffer, 0, readable );
            int index     = -1;
            int index_max = -1;

            while( tester.length() - index > packet.data.length )
            {
                index = tester.indexOf( "Head", index + 1 );

                if ( index > index_max )
                {
                    index_max = index;
                }

                if ( index_max == -1 )
                {
                    return false;
                }

                if ( index == -1 )
                {
                    break;
                }
            }

            packet.copyOver( buffer, index_max, packet.data.length );
            packet.updateValues();

            return true;
        }
        catch (IOException ex)
        {
            log( "IOException while reading from input stream. Closing streams." );
            tryCloseAllStreams();

            return false;
        }
    }

    int fails = 0;

    protected boolean attemptConnection()
    {
        try
        {
            // try to connect, abandon if we fail
            try
            {
                socket = (SocketConnection)Connector.open( targetUrl );
            }
            catch (Exception ex)
            {
                if ( ( ++fails % 3 ) == 0 )
                    log( "connection failed. Error:\n" + ex.toString() );

                return false;
            }

            // we are connected!
            log( "connection established to   " + targetUrl );

            // socket settings don't work >.<
            log( "opening data streams" );

            // Get the input and output streams of the connection.
            in  = socket.openDataInputStream();
            out = socket.openDataOutputStream();

            // data streams open
            return true;
        }
        catch (Exception e)
        {
            Time.newCycle();
            log( "error while connecting\n" );
            log( e );
            tryCloseAllStreams();

            return false;
        }
    }

    public static void log(String s)
    {
        System.out.println( Time.string() + ":TCC......." + s );
    }

    public static void log(Exception e)
    {
        log( "" );
        e.printStackTrace();
    }

    protected void tryCloseAllStreams()
    {
        log( "closing all streams" );

        try
        {
            in.close();
        }
        catch (Exception e) {}

        try
        {
            out.close();
        }
        catch (Exception e) {}

        try
        {
            socket.close();
        }
        catch (Exception e) {}

        in     = null;
        out    = null;
        socket = null;
    }

    public void kill()
    {
        continues = false;

        tryCloseAllStreams();
        this.interrupt();
    }
}


//FIRST FRC team 691 2012 competition code
