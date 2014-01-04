/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc3128.Util.Connection;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.SPIDevice;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.fpga.tSPI;
import frc3128.EventManager.Event;
import frc3128.Util.DebugLog;

/**Connects over SPI with the Beaglebone
 *
 * @author jamie
 */
public class Beaglebone extends Event
{
    
    /*
    BEAGLEBONE:
    P9_22: SCLK P9_18: DI P9_21: DO P9_17: CS0
    
    DIGITAL SIDECAR:
    GPIO1: MOSI GPIO2: MISO GPIO3: SCLK GPIO4: CS
    */
    
    //YESSSSSSS
    //found some documentation at http://www.chiefdelphi.com/forums/showthread.php?p=1081779
    SPIDevice spi;
    DigitalModule module;
    int slot;
    
    final static int clockPin = 3;
    final static int dataOutPin = 2;
    final static int dataInPin = 1;
    final static int chipSelectPin = 4;
    
    public Beaglebone(int slotParam)
    {
        slot = slotParam;

        
        spi = new SPIDevice(slot, clockPin, dataOutPin, dataInPin, chipSelectPin);
    } 
    
    /**
     * gets one byte from SPI (MOSI)
     * 
     * @return 
     */
    long readSPI()
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
