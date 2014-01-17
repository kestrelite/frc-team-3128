/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc3128.Util.Connection;

import javax.microedition.io.ServerSocketConnection;
import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;

/**Connects over SPI with the Beaglebone
 *
 * @author jamie
 */
public class Beaglebone extends Event
{
    ServerSocketConnection socketServer;
    
    public Beaglebone()
    {
        socketServer = new ServerSocketConnection();
    } 
    
    /**
     * 
     * 
     * @return 
     */
    byte[] readCommand()
    {
        // read the data
        long rawData = 0xA;
        
        spi.transfer(rawData, 1);
        
        return rawData;
    }

    public void execute()
    {
        
        long data = readSPI();
        DebugLog.log(DebugLog.LVL_INFO, this, "SPI read " + Long.toString(data, 16));
    }
    
    

    
}
