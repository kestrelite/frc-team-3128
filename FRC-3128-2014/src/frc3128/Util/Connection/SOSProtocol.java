/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frc3128.Util.Connection;

/**
 *
 * @author jamie
 */
public class SOSProtocol 
{
    static char START_TRANSMISSION = 0x01;
    static char END_OPCODE = 0x04;
    static char START_SHORT_TRANSMISSION = 0x09;
    static char END_SHORT = 0x0B;
    static char START_STRING_TRANSMISSION = 0x02;
    static char END_STRING = 0x03;
    static char END_TRANSMISSION = 0x06;
}
